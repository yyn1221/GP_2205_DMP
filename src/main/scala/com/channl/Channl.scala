package com.channl

import com.utils.RqtUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext

object Channl {

  def main(args: Array[String]): Unit = {
    //判断路径是否正确
    if(args.length != 2){
      println("目录参数不正确，退出")
      sys.exit()
    }
    //创建一个集合保存输入和输出目录
    val Array(inputPath,outputPath) = args

    val conf = new SparkConf().setAppName(this.getClass.getName).setMaster("local[*]")
      //设置序列化方式 采用kyro序列化方式，比默认序列化方式性能高
      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")

    //创建之行入口
    val sc = new SparkContext(conf)
    val sQLContext = new SQLContext(sc)
    val df = sQLContext.read.parquet(inputPath)

    df.registerTempTable("location")

    val files=df.map(row => {
      //把需要的字段全部取到
      val requestmode = row.getAs[Int]("requestmode")
      val processnode = row.getAs[Int]("processnode")
      val iseffective = row.getAs[Int]("iseffective")
      val isbilling = row.getAs[Int]("isbilling")
      val isbid = row.getAs[Int]("isbid")
      val iswin = row.getAs[Int]("iswin")
      val adorderid = row.getAs[Int]("adorderid")
      val winprice = row.getAs[Double]("winprice")
      val adpayment = row.getAs[Double]("adpayment")

      // key值 是 地狱的省市
      val channelid = row.getAs[String]("channelid")

      (channelid, RqtUtils.request(requestmode, processnode) ++ ((RqtUtils.click(requestmode, iseffective)) ++
        RqtUtils.Ad(iseffective, isbilling, isbid, iswin, adorderid, winprice, adpayment)))
    })

    //    files.groupByKey()
    //      .map(x => (x._1,x._2.fold(List(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0))((x,y) => (x zip y).map(x => x._1+x._2))))
    //      .foreach(println)

    //    files.groupByKey()
    //      .map(x => (x._1,x._2.reduce((x,y) => (x.zip(y)).map(x => x._1+x._2))))
    //        .foreach(println)

    //根据key聚合value
    files.reduceByKey(
      (list1, list2) => {
        list1.zip(list2)
          .map(x => x._1 + x._2)
      }).foreach(println)
    //      .map(x =>{
    //      x._1+","+x._2.mkString(",")
    //    })
  }

}
