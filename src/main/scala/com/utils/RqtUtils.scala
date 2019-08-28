package com.utils

object RqtUtils {

  //此方法处理请求数
  def request(requestmode:Int,processnode:Int): List[Double] ={

    var orgin = 0
    var effect = 0
    var adeffect = 0
    if(requestmode==1 &&processnode>= 1 ){
      orgin+=1
    }
    if(requestmode==1 &&processnode>= 2 ){
      effect+=1
    }
    if(requestmode==1 &&processnode== 3 ){
      adeffect+=1
    }
    List(orgin,effect,adeffect)
  }

  //此方法处理展示点击数
  def click(requestmode:Int,iseffective:Int): List[Double]  ={

    var adshow = 0
    var adclick = 0
    var clickrate:Double = 0.0
    if(requestmode==2 && iseffective==1){
      adshow+=1
    }
    if(requestmode==3 && iseffective==1){
      adclick+=1
    }
    if(adshow!=0){
      clickrate = adclick/adshow
    }else{
      clickrate=0.0
    }
    List(adshow,adclick,clickrate)
  }

  //此方法处理竞价操作
  def Ad(iseffective: Int, isbilling: Int, isbid: Int, iswin: Int, adorderid:Int,
         winprice:Double, adpayment: Double): List[Double] = {
    var bid = 0
    var bidsucc = 0
    var consume = 0.0
    var cost = 0.0
    var bidsuccrate = 0.0
    if(iseffective==1 && isbilling==1 && isbid==1 ){
      bid+=1
    }
    if(iseffective==1 && isbilling==1 && iswin==1 &&adorderid != 0 ){
      bidsucc+=1
    }
    if(iseffective==1 && isbilling==1 && iswin==1 ){
      consume+=winprice
    }
    if(iseffective==1 && isbilling==1 && iswin==1 ){
      cost+=adpayment
    }
    if(bid!=0){
      bidsuccrate = bidsucc/bid
    }else{
      bidsuccrate=0
    }
    List(bid,bidsucc,bidsuccrate,consume/1000,cost/1000)

  }

}
