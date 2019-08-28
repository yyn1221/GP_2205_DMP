package com.Tag

import com.typesafe.config.ConfigFactory
import com.utils.TagUtils
import org.apache.hadoop.hbase.{HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
/**
  *  上下文标签
  */
object TagContext {
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
        // todo 调用Hbase API
        //加载配置文件
        val load = ConfigFactory.load()
        val hbaseTableName = load.getString("hbase.TableName")
//        val hbaseTableName ="gp"
        //创建hadoop任务
        val configuration = sc.hadoopConfiguration
        configuration.set("hbase.zookeeper.quorum",load.getString("hbase.host"))
//        val configuration = HBaseConfiguration.create()
//        configuration.set("hbase.zookeeper.quorum","hadoop01")
//        configuration.set("hbase.zookeeper.property.clientPort","2181")
        //创建HBaseConnection
        val hbconn = ConnectionFactory.createConnection(configuration)
        val hbadmin = hbconn.getAdmin
        //判断表是否可用
        if(!hbadmin.tableExists(TableName.valueOf(hbaseTableName))){
            // 创建表操作
            val tableDescriptor = new HTableDescriptor(TableName.valueOf(hbaseTableName))
            val descriptor = new HColumnDescriptor("tags")
            tableDescriptor.addFamily(descriptor)
            hbadmin.createTable(tableDescriptor)
            hbadmin.close()
            hbconn.close()
        }
        //创建JobConf
        val jobconf = new JobConf(configuration)
        // 指定输出类型和表
        jobconf.setOutputFormat(classOf[TableOutputFormat])
        jobconf.set(TableOutputFormat.OUTPUT_TABLE,hbaseTableName)
        //读取数据
        val df1 = sc.textFile(inputPath2)
        val video = df1.map(_.split("\t", -1)).filter(_.length >= 5).map(x =>{
            var id = x(4)
            var name = x(1)
            (id,name)
        }).collectAsMap()
        val broadcast = sc.broadcast(video)

        val df2 = sc.textFile(inputPath3)
        //获取停用词库
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

                    val appList = TagsApp.makeTags(row,broadcast)

                    val channlList = TagsChannl.makeTags(row)

                    val terminalList = TagsTerminal.makeTags(row)

                    val keywordList = TagsKeyWord.makeTags(row,broadcast1)

                    val pro_city = TagsProvince_City.makeTags(row)
                    val business = BusinessTag.makeTags(row)
                    (userId,adList++appList++channlList++terminalList++keywordList++pro_city++business)
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
        )
                .map{
                    case (userid, userTag) => {
                        val put = new Put(Bytes.toBytes(userid))
                        //处理标签
                        val tags = userTag.map(t => t._1 + "," + t._2).mkString(",")
                        put.addImmutable(Bytes.toBytes("tags"), Bytes.toBytes(s"$days"), Bytes.toBytes(tags))
                        (new ImmutableBytesWritable(), put)
                    }
                }
                //保存到对应表
                .saveAsHadoopDataset(jobconf)
//                .foreach(println)

    }
}
