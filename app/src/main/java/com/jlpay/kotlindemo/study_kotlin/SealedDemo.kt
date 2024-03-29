package com.jlpay.kotlindemo.study_kotlin

/**
 * 密封类
 *
 * https://blog.csdn.net/alfredkao/article/details/107592173
 *
 * 密封(Sealed)类是一个限制类层次结构的类
 * 当对象具有来自有限集的类型之一，但不能具有任何其他类型时，使用密封类
 * 密封类的构造函数在默认情况下是私有的，它也不能允许声明为非私有
 */
sealed class SealedDemo(msg: Int) {
    object StartDemo : SealedDemo(1)
    data class AutoClick(val time: Int) : SealedDemo(2)
    class Exit : SealedDemo(3)
}