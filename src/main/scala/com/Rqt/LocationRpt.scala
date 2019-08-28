package com.Rqt

import java.sql.{Connection, PreparedStatement}

import com.utils.RqtUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SQLContext}

object LocationRpt {

  def main(args: Array[String]): Unit = {
    //判断路径是否正确
    if(args.length != 2){
      println("目录参数不正确，退出")
      sys.exit()
    }
    //创建一个集合保存输入和输出目录
    val Array(inputPath,outputPath) = args

    val conf = new SparkConf().setAppName(this.getClass.getName).setMaster("local[*]")
      //设置序列化方式 采用kyro序列化方式，比默认序列化方式性能高
      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")

    //创建之行入口
    val sc = new SparkContext(conf)
    val sQLContext = new SQLContext(sc)
    val df = sQLContext.read.parquet(inputPath)

    df.registerTempTable("location")

    val files: RDD[((String, String), List[Double])] = df.map(row => {
      //把需要的字段全部取到
      val requestmode = row.getAs[Int]("requestmode")
      val processnode = row.getAs[Int]("processnode")
      val iseffective = row.getAs[Int]("iseffective")
      val isbilling = row.getAs[Int]("isbilling")
      val isbid = row.getAs[Int]("isbid")
      val iswin = row.getAs[Int]("iswin")
      val adorderid = row.getAs[Int]("adorderid")
      val winprice = row.getAs[Double]("winprice")
      val adpayment = row.getAs[Double]("adpayment")

      // key值 是 地狱的省市
      val pro = row.getAs[String]("provincename")
      val city = row.getAs[String]("cityname")

      ((pro, city), RqtUtils.request(requestmode, processnode) ++ ((RqtUtils.click(requestmode, iseffective)) ++
        RqtUtils.Ad(iseffective, isbilling, isbid, iswin, adorderid, winprice, adpayment)))
    })

    files.reduceByKey(
      (list1, list2) => {
        list1.zip(list2)
          .map(x => x._1 + x._2)
      }) // .foreach(println)
      .foreachPartition(item => {
      //获取连接
      val conn: Connection = JDBCPool.getConn()
      item.foreach(one => {
        val pstm: PreparedStatement = conn.prepareStatement("insert into  location" +
          "(province,city , " +
          "orgin ,effect ,adeffect ," +
          "adshow ,adclick, " +
          "clickrate  ," +
          "bid ,bidsucc, " +
          "bidsuccrate , " +
          "consume_1000 ,cost_1000 ) " +
          "values(?,?,?,?,?,?,?,?,?,?,?,?,?)")
//        val str = one.split(",")
        pstm.setString(1, one._1._1)
        pstm.setString(2, one._1._2)
        pstm.setDouble(3, one._2(0))
        pstm.setDouble(4, one._2(1))
        pstm.setDouble(5, one._2(2))
        pstm.setDouble(6, one._2(3))
        pstm.setDouble(7, one._2(4))
        pstm.setDouble(8, one._2(5))
        pstm.setDouble(9, one._2(6))
        pstm.setDouble(10, one._2(7))
        pstm.setDouble(11, one._2(8))
        pstm.setDouble(12,one._2(9))
        pstm.setDouble(13, one._2(10))
        pstm.executeUpdate()
      })
      JDBCPool.returnConn(conn)
      conn.close()
    })

    sc.stop()
  }

}


//    files.groupByKey()
//      .map(x => (x._1,x._2.fold(List(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0))((x,y) => (x zip y).map(x => x._1+x._2))))
//      .foreach(println)

//    files.groupByKey()
//      .map(x => (x._1,x._2.reduce((x,y) => (x.zip(y)).map(x => x._1+x._2))))
//        .foreach(println)

//      .map(x => {
//        x._1._1 + "," + x._1._2 + "," + x._2.mkString(",")
//      })