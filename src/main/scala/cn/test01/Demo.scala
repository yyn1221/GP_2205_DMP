package com.test01

object Demo {
    def main(args: Array[String]): Unit = {
        val list: List[(Char, Int)] = List(('a',1),('b',1),('c',1))
        val list1: List[(Char, Int)] = List(('d',1),('e',1),('f',1))
        println(list.zip(list1))
        println(list.union(list1))
    }
}
