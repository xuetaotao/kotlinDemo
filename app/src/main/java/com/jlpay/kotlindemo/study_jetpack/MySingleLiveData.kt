package com.jlpay.kotlindemo.study_jetpack

object MySingleLiveData {

    val info1: SingleLiveData<String> by lazy { SingleLiveData() }
}