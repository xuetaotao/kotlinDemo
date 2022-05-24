package com.jlpay.kotlindemo.jetpack_study.mvvm3

/**
 * 密封类：sealed
 * https://blog.csdn.net/alfredkao/article/details/107592173
 *
 * 密封(Sealed)类是一个限制类层次结构的类
 * 当对象具有来自有限集的类型之一，但不能具有任何其他类型时，使用密封类
 * 密封类的构造函数在默认情况下是私有的，它也不能允许声明为非私有
 */
sealed class LoadStateBean(val msg: String) {
    class Loading(msg: String = "") : LoadStateBean(msg)
    class Success(msg: String = "") : LoadStateBean(msg)
    class Fail(msg: String) : LoadStateBean(msg)
}