package com.Tag

import com.utils.{RedisPool, TagUtils}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext}
import redis.clients.jedis.Jedis

object TagsApp_redis {
    def main(args: Array[String]): Unit = {

        val conf = new SparkConf()
                .setAppName(this.getClass.getName).setMaster("local[*]")
        val sc = new SparkContext(conf)
        val  sqlContext = new SQLContext(sc)

        //创建jedis连接
//        var jedis = new Jedis("hadoop01",6379)
        //读取数据
        val Array(inputPath1,inputPath2, outputPath)= args
        val df = sc.textFile(inputPath2)
        val videofile = df.map(_.split("\t", -1)).filter(_.length >= 5).map(x => {
            var id = x(4)
            var name = x(1)
            (id, name)
        })
//                .collectAsMap()
//                .foreach(j => jedis.hset("s0",j._1,j._2))
                .foreachPartition( x =>{
                    val jedis: Jedis = RedisPool.getConnection()
                    x.foreach(j => jedis.hset("s0",j._1,j._2))
//                    jedis.close()
        })

        val df1 = sqlContext.read.parquet(inputPath1)
                df1.filter(TagUtils.OneUserId)
                        .map(row => {
                            //取出用户id
                            val app: List[(String, Int)] = TagsApp_redis_fun.makeTags(row)
                            (app)
                        }).foreach(println)
//        jedis.close()
        sc.stop()
    }
}
