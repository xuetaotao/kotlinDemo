package com.jlpay.kotlindemo.jetpack_study.mvvm3

data class BaseResponse<T>(
    val errorCode: Int = 0,
    val errorMsg: String? = null,
    var data: T? = null
)