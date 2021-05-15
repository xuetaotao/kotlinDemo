package com.jlpay.kotlindemo.ui.main.hencoder.mvcdemo

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

//MVC中的Controller ：Activity，负责把具体事件做一个分发
//MVC中的Model ：DataCenter，负责处理数据和逻辑
//MVC中的View ：XML布局文件
class MvcDemoActivity : AppCompatActivity() {

    private lateinit var et_name: EditText
    private lateinit var et_password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_demo)

        et_name = findViewById(R.id.et_name)
        et_password = findViewById(R.id.et_password)

        val data = DataCenter.getData()
        et_name.setText(data[0])
        et_password.setText(data[1])
    }
}