package com.jlpay.kotlindemo.utils

import android.app.Application
import android.content.Context
import com.jlpay.kotlindemo.base.Constants

object AppUtils {

    private lateinit var mContext: Context

    //    @JvmStatic
    fun initAppUtils(context: Context) {
        mContext = context

        initFileDir()
    }

    @JvmStatic
    fun getContext(): Context {
        return mContext
    }

    @JvmStatic
    fun getApplication(): Application {
        return mContext as Application
    }

    /**
     * 创建APP存储目录结构
     */
    public fun initFileDir() {
        FileUtils.makeDirs(Constants.AUDIO_SAVE_DIR)
        FileUtils.makeDirs(Constants.FILE_SAVE_DIR)
        FileUtils.makeDirs(Constants.IMAGE_SAVE_DIR)
        FileUtils.makeDirs(Constants.VIDEO_SAVE_DIR)
    }

}