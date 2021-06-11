package com.jlpay.eyepetizer.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class EyepetizerApplication : Application() {

    companion object {
        lateinit var mContext: Context
    }

    init {

    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}