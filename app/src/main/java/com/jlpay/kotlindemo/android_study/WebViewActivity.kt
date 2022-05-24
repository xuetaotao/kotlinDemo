package com.jlpay.kotlindemo.android_study

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

/**
 * TODO 有空再写
 */
class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        //webview初始化
        val webview = findViewById<WebView>(R.id.webview)


        val intent = intent

    }
}