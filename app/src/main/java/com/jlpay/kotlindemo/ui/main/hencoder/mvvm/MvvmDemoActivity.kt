package com.jlpay.kotlindemo.ui.main.hencoder.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

//MVVM 中的ViewModel ：负责业务逻辑处理，负责View和Model的交互。和View层双向绑定
//MVVM 中的Model ：数据获取模块，这里的DataCenter
//MVVM 中的View ：对应于Activity/Fragment/自定义View，主要负责UI渲染
class MvvmDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvvm_demo)

        ViewModel(findViewById(R.id.et_name), findViewById(R.id.et_password)).init()
    }
}