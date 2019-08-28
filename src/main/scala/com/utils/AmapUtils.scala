package com.utils

import com.alibaba.fastjson.{JSON, JSONObject}

/**
  * 商圈解析工具
  */
object AmapUtils {
    //获取高德地图商圈信息
    def getBusinessFromAmap(lang:Double,lat:Double):String={
        val location = lang+","+lat
        val urlStr = "https://restapi.amap.com/v3/geocode/regeo?location=" + location + "&key=c1b9847f94b9ab6bc3ea8b01a6cd7022"
        //调用请求
        val jsonstr = HttpUtils.get(urlStr)
        //解析json
        val jsonparse: JSONObject = JSON.parseObject(jsonstr)
        //判断状态是否成功
        val status = jsonparse.getIntValue("status")
        if(status==0) return ""
        // 接下来解析内部json串,判断每个key的valus都不为空
        val regeocodeJson = jsonparse.getJSONObject("regeocode")
        if(regeocodeJson==null || regeocodeJson.keySet().isEmpty) return ""

        val addressComponentJson: JSONObject = regeocodeJson.getJSONObject("addressComponent")
        if(addressComponentJson==null || addressComponentJson.keySet().isEmpty) return ""

        val businessAreasArray = addressComponentJson.getJSONArray("businessAreas")
        if(businessAreasArray == null || businessAreasArray.isEmpty) return null

        // 创建集合 保存数据
        val buffer = collection.mutable.ListBuffer[String]()

        // 循环输出
        for(item <- businessAreasArray.toArray){
            if(item.isInstanceOf[JSONObject]){
                val json = item.asInstanceOf[JSONObject]
                buffer.append(json.getString("name"))
            }
        }
        buffer.mkString(",")
    }
}
