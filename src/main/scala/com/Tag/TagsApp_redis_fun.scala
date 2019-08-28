package com.Tag

import com.utils.{RedisPool, Tag}
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row
import redis.clients.jedis.Jedis

object TagsApp_redis_fun extends Tag{
    /**
      * 打标签的统一结构
      */
    override def makeTags(args: Any*): List[(String, Int)] = {
        var list = List[(String, Int)]()
        val row = args(0).asInstanceOf[Row]
        val appId = row.getAs[String]("appid")
        val appName = row.getAs[String]("appname")
        val jedis: Jedis = RedisPool.getConnection()
//        var jedis = new Jedis("hadoop01",6379)
        if(StringUtils.isNotBlank(appName)){
            list:+=("APP"+appName,1)
        }else if(StringUtils.isNoneBlank(appId)){
            if(jedis.hget("s0",appId) != null) {
                list :+ ("APP" + jedis.hget("s0", appId), 1)
            }
        }
        jedis.close()

        list
    }

}
