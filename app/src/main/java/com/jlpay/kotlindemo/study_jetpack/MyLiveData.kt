package com.jlpay.kotlindemo.study_jetpack

import androidx.lifecycle.MutableLiveData

object MyLiveData {//单例类

    //懒加载
    val info1: MutableLiveData<String> by lazy { MutableLiveData() }
}