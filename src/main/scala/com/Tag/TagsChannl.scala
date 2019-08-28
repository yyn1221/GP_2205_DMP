package com.Tag

import com.utils.Tag
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row

object TagsChannl extends  Tag{
    /**
      * 打标签的统一结构
      */
    override def makeTags(args: Any*): List[(String, Int)] = {
        var list = List[(String,Int)]()
        //解析参数
        val row = args(0).asInstanceOf[Row]
        //获取渠道名
        val channlId = row.getAs[Int]("adplatformproviderid")
            list:+=("CN"+channlId,1)

        list
    }
}
