package com.jlpay.kotlindemo.application

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.multidex.MultiDex
import com.jlpay.kotlindemo.ui.base.Constants
import com.jlpay.kotlindemo.ui.utils.AppUtils
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.tinker.entry.DefaultApplicationLike

/**
 * 自定义ApplicationLike
 */
class MyApplication(
    application: Application,
    tinkerFlags: Int,
    tinkerLoadVerifyFlag: Boolean,
    applicationStartElapsedTime: Long,
    applicationStartMillisTime: Long,
    tinkerResultIntent: Intent
) : DefaultApplicationLike(
    application,
    tinkerFlags,
    tinkerLoadVerifyFlag,
    applicationStartElapsedTime,
    applicationStartMillisTime,
    tinkerResultIntent
) {

    companion object {

        lateinit var mContext: Context

        /**
         * 用户同意隐私政策后初始化第三方库
         */
        @JvmStatic
        fun initLibAfterAgreePolicy() {

        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = application

        initLib()
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onBaseContextAttached(base: Context?) {
        super.onBaseContextAttached(base)
        MultiDex.install(base)
        // 安装tinker
        Beta.installTinker(this)
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    fun registerActivityLifecycleCallback(callbacks: Application.ActivityLifecycleCallbacks) {
        application.registerActivityLifecycleCallbacks(callbacks)
    }

    /**
     * 第三方库初始化
     */
    private fun initLib() {
        AppUtils.initAppUtils(mContext)

        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId。调试时，将第三个参数改为true
        Bugly.init(application, Constants.BUGLY_APP_ID, false)
    }


}