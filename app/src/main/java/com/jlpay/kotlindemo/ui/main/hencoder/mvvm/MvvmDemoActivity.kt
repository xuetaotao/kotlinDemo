package com.jlpay.kotlindemo.ui.main.hencoder.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

//MVVM 中的ViewModel ：
//MVVM 中的Model ：
//MVVM 中的View ：
class MvvmDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvvm_demo)

        ViewModel(findViewById(R.id.et_name), findViewById(R.id.et_password)).init()
    }
}