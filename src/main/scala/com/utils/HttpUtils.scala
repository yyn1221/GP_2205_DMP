package com.utils

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

/**
  * http请求协议
  */
object HttpUtils {
    //get
    def  get(url:String):String={
        val client = HttpClients.createDefault()
        val get = new HttpGet(url)

        //发送请求
        val response: CloseableHttpResponse = client.execute(get)
        //返回结果获取
        EntityUtils.toString(response.getEntity,"UTF-8")


    }
}
