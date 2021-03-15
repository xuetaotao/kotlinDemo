package com.jlpay.kotlindemo.ui.base

import android.os.Bundle

abstract class BaseMvpActivity<P : BasePresenter> : BaseActivity() {

    lateinit var mPresenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        mPresenter = createPresenter()
        super.onCreate(savedInstanceState)
    }


    override fun onDestroy() {
        mPresenter.unSubscribe()
        super.onDestroy()
    }

    abstract fun createPresenter(): P
}