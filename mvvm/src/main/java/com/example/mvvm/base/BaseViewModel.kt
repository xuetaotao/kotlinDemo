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
 * lateinit 只用于变量 var，而 lazy 只用于常量 val
 * lazy 应用于单例模式(if-null-then-init-else-return)，而且当且仅当变量被第一次调用的时候，委托方法才会执行
 * lazy()是接受一个 lambda 并返回一个 Lazy <T> 实例的函数，返回的实例可以作为实现延迟属性的委托： 第一次调用 get() 会执行已传递给 lazy() 的 lambda 表达式并记录结果，
 * 后续调用 get() 只是返回记录的结果
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
            withTimeout(HttpConstant.DEFAULT_TIMEOUT * 1000) {
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