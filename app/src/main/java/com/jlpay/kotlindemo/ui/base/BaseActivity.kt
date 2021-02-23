package com.jlpay.kotlindemo.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getResourceId())

        initView()
        initData()
    }


    abstract fun getResourceId(): Int
    abstract fun initView()
    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
    }
}