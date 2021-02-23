package com.jlpay.kotlindemo.ui.utils

import android.content.Context
import com.jlpay.kotlindemo.ui.base.Constants

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

    /**
     * 创建APP存储目录结构
     */
    private fun initFileDir() {
        FileUtils.makeDirs(Constants.AUDIO_SAVE_DIR)
        FileUtils.makeDirs(Constants.FILE_SAVE_DIR)
        FileUtils.makeDirs(Constants.IMAGE_SAVE_DIR)
        FileUtils.makeDirs(Constants.VIDEO_SAVE_DIR)
    }

}