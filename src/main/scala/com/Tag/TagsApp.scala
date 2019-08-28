package com.Tag

import com.utils.Tag
import org.apache.commons.lang3.StringUtils
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.Row

object TagsApp extends  Tag{
    /**
      * 打标签的统一结构
      */
    override def makeTags(args: Any*): List[(String, Int)] = {
        var list = List[(String,Int)]()
        //解析参数
        val row = args(0).asInstanceOf[Row]
        val broadcast = args(1).asInstanceOf[Broadcast[Map[String, String]] ]
        //获取App名
        val appId = row.getAs[String]("appid")
        val appName = row.getAs[String]("appname")
        if(StringUtils.isNotBlank(appName)){
            list:+=("APP"+appName,1)
        }else if(StringUtils.isNoneBlank(appId)){
            list:+("APP"+broadcast.value.getOrElse(appId,appId),1)
        }
        list
    }
}
