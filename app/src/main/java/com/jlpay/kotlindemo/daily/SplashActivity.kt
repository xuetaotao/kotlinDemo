package com.jlpay.kotlindemo.daily

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.utils.Utils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //如果把布局页面中的ImageView注释掉，只保留AppTheme.Launcher.Picture主题中的android:windowBackground
        //那么会启动App的时候会顿一下，显示两次android:windowBackground中的图片
        //如果保留布局页面中的ImageView和AppTheme.Launcher.Picture主题中的android:windowBackground的图片
        //那么启动的时候就会先显示AppTheme.Launcher.Picture主题中的android:windowBackground的图片，
        //后显示布局页面中的ImageView
        setContentView(R.layout.activity_splash)
        //设置沉浸式状态栏
//        Utils.setStatusBarTransparent(this)

        //可以去掉延时，这样就会不显示SplashActivity页面直接跳转
        Handler().postDelayed({
            Utils.launchActivity(this, MainActivity::class.java)
            finish()
        }, 1000)
    }
}