package cn.test0824

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Test {

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName(this.getClass.getName).setMaster("local[*]")
        val sc = new SparkContext(conf)
        val sqlContext = new SQLContext(sc)
        val log: RDD[String] = sc.textFile("H://千峰项目/json.txt")

        }
}
/**
val logs: mutable.Buffer[String] = log.collect().toBuffer

        var list: List[String] = List()
        for (i <- 0 until logs.length) {
            val jsonstr: String = logs(i).toString
            //解析json
            val jsonparse: JSONObject = JSON.parseObject(jsonstr)
            //判断状态是否成功
            val status = jsonparse.getIntValue("status")
            if (status == 0) return ""
            // 接下来解析内部json串,判断每个key的valus都不为空
            val regeocodeJson = jsonparse.getJSONObject("regeocode")
            if (regeocodeJson == null || regeocodeJson.keySet().isEmpty) return ""

            val poisArray = regeocodeJson.getJSONArray("pois")
            if (poisArray == null || poisArray.isEmpty) return null

            // 创建集合 保存数据
            val buffer = collection.mutable.ListBuffer[String]()

            for(item <- poisArray.toArray){
                if(item.isInstanceOf[JSONObject]){
                    val json = item.asInstanceOf[JSONObject]

                    if(json.getString("businessarea") != "[]" &&  json.getString("businessarea") !=""){
                        buffer.append(json.getString("businessarea"))
                    }
                }
            }
            if(buffer.size>0)
                list:+=buffer.mkString(",")
        }
        list.foreach(println)

        val res1 = list.flatMap(x => x.split(","))
                .map(x => (x, 1))
//                .filter(_._1!="[]")
                .groupBy(x => x._1)
                .mapValues(x => x.size)
        res1.foreach(println)

  */