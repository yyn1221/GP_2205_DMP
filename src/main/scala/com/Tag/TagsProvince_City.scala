package com.Tag

import com.utils.Tag
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row

object TagsProvince_City extends Tag{
    /**
      * 打标签的统一结构
      */
    override def makeTags(args: Any*): List[(String, Int)] = {
        var list = List[(String,Int)]()
        val row = args(0).asInstanceOf[Row]
        val province = row.getAs[String]("provincename")
        val city = row.getAs[String]("cityname")
        if(StringUtils.isNotBlank("cityname") ){
            list:+=("ZP"+province,1)
        }
        if(StringUtils.isNotBlank("provincename")){
            list:+=("ZC"+city,1)
        }
        list
    }
}
