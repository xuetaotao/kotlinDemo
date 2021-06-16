package com.example.mvvm.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

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
        viewModel.run {
            //TODO
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}