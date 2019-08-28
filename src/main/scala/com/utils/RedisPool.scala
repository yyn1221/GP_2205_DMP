package com.utils

import com.typesafe.config.ConfigFactory
import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

object RedisPool {
    private var jedisSource: JedisPool = null

    //初始化连接池
    def getJedisSource() :JedisPool ={

        val load = ConfigFactory.load()
//        val prop = new Properties()
//        prop.load(new FileReader("src/main/resources/redis.properties"))

        val jconf = new JedisPoolConfig()
//        prop.setProperty("user",load.getString("jdbc.user"))
        jconf.setMaxTotal(load.getInt("redis.MaxTotal"))
        jconf.setMaxIdle(load.getInt("redis.timeout"))
        jconf.setMaxWaitMillis(load.getInt("redis.timeout"))
        val pool = new JedisPool(jconf,load.getString("redis.host"),load.getInt("redis.port"))
//        jconf.setMaxTotal(prop.getProperty("redis.MaxTotal").toInt)
//        jconf.setMaxIdle(prop.getProperty("redis.MaxIdle").toInt)
//        jconf.setMaxWaitMillis(prop.getProperty("redis.timeout").toLong)
//        val pool = new JedisPool(jconf,prop.getProperty("redis.host"),prop.getProperty("redis.port").toInt)
        pool
    }

    //获取连接对象
    def getConnection() :Jedis ={
        if (jedisSource == null){
            jedisSource = getJedisSource()
        }
        jedisSource.getResource
    }
}
