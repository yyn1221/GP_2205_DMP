package com.Tag

import com.utils.Tag
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row

object TagsTerminal extends Tag{
    /**
      * 打标签的统一结构
      */
    override def makeTags(args: Any*): List[(String, Int)] = {
        var list = List[(String,Int)]()
        //解析参数
        val row = args(0).asInstanceOf[Row]

        val client = row.getAs[Int]("client")
        client match {
            case 1 => list:+=("android D00010001",1)
            case 2 => list:+=("ios D00010002",1)
            case 3 => list:+=("wp D00010003",1)
            case _ => list:+=("其他 D00010004",1)
        }
        //联网方式
        val netname = row.getAs[String]("networkmannername")
        netname match {
            case "WiFi" => list:+=("D00020001",1)
            case "4G " => list:+=("D00020002",1)
            case "3G " => list:+=("D00020003",1)
            case "2G " => list:+=("D00020004",1)
            case _ =>  list:+=("D00020005",1)
        }
        //运营商
        val ispname = row.getAs[String]("ispname")
        ispname match {
            case "移动" => list:+=("D00030001",1)
            case "联通" => list:+=("D00030002",1)
            case "电信" => list:+=("D00030003",1)
            case _ => list:+=("D00030004",1)
        }

        //        var oSname =""
//        if( client ==1 ){
//            oSname = "D00010001 "
//        }else if( client ==2){
//            oSname = "D00010002 "
//        }else if( client ==3){
//            oSname = "D00010003 "
//        }else{
//            oSname ="D00010004"
//        }

//        val netname = row.getAs[String]("networkmannername")
//        var nmname =""
//        netname match {
//            case "WiFi" => nmname = "D00020001 "
//            case "4G " => nmname = "D00020002"
//            case "3G " => nmname = "D00020003"
//            case "2G " => nmname = "D00020004"
//            case _ => nmname ="D00020005"
//        }

//        val ispname = row.getAs[String]("ispname")
//        var aname =""
//        ispname match {
//            case "移动" => aname = "D00030001"
//            case "联通" => aname = "D00030002"
//            case "电信" => aname = "D00030003"
//            case _ => aname ="D00030004"
//        }
//        if(StringUtils.isNotBlank(oSname) && StringUtils.isNotBlank(nmname) &&StringUtils.isNotBlank(aname)){
//            list:+=("操作系统"+oSname,1)
//            list:+=("联网方式"+nmname,1)
//            list:+=("运营商"+aname,1)
//        }
        list
    }
}
