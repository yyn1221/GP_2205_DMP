package com.ETL

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

object PartDistribution {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName(this.getClass.getName).setMaster("local[2]")
    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)

    val Array(inputPath,outputPath) = args
    val files: DataFrame = sqlContext.read.parquet(inputPath)

    val uidRDD = files.rdd.map(x =>{
      val requestmode = x(8)
      val processnode = x(35)
      val iseffective = x(30)
      val isbilling = x(31)
      val isbid = x(39)
      val iswin = x(42)
      val adorderid = x(2)
      Row(x(46),x(48),x(49),x(50),x(51),x(62),x(63),x(64),x(65),x(66),x(67),x(68),x(69),x(70),x(71),requestmode,processnode,iseffective,isbilling,isbid,iswin,adorderid)
    })

    val rowRDD: RDD[Row] = uidRDD.map(x => {
      var str: String = ""
      if (x.get(0) != "")
        str += x.get(0) + ","
      if (x.get(1) != "")
        str += x.get(1) + ","
      if (x.get(2) != "")
        str += x.get(2) + ","
      if (x.get(3) != "")
        str += x.get(3) + ","
      if (x.get(4) != "")
        str += x.get(4) + ","
      if (x.get(5) != "")
        str += x.get(5) + ","
      if (x.get(6) != "")
        str += x.get(6) + ","
      if (x.get(7) != "")
        str += x.get(7) + ","
      if (x.get(8) != "")
        str += x.get(8) + ","
      if (x.get(9) != "")
        str += x.get(9) + ","
      if (x.get(10) != "")
        str += x.get(10) + ","
      if (x.get(11) != "")
        str += x.get(11) + ","
      if (x.get(12) != "")
        str += x.get(12) + ","
      if (x.get(13) != "")
        str += x.get(13) + ","
      if (x.get(14) != "")
        str += x.get(14)
      (str, x.get(15), x.get(16), x.get(17), x.get(18), x.get(19), x.get(20), x.get(21))
    }).filter(_._1 != "").map(x => Row(x._1.split(",")(0), x._2, x._3, x._4, x._5, x._6, x._7, x._8))



    rowRDD.foreach(println)








  }

}
