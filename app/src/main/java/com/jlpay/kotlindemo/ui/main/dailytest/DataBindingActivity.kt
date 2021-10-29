package com.jlpay.kotlindemo.ui.main.dailytest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityDatabindingBinding

/**
 * Jetpack dataBinding(数据绑定库)
 * 官网地址：https://developer.android.google.cn/topic/libraries/data-binding?hl=zh_cn
 * Android从零开始搭建MVVM架构（1）————DataBinding：https://juejin.cn/post/6844903955693043725
 *
 * TODO unfinished
 */
class DataBindingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatabindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_databinding)
        //写上这句后，上面的代码可以注释
        binding = DataBindingUtil.setContentView(this, R.layout.activity_databinding)
        binding.txt.text = "这里就能设置数据"
    }
}