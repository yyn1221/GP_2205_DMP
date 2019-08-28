package com.utils

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row

/**
  *  标签工具类
  *  object 相当于单例模式 内部可以直接初始化所有的方法
  *  class 连接调用不到 不能用new关键字实例化一个单例对象,你没有机会传递给它参数
  */
object TagUtils {
    //过滤需要的字段
    val OneUserId=
        """
          | imei != '' or mac != '' or openudid != '' or androidid != '' or idfa != '' or
          |  imeimd5 != '' or macmd5 != '' or openudidmd5 != '' or androididmd5 != '' or idfamd5 != '' or
          |   imeisha1 != '' or macsha1 != ''  or openudidsha1 != '' or androididsha1 != '' or idfasha1 != ''
        """.stripMargin

    //取唯一不为空id
    def getOneUserId(row : Row): String ={
        row match {
            case v if StringUtils.isNoneBlank(v.getAs[String]("imei")) => "IM: "+ v.getAs[String]("imei")
            case v if StringUtils.isNoneBlank(v.getAs[String]("mac")) => "mac: "+ v.getAs[String]("mac")
            case v if StringUtils.isNoneBlank(v.getAs[String]("openudid")) => "openudid: "+ v.getAs[String]("openudid")
            case v if StringUtils.isNoneBlank(v.getAs[String]("androidid")) => "androidid: "+ v.getAs[String]("androidid")
            case v if StringUtils.isNoneBlank(v.getAs[String]("idfa")) => "idfa: "+ v.getAs[String]("idfa")
            case v if StringUtils.isNoneBlank(v.getAs[String]("imeimd5")) => "imeimd5: "+ v.getAs[String]("imeimd5")
            case v if StringUtils.isNoneBlank(v.getAs[String]("macmd5")) => "macmd5: "+ v.getAs[String]("macmd5")
            case v if StringUtils.isNoneBlank(v.getAs[String]("openudidmd5")) => "openudidmd5: "+ v.getAs[String]("openudidmd5")
            case v if StringUtils.isNoneBlank(v.getAs[String]("androididmd5")) => "androididmd5 "+ v.getAs[String]("androididmd5")
            case v if StringUtils.isNoneBlank(v.getAs[String]("idfamd5")) => "idfamd5: "+ v.getAs[String]("idfamd5")
            case v if StringUtils.isNoneBlank(v.getAs[String]("imeisha1")) => "imeisha1: "+ v.getAs[String]("imeisha1")
            case v if StringUtils.isNoneBlank(v.getAs[String]("macsha1")) => "macsha1 "+ v.getAs[String]("macsha1")
            case v if StringUtils.isNoneBlank(v.getAs[String]("openudidsha1")) => "openudidsha1: "+ v.getAs[String]("openudidsha1")
            case v if StringUtils.isNoneBlank(v.getAs[String]("androididsha1")) => "androididsha1: "+ v.getAs[String]("androididsha1")
            case v if StringUtils.isNoneBlank(v.getAs[String]("idfasha1")) => "idfasha1: "+ v.getAs[String]("idfasha1")

        }
    }
    //获取所有ID
    def getAllUserId(row:Row):List[String]={
        var list = List[String]()
        if (StringUtils.isNoneBlank(row.getAs[String]("imei"))) list:+= "IM: "+ row.getAs[String]("imei")
        if (StringUtils.isNoneBlank(row.getAs[String]("mac"))) list:+= "mac: "+ row.getAs[String]("mac")
        if (StringUtils.isNoneBlank(row.getAs[String]("openudid")) ) list:+="openudid: "+ row.getAs[String]("openudid")
        if (StringUtils.isNoneBlank(row.getAs[String]("androidid")) ) list:+= "androidid: "+ row.getAs[String]("androidid")
        if (StringUtils.isNoneBlank(row.getAs[String]("idfa")) ) list:+= "idfa: "+ row.getAs[String]("idfa")
        if (StringUtils.isNoneBlank(row.getAs[String]("imeimd5")) ) list:+= "imeimd5: "+ row.getAs[String]("imeimd5")
        if (StringUtils.isNoneBlank(row.getAs[String]("macmd5")) ) list:+= "macmd5: "+ row.getAs[String]("macmd5")
        if (StringUtils.isNoneBlank(row.getAs[String]("openudidmd5")) ) list:+= "openudidmd5: "+ row.getAs[String]("openudidmd5")
        if (StringUtils.isNoneBlank(row.getAs[String]("androididmd5")) ) list:+= "androididmd5 "+ row.getAs[String]("androididmd5")
        if (StringUtils.isNoneBlank(row.getAs[String]("idfamd5")) ) list:+= "idfamd5: "+ row.getAs[String]("idfamd5")
        if (StringUtils.isNoneBlank(row.getAs[String]("imeisha1")) ) list:+= "imeisha1: "+ row.getAs[String]("imeisha1")
        if (StringUtils.isNoneBlank(row.getAs[String]("macsha1")) ) list:+= "macsha1 "+ row.getAs[String]("macsha1")
        if (StringUtils.isNoneBlank(row.getAs[String]("openudidsha1")) ) list:+= "openudidsha1: "+ row.getAs[String]("openudidsha1")
        if (StringUtils.isNoneBlank(row.getAs[String]("androididsha1")) ) list:+= "androididsha1: "+ row.getAs[String]("androididsha1")
        if (StringUtils.isNoneBlank(row.getAs[String]("idfasha1")) ) list:+= "idfasha1: "+ row.getAs[String]("idfasha1")
        list
    }
}
