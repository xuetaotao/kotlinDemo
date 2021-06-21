package com.jlpay.eyepetizer.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.github.moduth.blockcanary.BlockCanary
import com.github.moduth.blockcanary.BlockCanaryContext

class EyepetizerApplication : Application() {

    companion object {
        lateinit var mContext: Context
    }

    init {

    }

    override fun onCreate() {
        super.onCreate()
        mContext = this

        initLib()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initLib() {
        //卡顿监控分析
        BlockCanary.install(mContext, BlockCanaryContext()).start()
    }
}