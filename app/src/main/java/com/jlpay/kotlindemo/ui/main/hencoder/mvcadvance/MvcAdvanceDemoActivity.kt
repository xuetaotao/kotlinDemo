package com.jlpay.kotlindemo.ui.main.hencoder.mvcadvance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

//MVC中的Controller ：Activity，负责把具体事件做一个分发
//MVC中的Model ：DataCenter，负责处理数据和逻辑
//MVC中的View ：XML布局文件，MvcAdvanceView
class MvcAdvanceDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_advance_demo)

        val data = DataCenter.getData()
        val mvcAdvanceView: MvcAdvanceView = findViewById(R.id.mvcAdvanceView)
        mvcAdvanceView.showData(data)
    }
}