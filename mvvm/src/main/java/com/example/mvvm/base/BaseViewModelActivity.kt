package com.example.mvvm.base

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm.utils.toast
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import retrofit2.HttpException

/**
 * Kotlin：内置函数let、also、with、run、apply:
 * https://cloud.tencent.com/developer/article/1591238
 */
abstract class BaseViewModelActivity<VM : BaseViewModel> : BaseActivity() {

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        initVM()
        super.onCreate(savedInstanceState)
        startObserve()
        startHttp()
    }

    private fun initVM() {
        //let: 方便了统一判空的处理 & 确定了mVar变量的作用域
        providerVMClass().let {
            viewModel = ViewModelProvider(this).get(it)
            lifecycle.addObserver(viewModel)
        }
    }

    //viewModel实例
    abstract fun providerVMClass(): Class<VM>

    private fun startObserve() {
        //处理一些通用异常，比如网络超时等
        //run:结合了let、with两个函数的作用，即：1.调用同一个对象的多个方法 / 属性时，可以省去对象名重复，直接调用方法名 / 属性即可(with);
        //2.定义一个变量在特定作用域内;3.统一做判空处理(let)
        //4.函数返回最后一行的值 / 表达式
        viewModel.run {
            getError().observe(this@BaseViewModelActivity,
                Observer<Exception> {
                    hideLoading()
                    hideSearchLoading()
                    requestError(it)
                })
            getFinally().observe(this@BaseViewModelActivity,
                Observer<Int> {
                    requestFinally(it)
                })
        }
    }

    //接口请求完毕，子类可以重写此方法做一些操作
    open fun requestFinally(it: Int?) {

    }

    //接口请求出错，子类可以重写此方法做一些操作
    open fun requestError(it: Exception?) {
        //处理一些已知异常
        it?.run {
            when (it) {
                is CancellationException -> {
                    Log.d("${TAG}--->接口请求取消", it.message.toString())
                }
                is TimeoutCancellationException -> toast("请求超时")
                is BaseRepository.TokenInvalidException -> toast("登陆超时")
                is HttpException -> {
                    if (it.code() == 504) {
                        toast("无法连接服务器,请检查网络设置")
                    } else {
                        toast(it.message().toString())
                    }
                }
                else -> toast(it.message.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}