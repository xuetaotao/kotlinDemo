package com.example.mvvm.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.constant.HttpConstant
import com.example.mvvm.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

/**
 * by lazy:
 * lazy 应用于单例模式(if-null-then-init-else-return)，而且当且仅当变量被第一次调用的时候，委托方法才会执行
 * <p>
 * Kotlin lateinit 和 by lazy: https://www.jianshu.com/p/e2cb4c65d4ff
 * Kotlin 中的 by lazy: https://blog.csdn.net/qq_40990280/article/details/107601024
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {

    private val error by lazy { SingleLiveEvent<Exception>() }

    private val finally by lazy { SingleLiveEvent<Int>() }

    //运行在UI线程的协程
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        try {
            withTimeout(HttpConstant.DEFAULT_TIMEOUT) {
                block()
            }
        } catch (e: Exception) {
            //此处接收到BaseRepository里的request抛出的异常，直接赋值给error
            error.value = e
        } finally {
            finally.value = 200
        }
    }

    /**
     * 请求失败，出现异常
     */
    fun getError(): LiveData<Exception> = error

    /**
     * 请求完成，在此处做一些关闭操作
     */
    fun getFinally(): LiveData<Int> = finally
}