package com.example.mvvm.db.repository

import android.widget.Toast
import com.example.mvvm.base.BaseApplication
import kotlinx.coroutines.*

/**
 * Kotlin by 关键字详解
 * Kotlin 中 by 就是用于实现委托的
 * https://blog.csdn.net/github_39465103/article/details/90238042
 *
 * Kotlin inline, noinline and crossinline
 * https://www.jianshu.com/p/cd0be9b887ec
 */
open class BaseDBRepository : CoroutineScope by MainScope() {

    fun <T> execute(
        runnable: suspend () -> T,
        success: ((t: T) -> Unit)? = null,
        error: ((e: Throwable) -> Unit)? = null
    ) {
        launch(CoroutineExceptionHandler { _, _ -> }) {
            val result: T = withContext(Dispatchers.IO) {
                runnable()
            }
            success?.invoke(result)

        }.invokeOnCompletion {
            if (it != null) {
                Toast.makeText(
                    BaseApplication.mContext,
                    it.message ?: "本地数据库异常",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            //需要自行处理error的话可以传入error参数
            it?.let {
                error?.invoke(it)
            }
        }
    }
}