package com.jlpay.kotlindemo.jetpack_study.mvvm3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * TODO 待学习理解，Kotlin协程
 */
class WanViewModel : ViewModel() {

    val user: MutableLiveData<BaseResponse<UserBean>> = MutableLiveData<BaseResponse<UserBean>>()
    val loadState: MutableLiveData<LoadStateBean> = MutableLiveData()

    /**
     * async await方法：
     * 用async方法包裹了suspend方法来执行并发请求，并发结果都返回之后，切换到主线程，接着再用await方法来获取并发请求结果
     */
    fun login(username: String, password: String) {
        launch({
            loadState.value = LoadStateBean.Loading()
            val result = async { WanRepository.getWanRepository().loginForm(username, password) }
            user.value = result.await()
            loadState.value = LoadStateBean.Success()
        }, {
            loadState.value = LoadStateBean.Fail(it.message ?: "登陆失败")
        })
    }


    /**
     * 定义 ViewModel 的扩展
     *
     * launch方法解释：
     * context：协程上下文，可以指定协程运行的线程。默认与指定的CoroutineScope中的coroutineContext保持一致，比如GlobalScope默认运行在一个后台工作线程内。
     * 也可以通过显示指定参数来更改协程运行的线程，Dispatchers提供了几个值可以指定：Dispatchers.Default、Dispatchers.Main、Dispatchers.IO、Dispatchers.Unconfined。
     *
     * start：协程的启动模式。默认的CoroutineStart.DEFAULT是指协程立即执行，除此之外还有CoroutineStart.LAZY、CoroutineStart.ATOMIC、CoroutineStart.UNDISPATCHED。
     *
     * block：协程主体。也就是要在协程内部运行的代码，可以通过lamda表达式的方式方便的编写协程内运行的代码。
     *
     * CoroutineExceptionHandler：指定CoroutineExceptionHandler来处理协程内部的异常。
     *
     * Job：返回值，对当前创建的协程的引用。可以通过Job的start、cancel、join等方法来控制协程的启动和取消。
     *
     */
    fun ViewModel.launch(
        block: suspend CoroutineScope.() -> Unit,
        onError: (e: Throwable) -> Unit = {},
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch(CoroutineExceptionHandler { _, e -> onError(e) }) {
            try {
                block.invoke(this)
            } finally {
                onComplete()
            }
        }
    }
}