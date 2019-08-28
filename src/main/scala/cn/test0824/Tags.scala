package test0824

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext

import scala.collection.mutable

object Tags {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName(this.getClass.getName)
                .setMaster("local[*]")
        val sc = new SparkContext(conf)
        val sQLContext = new SQLContext(sc)

        val log: RDD[String] = sc.textFile("H://千峰项目/json.txt")

        val logs: mutable.Buffer[String] = log.collect().toBuffer
        var list: List[String] = List()
        for(i <- 0 until logs.length) {
            val str: String = logs(i).toString

            val jsonparse: JSONObject = JSON.parseObject(str)

            val status = jsonparse.getIntValue("status")
            if (status == 0) return ""

            val regeocodeJson = jsonparse.getJSONObject("regeocode")
            if (regeocodeJson == null || regeocodeJson.keySet().isEmpty) return ""

//            val addressComponentJson: JSONObject = regeocodeJson.getJSONObject("addressComponent")
//            if (addressComponentJson == null || addressComponentJson.keySet().isEmpty) return ""

            val poisArray = regeocodeJson.getJSONArray("pois")
            if (poisArray == null || poisArray.isEmpty) return null

            // 创建集合 保存数据
            val buffer = collection.mutable.ListBuffer[String]()
            // 循环输出
            for (item <- poisArray.toArray) {
                if (item.isInstanceOf[JSONObject]) {
                    val json = item.asInstanceOf[JSONObject]
                    buffer.append(json.getString("type"))
                }
            }

            list:+=buffer.mkString(";")
        }
//        println(list)

        val res2 = list
                .flatMap(x => x.split(";"))
                .map(x => (x, 1))
                .groupBy(x => x._1)
                .mapValues(x => x.length)

        res2.foreach(x => println(x))

    }
}