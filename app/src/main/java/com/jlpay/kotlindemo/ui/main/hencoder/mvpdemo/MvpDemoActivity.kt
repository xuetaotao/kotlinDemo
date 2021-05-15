package com.jlpay.kotlindemo.ui.main.hencoder.mvpdemo

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R


//MVP中的Presenter  ：Presenter，控制，负责把具体事件做一个分发，通过Model拿到数据，通过View进行显示
//MVP中的Model：DataCenter，掌管数据，负责处理数据和逻辑
//MVP中的View ：Activity，XML布局文件，掌管显示
class MvpDemoActivity : AppCompatActivity(), Presenter.IView {

    private lateinit var et_name: EditText
    private lateinit var et_password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvp_demo)

        et_name = findViewById(R.id.et_name)
        et_password = findViewById(R.id.et_password)

        Presenter(this).init()
    }


    override fun showData(data: List<String>) {
        et_name.setText(data[0])
        et_password.setText(data[1])
    }


}