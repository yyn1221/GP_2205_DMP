package com.test

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import org.apache.commons.lang3.StringUtils
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
/**
 *@description: 
 *@author: cz
 *@time: 2019/8/24 17:58
 */
object JSONUtils2 {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName(this.getClass.getName).setMaster("local[*]")
    val sc = new SparkContext(conf)
    val sQLContext = new SQLContext(sc)
    val list:List[String] =List[String]()
    //读取数据
    val log: RDD[String] = sc.textFile("H://千峰项目/json.txt")

       log.map(row => {
          val strings: List[String] = str2JSON(row)
          list ::: strings
      })
              .map(x => x.map((_, 1)))
              .collect()
              .flatten
              .filter(!_._1.equals("[]"))
              .groupBy(_._1)
              .mapValues(_.size)
            .foreach(println)

   //val test ="{\"status\":\"1\",\"regeocode\":{\"roads\":[{\"id\":\"010J50F00201936438\",\"location\":\"116.322,39.8933\",\"direction\":\"北\",\"name\":\"广莲路\",\"distance\":\"178.386\"},{\"id\":\"010J50F0020193516\",\"location\":\"116.322,39.8969\",\"direction\":\"南\",\"name\":\"莲花池东路辅路\",\"distance\":\"218.014\"},{\"id\":\"010J50F0020192907\",\"location\":\"116.322,39.897\",\"direction\":\"南\",\"name\":\"莲花池东路\",\"distance\":\"234.951\"}],\"roadinters\":[{\"second_name\":\"羊坊店路\",\"first_id\":\"010J50F0020193516\",\"second_id\":\"010J50F0020193710\",\"location\":\"116.3211464,39.89725639\",\"distance\":\"272.217\",\"first_name\":\"莲花池东路辅路\",\"direction\":\"南\"}],\"formatted_address\":\"北京市丰台区太平桥街道北京西站\",\"addressComponent\":{\"city\":[],\"province\":\"北京市\",\"adcode\":\"110106\",\"district\":\"丰台区\",\"towncode\":\"110106002000\",\"streetNumber\":{\"number\":\"118号\",\"location\":\"116.321344,39.895065\",\"direction\":\"西\",\"distance\":\"63.1605\",\"street\":\"莲花池东路\"},\"country\":\"中国\",\"township\":\"太平桥街道\",\"businessAreas\":[{\"location\":\"116.328253,39.873074\",\"name\":\"太平桥\",\"id\":\"110106\"},{\"location\":\"116.343174,39.882633\",\"name\":\"广安门\",\"id\":\"110102\"},{\"location\":\"116.305696,39.904917\",\"name\":\"公主坟\",\"id\":\"110108\"}],\"building\":{\"name\":\"北京西站\",\"type\":\"交通设施服务;火车站;火车站\"},\"neighborhood\":{\"name\":[],\"type\":[]},\"citycode\":\"010\"},\"aois\":[{\"area\":\"280124.080323\",\"type\":\"150200\",\"id\":\"B000A83M61\",\"location\":\"116.322056,39.89491\",\"adcode\":\"110106\",\"name\":\"北京西站\",\"distance\":\"0\"}],\"pois\":[{\"id\":\"B000A83M61\",\"direction\":\"Center\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号\",\"poiweight\":\"0.903652\",\"name\":\"北京西站\",\"location\":\"116.322056,39.89491\",\"distance\":\"0\",\"tel\":\"010-51824233\",\"type\":\"交通设施服务;火车站;火车站\"},{\"id\":\"BV10000102\",\"direction\":\"西\",\"businessarea\":\"太平桥\",\"address\":\"7号线9号线\",\"poiweight\":\"0.643801\",\"name\":\"北京西站(地铁站)\",\"location\":\"116.321262,39.894763\",\"distance\":\"69.697\",\"tel\":[],\"type\":\"交通设施服务;地铁站;地铁站\"},{\"id\":\"B000A7QRY7\",\"direction\":\"东北\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路116-1号瑞尔威写字楼1层\",\"poiweight\":\"0.6\",\"name\":\"中国邮政储蓄银行(西站主楼东邮政所)(装修中)\",\"location\":\"116.322965,39.896078\",\"distance\":\"151.273\",\"tel\":\"95580\",\"type\":\"金融保险服务;银行;中国邮政储蓄银行\"},{\"id\":\"B000A7IGUN\",\"direction\":\"东北\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F1层\",\"poiweight\":\"0.338284\",\"name\":\"瑞尔威写字楼B座(北京西站)\",\"location\":\"116.322655,39.896093\",\"distance\":\"141.129\",\"tel\":[],\"type\":\"商务住宅;楼宇;商务写字楼\"},{\"id\":\"B0FFHJZVHP\",\"direction\":\"西南\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站B2层\",\"poiweight\":\"0.270605\",\"name\":\"公安(北京西站)\",\"location\":\"116.321548,39.893860\",\"distance\":\"124.546\",\"tel\":[],\"type\":\"政府机构及社会团体;公检法机构;公安警察\"},{\"id\":\"B0FFGPMHV0\",\"direction\":\"西北\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F2层\",\"poiweight\":\"0.133055\",\"name\":\"北京西站(北2进站口)\",\"location\":\"116.321478,39.89592\",\"distance\":\"122.663\",\"tel\":[],\"type\":\"交通设施服务;火车站;进站口\\/检票口\"},{\"id\":\"B0FFF5AQHQ\",\"direction\":\"北\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路31号附近\",\"poiweight\":\"0.133028\",\"name\":\"行车公寓段京西西公寓\",\"location\":\"116.321690,39.895958\",\"distance\":\"120.657\",\"tel\":[],\"type\":\"商务住宅;住宅区;住宅小区\"},{\"id\":\"B0FFFDRN3N\",\"direction\":\"南\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F1层\",\"poiweight\":\"0.352057\",\"name\":\"京铁大酒店(北京西站南广场店)\",\"location\":\"116.322401,39.893694\",\"distance\":\"138.398\",\"tel\":\"010-63955511\",\"type\":\"住宿服务;宾馆酒店;三星级宾馆\"},{\"id\":\"B0FFFVAVDW\",\"direction\":\"西北\",\"businessarea\":\"太平桥\",\"address\":\"莲花东路118号北京西站候车大厅内2层第7营业厅及2夹层投影\",\"poiweight\":\"0.317439\",\"name\":\"麦当劳(北京西站)\",\"location\":\"116.321461,39.895522\",\"distance\":\"84.8962\",\"tel\":\"010-51933235;010-63442310\",\"type\":\"餐饮服务;快餐厅;麦当劳\"},{\"id\":\"B0FFFEC6SV\",\"direction\":\"西南\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F2层9-S2\",\"poiweight\":\"0.210012\",\"name\":\"北京同仁堂阿胶坊\",\"location\":\"116.321550,39.894343\",\"distance\":\"76.4134\",\"tel\":[],\"type\":\"医疗保健服务;医药保健销售店;医疗保健用品\"},{\"id\":\"B0FFHJZVKS\",\"direction\":\"西\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号(近羊坊店路)2层T-31、3层\",\"poiweight\":\"0.354284\",\"name\":\"肯德基(北京西站)\",\"location\":\"116.321061,39.895091\",\"distance\":\"87.2447\",\"tel\":\"4009200715\",\"type\":\"餐饮服务;快餐厅;肯德基\"},{\"id\":\"B0FFFCVQCM\",\"direction\":\"西\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站通廊7-1号\",\"poiweight\":\"0.258835\",\"name\":\"星巴克咖啡(北京西站)\",\"location\":\"116.321458,39.895112\",\"distance\":\"55.7462\",\"tel\":\"010-51931281\",\"type\":\"餐饮服务;咖啡厅;星巴克咖啡\"},{\"id\":\"B000A9R4Z3\",\"direction\":\"西北\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号\",\"poiweight\":\"0.389562\",\"name\":\"北京西站北广场\",\"location\":\"116.321206,39.896492\",\"distance\":\"190.273\",\"tel\":[],\"type\":\"风景名胜;公园广场;城市广场\"},{\"id\":\"B000A87L8P\",\"direction\":\"西\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站2层TB19、3层\",\"poiweight\":\"0.3685\",\"name\":\"永和大王(北京西站)\",\"location\":\"116.321441,39.894721\",\"distance\":\"56.517\",\"tel\":\"010-51931209\",\"type\":\"餐饮服务;快餐厅;快餐厅\"},{\"id\":\"B0FFIVR5IR\",\"direction\":\"北\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F2层\",\"poiweight\":\"0.185611\",\"name\":\"北京西站(1检票口)\",\"location\":\"116.321937,39.895613\",\"distance\":\"78.8381\",\"tel\":[],\"type\":\"交通设施服务;火车站;进站口\\/检票口\"},{\"id\":\"B0FFFDRN41\",\"direction\":\"北\",\"businessarea\":\"太平桥\",\"address\":\"北京西站B2层\",\"poiweight\":\"0.310896\",\"name\":\"北京西站退票处\",\"location\":\"116.322147,39.896023\",\"distance\":\"124.014\",\"tel\":[],\"type\":\"交通设施服务;火车站;退票\"},{\"id\":\"B0FFFWT443\",\"direction\":\"西\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站2层候车大厅\",\"poiweight\":\"0.215093\",\"name\":\"真功夫(北京西站)\",\"location\":\"116.321060,39.894729\",\"distance\":\"87.3372\",\"tel\":\"010-51933500;4006927927\",\"type\":\"餐饮服务;快餐厅;快餐厅\"},{\"id\":\"B0FFG3I0V0\",\"direction\":\"西南\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F2层\",\"poiweight\":\"0.163097\",\"name\":\"北京西站候车休息区\",\"location\":\"116.321337,39.893974\",\"distance\":\"120.832\",\"tel\":[],\"type\":\"交通设施服务;火车站;候车室\"},{\"id\":\"B000A8752B\",\"direction\":\"西北\",\"businessarea\":\"太平桥\",\"address\":\"莲花东路118号北京西站候车大厅3层\",\"poiweight\":\"0.402457\",\"name\":\"吉野家(北京西站)\",\"location\":\"116.321060,39.895492\",\"distance\":\"106.817\",\"tel\":\"010-51931286;4008197197\",\"type\":\"餐饮服务;快餐厅;吉野家\"},{\"id\":\"B0FFFDXD4B\",\"direction\":\"西南\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F2层\",\"poiweight\":\"0.522808\",\"name\":\"北京西站7候车室\",\"location\":\"116.321798,39.894801\",\"distance\":\"25.1261\",\"tel\":[],\"type\":\"交通设施服务;火车站;候车室\"},{\"id\":\"B0FFFDXD49\",\"direction\":\"西北\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F2层\",\"poiweight\":\"0.522808\",\"name\":\"北京西站5候车室\",\"location\":\"116.321814,39.895154\",\"distance\":\"34.0965\",\"tel\":[],\"type\":\"交通设施服务;火车站;候车室\"},{\"id\":\"B0FFFDXD4D\",\"direction\":\"西南\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F2层\",\"poiweight\":\"0.522808\",\"name\":\"北京西站9候车室\",\"location\":\"116.321807,39.894440\",\"distance\":\"56.4218\",\"tel\":[],\"type\":\"交通设施服务;火车站;候车室\"},{\"id\":\"B000A9ILXV\",\"direction\":\"西\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F3层\",\"poiweight\":\"0.236427\",\"name\":\"正一味(西客站店)\",\"location\":\"116.321058,39.894922\",\"distance\":\"85.1596\",\"tel\":\"010-51935009\",\"type\":\"餐饮服务;外国餐厅;韩国料理\"},{\"id\":\"B0FFFDXD47\",\"direction\":\"西北\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F2层\",\"poiweight\":\"0.522808\",\"name\":\"北京西站3候车室\",\"location\":\"116.321546,39.895451\",\"distance\":\"74.2548\",\"tel\":[],\"type\":\"交通设施服务;火车站;候车室\"},{\"id\":\"B0FFFPJFXU\",\"direction\":\"东北\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站B2层\",\"poiweight\":\"0.7\",\"name\":\"北京西站出站口2(东北)\",\"location\":\"116.322408,39.895478\",\"distance\":\"69.9409\",\"tel\":[],\"type\":\"交通设施服务;火车站;出站口\"},{\"id\":\"B0FFFDXD45\",\"direction\":\"北\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F2层\",\"poiweight\":\"0.522808\",\"name\":\"北京西站1软席候车室\",\"location\":\"116.321849,39.895570\",\"distance\":\"75.4913\",\"tel\":[],\"type\":\"交通设施服务;火车站;候车室\"},{\"id\":\"B0FFFDXD42\",\"direction\":\"南\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F2层\",\"poiweight\":\"0.522808\",\"name\":\"北京西站11候车室\",\"location\":\"116.321788,39.894156\",\"distance\":\"86.8958\",\"tel\":[],\"type\":\"交通设施服务;火车站;候车室\"},{\"id\":\"B0FFGFVR3J\",\"direction\":\"南\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F1层\",\"poiweight\":\"0.341674\",\"name\":\"世纪华联超市(北京西站)\",\"location\":\"116.322400,39.893570\",\"distance\":\"151.869\",\"tel\":\"010-51765139\",\"type\":\"购物服务;超级市场;北京华联\"},{\"id\":\"B0FFFPJFXP\",\"direction\":\"西北\",\"businessarea\":\"太平桥\",\"address\":\"莲花东路118号北京西站\",\"poiweight\":\"0.204283\",\"name\":\"北京西站出站口1(西北)\",\"location\":\"116.320873,39.895457\",\"distance\":\"117.841\",\"tel\":[],\"type\":\"交通设施服务;火车站;火车站\"},{\"id\":\"B0FFFDXD48\",\"direction\":\"西\",\"businessarea\":\"太平桥\",\"address\":\"莲花池东路118号北京西站F2层\",\"poiweight\":\"0.522808\",\"name\":\"北京西站4候车室\",\"location\":\"116.320713,39.895159\",\"distance\":\"117.878\",\"tel\":[],\"type\":\"交通设施服务;火车站;候车室\"}]},\"info\":\"OK\",\"infocode\":\"10000\"}"
  //我要返回的list值
    //println(str2JSON(test))

  }
  def str2JSON(str:String):List[String]={
    //初始化
    val arr1: JSONObject = JSON.parseObject(str)
    var list:List[String] = List[String]()
    //一级JSON使用
    //arr1.getxxxx
    //获取JSON整体对象中的某一个
    val array: JSONObject = arr1.getJSONObject( "regeocode")
    //获取整体对象中的其中一个
    val array2: JSONArray = array.getJSONArray("pois")
    //数组需要转化为Array
    for(item<-array2.toArray()){
      val str: String = item.asInstanceOf[JSONObject].getString("businessarea")
      list:+=str
    }
    //return
    list
  }
}
