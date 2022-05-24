package com.jlpay.kotlindemo.jetpack_study.mvvm2

/**
 * Model ：
 *
 * 因为有了Repository的概念，所以这里的Model的定义相对简单，就是JavaBean，即网络请求对应的实体数据
 */
class Data<T>(var data: T?, var errorMsg: String?) {

}