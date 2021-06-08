package com.jlpay.kotlindemo.ui.main.jetpack.mvvm.learn2

data class BaseResponse<T>(
    val errorCode: Int = 0,
    val errorMsg: String? = null,
    var data: T? = null
)