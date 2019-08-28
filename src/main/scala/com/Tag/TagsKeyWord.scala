package com.Tag

import com.utils.Tag
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.Row

object TagsKeyWord extends Tag{
    /**
      * 打标签的统一结构
      */
    override def makeTags(args: Any*): List[(String, Int)] = {
        var list = List[(String, Int)]()
        val row = args(0).asInstanceOf[Row]
        val word = args(1).asInstanceOf[Broadcast[Map[String, String]]]
        val keys = row.getAs[String]("keywords").split("\\|")
        keys.filter(w => {
            w.length >= 3 && w.length <= 8 && !word.value.contains(w)
        })
                .foreach(x => list:+=("K"+x,1))

        list

    }
}
