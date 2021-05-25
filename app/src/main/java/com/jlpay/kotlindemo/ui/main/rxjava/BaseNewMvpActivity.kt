package com.jlpay.kotlindemo.ui.main.rxjava

import android.os.Bundle

abstract class BaseNewMvpActivity<P : IPresenter> : BaseNewActivity() {

    lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = createPresenter()
        lifecycle.addObserver(presenter)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
//        presenter.unSubscribe()
        super.onDestroy()
    }

    abstract fun createPresenter(): P
}