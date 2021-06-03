package com.jlpay.kotlindemo.ui.main.rxjava

import android.os.Bundle

abstract class BaseNewMvpActivity<P : IPresenter> : BaseNewActivity() {

    lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = createPresenter()
        //添加这一行，如此每当Activity发生了对应的生命周期改变，Presenter就会执行对应事件注解的方法
        lifecycle.addObserver(presenter)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
//        presenter.unSubscribe()
        super.onDestroy()
    }

    abstract fun createPresenter(): P
}