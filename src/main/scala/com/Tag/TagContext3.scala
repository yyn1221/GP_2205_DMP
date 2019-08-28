package com.Tag

import com.utils.TagUtils
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.graphx.{Edge, Graph, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
/**
  *  上下文标签
  */
object TagContext3 {
    def main(args: Array[String]): Unit = {
        if(args.length != 5){
            println("目录不匹配,退出程序")
            sys.exit()
        }
        val Array(intputPath1,inputPath2,inputPath3, outputPath,days)= args
        val conf = new SparkConf()
                .setAppName(this.getClass.getName).setMaster("local[*]")
        val sc = new SparkContext(conf)
        val  sqlContext = new SQLContext(sc)
        //读取数据

        val df1 = sc.textFile(inputPath2)
        val video = df1.map(_.split("\t", -1)).filter(_.length >= 5).map(x =>{
            var id = x(4)
            var name = x(1)
            (id,name)
        }).collectAsMap()
        val broadcast = sc.broadcast(video)

        val df2 = sc.textFile(inputPath3)
        val word = df2.map((_,0)).collectAsMap()
        val broadcast1 = sc.broadcast(word)

        val df = sqlContext.read.parquet(intputPath1)
        val baseRDD: RDD[(List[String], Row)] = df.filter(TagUtils.OneUserId)
                .map(row => {
                    val userList: List[String] = TagUtils.getAllUserId(row)
                    (userList, row)
                })

        //构建点集合
        val vertiesRDD: RDD[(Long, List[(String, Int)])] = baseRDD.flatMap(tp => {
            val row: Row = tp._2
            val adList: List[(String, Int)] = TagsAd.makeTags(row)
            val appList = TagsApp.makeTags(row, broadcast)
            val terminalList = TagsTerminal.makeTags(row)
            val keywordList = TagsKeyWord.makeTags(row, broadcast1)
            val pro_city = TagsProvince_City.makeTags(row)
            val business: List[(String, Int)] = BusinessTag.makeTags(row)
            val AllTag = adList ++ appList ++ terminalList ++ keywordList ++ pro_city ++ business

            val VD: List[(String, Int)] = tp._1.map((_, 0)) ++ AllTag
            tp._1.map(uId => {
                if (tp._1.head.equals(uId)) {
                    (uId.hashCode.toLong, VD)
                } else {
                    (uId.hashCode.toLong, List.empty)
                }
            })

        })
//        vertiesRDD.take(20).foreach(println)

        val edges: RDD[Edge[Int]] = baseRDD.flatMap(tp => {
            tp._1.map(uId => Edge(tp._1.head.hashCode, uId.hashCode, 0))
        })
//        edges.take(20).foreach(println)
        //构件图
        val graph: Graph[List[(String, Int)], Int] = Graph(vertiesRDD,edges)
        //取出顶点 使用的是
        val vertices: VertexRDD[VertexId] = graph.connectedComponents().vertices
        //处理所偶的标签和id
        vertices.join(vertiesRDD).map{
            case  (uId,(conId,tagsAll)) => (conId,tagsAll)
        }.reduceByKey((list1,list2)=>{
            (list1++list2).groupBy(_._1).mapValues(_.map(_._2).sum).toList
        })
                .take(20).foreach(println)
        sc.stop()

    }
}
