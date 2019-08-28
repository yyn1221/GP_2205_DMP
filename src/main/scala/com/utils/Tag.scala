package com.utils

trait Tag {

    /**
      * 打标签的统一结构
      */
    //List[标签k, 标签v]
    def makeTags(args:Any*):List[(String,Int)]

}
