package com.jlpay.kotlindemo.study_jetpack.mvp

/**
 * 也可以是接口
 * 控制，负责把具体事件做一个分发，通过Model拿到数据，通过View进行显示。负责数据处理以及View和Model的交互等，
 * 持有Model和View的引用。
 * 另外，需要监听Activity的生命周期，避免Activity销毁了还回调
 */
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