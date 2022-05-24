package com.jlpay.kotlindemo.utils

import android.text.TextUtils
import java.io.File

object FileUtils {

    /**
     * 创建目录
     */
    @JvmStatic
    fun makeDirs(dir: String): Boolean {
        if (TextUtils.isEmpty(dir)) {
            return false
        }
        val file = File(dir)
        if (!file.exists()) {
            return file.mkdirs()
        }
        return true
    }
}