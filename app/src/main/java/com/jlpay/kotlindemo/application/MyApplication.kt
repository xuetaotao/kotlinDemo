package com.jlpay.kotlindemo.application

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.multidex.MultiDex
import com.jlpay.kotlindemo.BuildConfig
import com.jlpay.kotlindemo.net.ExceptionHandle
import com.jlpay.kotlindemo.base.Constants
import com.jlpay.kotlindemo.utils.AppUtils
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.tinker.entry.DefaultApplicationLike
import java.util.concurrent.TimeoutException

/**
 * 自定义ApplicationLike
 */
class MyApplication(
    application: Application,
    tinkerFlags: Int,
    tinkerLoadVerifyFlag: Boolean,
    applicationStartElapsedTime: Long,
    applicationStartMillisTime: Long,
    tinkerResultIntent: Intent,
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

        fixTimeoutException()//捕获Android异常
        ExceptionHandle.rxjavaExceptionCapture()//捕获Rxjava的异常

        initLibNeedAgree()
    }

    /**
     * 此外：还可以用来捕获异常，很好用
     *
     * 解决FinalizerWatchdogDaemon产生的TimeoutException问题
     * 原因：FinalizerWatchdogDaemon析构监护守护线程，检测到超时后FinalizerDaemon析构守护线程中仍有正在执行的finalize()对象，视为finalize()执行超时，产生异常
     * 解决方案：设置自定义UncaughtExceptionHandler处理该情况
     * 参考：https://segmentfault.com/a/1190000019373275
     */
    private fun fixTimeoutException() {
        val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            //根据Bugly反应情况，暂时先只对OPPO机型处理
            if (thread.name == "FinalizerWatchdogDaemon" && throwable is TimeoutException &&
                Build.BRAND.equals("OPPO", ignoreCase = true)
            ) {
                //ignore it
            } else {
                defaultUncaughtExceptionHandler?.uncaughtException(thread, throwable)
            }
        }
    }

    /**
     * 需要同意隐私协议后初始化的第三方库
     */
    private fun initLibNeedAgree() {
        initBuglyHotFix()
        initBugly()
        //卡顿监控分析 (暂时屏蔽，影响 assembleRelease 命令执行，而且实际也未生效，原因待查询)
//        BlockCanary.install(mContext, BlockCanaryContext()).start()
    }


    /**
     * 热更新相关初始化
     */
    private fun initBuglyHotFix() {
        setStrictMode()
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = true
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId。调试时，将第三个参数改为true
        Bugly.init(application, Constants.BUGLY_APP_ID, false)
    }

    private fun initBugly() {
        val sceneTag = if (BuildConfig.DEBUG) {
            Constants.BUGLY_DEBUG_TAG
        } else {
            Constants.BUGLY_RELEASE_TAG
        }
        CrashReport.setUserSceneTag(mContext, sceneTag)
    }


    @TargetApi(9)
    protected fun setStrictMode() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        StrictMode.setVmPolicy(VmPolicy.Builder().detectAll().penaltyLog().build())
    }

    //程序终止的时候执行
    override fun onTerminate() {
        super.onTerminate()
        Beta.unInit()
    }
}