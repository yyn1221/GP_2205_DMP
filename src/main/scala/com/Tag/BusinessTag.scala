package com.Tag

import ch.hsr.geohash.GeoHash
import com.utils.{AmapUtils, RedisPool, Tag, Utils2Type}
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row

/**
  * 商圈标签
  */
object BusinessTag extends Tag{
    /**
      * 打标签的统一接口
      */
    override def makeTags(args: Any*): List[(String, Int)] = {
        var list = List[(String,Int)]()
        //解析参数
        val row = args(0).asInstanceOf[Row]
        val long = row.getAs[String]("long")
        val lat = row.getAs[String]("lat")
        //获取经纬度
        if(Utils2Type.toDouble(long)>=73.0
                && Utils2Type.toDouble(long) <= 135.0
                && Utils2Type.toDouble(lat)>=3.0
                && Utils2Type.toDouble(lat) <= 54.0){
            //先去数据库获取商圈
            val business = getBusiness(long.toDouble,lat.toDouble)
            //判断缓存中是否有此商圈
            if(StringUtils.isNotBlank(business)){
                val lines = business.split(",")

                lines.foreach(f => list:+=(f,1))
            }
        }
        list
    }

    /**
      * 获取数据库商圈信息
      */
    def getBusiness(long:Double,lat:Double):String={
        //转化Geo Hasg字符串
        val geoHash =
            GeoHash.geoHashStringWithCharacterPrecision(lat,long,8)
        //数据库查询
        val business = redis_queryBusiness(geoHash)
        //判断缓存中是否有此商圈
        if(business==null || business.length==0){
            //通过经纬度获取商圈
            val business = AmapUtils.getBusinessFromAmap(long.toDouble, lat.toDouble)
            //如果调用高德地图解析商圈  那么需要将此次商圈存入redis
            redis_insertBusiness(geoHash,business)
        }
        business
    }

    /**
      * 获取商圈信息
      */
    def redis_queryBusiness(geohash:String):String={
        val jedis = RedisPool.getConnection()
        val business = jedis.get(geohash)
        jedis.close()
        business
    }

    /**
      * 将商圈存到redis
      */
    def redis_insertBusiness(geoHash: String,business:String)={
        val jedis = RedisPool.getConnection()
        jedis.set(geoHash,business)
        jedis.close()
    }
}
