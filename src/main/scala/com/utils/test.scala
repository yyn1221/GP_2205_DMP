package com.utils

import com.Tag.BusinessTag
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object test {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
                .setAppName(this.getClass.getName).setMaster("local[*]")
        val sc = new SparkContext(conf)
        val sqlContext = new SQLContext(sc)

        val df = sqlContext.read.parquet("H://千峰项目/output")
        df.map(row =>{
            val business = BusinessTag.makeTags(row)
            business
        })
                .foreach(println)

//        val Array(inputPath) = args
//        val list = List("116.310003,39.991957")
//        val df = sqlContext.read.parquet(inputPath)
//        val buffer: mutable.Buffer[(String, String)] = df.filter(TagUtils.OneUserId)
//                .map(row => {
//                    val lang = row.getAs[String]("long")
//                    val lat = row.getAs[String]("long")
//                    (lang, lat)
//                }).collect().toBuffer
//        buffer
//
//        println(buffer)


//        val rdd = sc.makeRDD(list)
//        val bs = rdd.map(t => {
//            val arr = t.split(",")
//            AmapUtils.getBusinessFromAmap(arr(0).toDouble,arr(1).toDouble)
//        })
//        bs.foreach(println)
    }
}
