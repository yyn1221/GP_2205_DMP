package com.Tag
import com.utils.TagUtils
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
/**
  *  上下文标签
  */
object TagContext2 {
    def main(args: Array[String]): Unit = {
        if(args.length != 4){
            println("目录不匹配,退出程序")
            sys.exit()
        }
        val Array(intputPath1,inputPath2,inputPath3, outputPath)= args
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
        df.filter(TagUtils.OneUserId)
                .map(row => {
                    //取出用户id
                    val userId = TagUtils.getOneUserId(row)
                    //通过row数据 打上 所有标签(按需求)
                    //广告位类型标签
                    val adList: List[(String, Int)] = TagsAd.makeTags(row)

//                    val appList = TagsApp.makeTags(row,broadcast)
                    val appList = TagsApp_redis_fun.makeTags(row)

                    val channlList = TagsChannl.makeTags(row)

                    val terminalList = TagsTerminal.makeTags(row)

                    val keywordList = TagsKeyWord.makeTags(row,broadcast1)

                    val pro_city = TagsProvince_City.makeTags(row)
                    (userId,adList++appList++channlList++terminalList++keywordList++pro_city)
                })
//                .foreach(println)
                .reduceByKey((list1, list2) =>
                    //List((LNxxx,1),(),()...)
                    (list1:::list2)       //将list都加到list2上
                            //List(("爱奇艺"),List()))
                            .groupBy(_._1)
//                            .mapValues(_.size)
                            .mapValues(_.foldLeft[Int](0)(_+_._2))
                .toList
                ).foreach(println)

    }
}
