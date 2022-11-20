package com.jlpay.kotlindemo.study_jetpack.mvvm6

data class ResponseData<T>(
    val errorCode: Int = 0,
    val errorMsg: String? = null,
    var data: T? = null
)