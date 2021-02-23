package com.jlpay.kotlindemo.application

import android.app.Application
import android.content.Context
import com.jlpay.kotlindemo.ui.utils.AppUtils

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        mContext = this

        initLib()
    }

    /**
     * 第三方库初始化
     */
    private fun initLib() {
        AppUtils.initAppUtils(mContext)
    }


    companion object {

        lateinit var mContext: Context

        /**
         * 用户同意隐私政策后初始化第三方库
         */
        @JvmStatic
        fun initLibAfterAgreePolicy() {

        }
    }


}