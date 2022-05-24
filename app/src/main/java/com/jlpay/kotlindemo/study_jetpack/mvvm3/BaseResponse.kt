package com.jlpay.kotlindemo.study_jetpack.mvvm3

data class BaseResponse<T>(
    val errorCode: Int = 0,
    val errorMsg: String? = null,
    var data: T? = null
)