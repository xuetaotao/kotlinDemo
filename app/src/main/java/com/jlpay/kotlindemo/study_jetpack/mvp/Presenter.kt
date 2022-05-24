package com.jlpay.kotlindemo.study_jetpack.mvp

//也可以是接口
class Presenter(private val iView: IView) {

    fun init() {
        val data = DataCenter.getData()
        iView.showData(data)
    }

    //面向接口编程
    interface IView {
        fun showData(data: List<String>)
    }
}