package com.example.mvvm.base

import android.app.Application
import android.content.Context

class BaseApplication : Application() {

    companion object {
        var TAG = BaseApplication::class.java.simpleName
//        var TAG = javaClass.simpleName

        lateinit var instance: Application
        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        mContext = applicationContext
    }
}