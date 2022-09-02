package com.example.mvvm.ui.activity

import android.content.Intent
import com.example.mvvm.base.BaseActivity
import com.example.mvvm.ui.activity.login.LoginActivity
import com.example.mvvm.webView.WebViewActivity

class SplashActivity : BaseActivity() {

    override fun getLayoutId(): Int = 0

    override fun initData() {
//        startActivity(Intent(this, MainActivity::class.java))
        startActivity(Intent(this, LoginActivity::class.java))
//        WebViewActivity.start(this, "http://static.jlpay.com/agent/agreement/index.html")
        finish()
    }

    override fun initView() {
    }

    override fun startHttp() {
    }

}