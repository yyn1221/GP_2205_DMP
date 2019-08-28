package com.utils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import redis.clients.jedis.Jedis

object App2Redis {

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
                .setAppName(this.getClass.getName).setMaster("local[*]")
        val sc = new SparkContext(conf)
        val  sqlContext = new SQLContext(sc)
        val dict = sc.textFile("H:\\千峰项目\\资料包\\Spark用户画像分析\\app_dict.txt")
        //处理字典文件
        dict.map(_.split("\t", -1))
                .filter(_.length >= 5)
                .foreachPartition(x => {
                    val jedis: Jedis = RedisPool.getConnection()
                    x.foreach(x => {
                        jedis.hset("s0", x(4), x(1))
                    })
                    jedis.close()
                })

    }

}
