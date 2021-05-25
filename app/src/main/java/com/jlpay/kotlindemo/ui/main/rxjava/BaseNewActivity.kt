package com.jlpay.kotlindemo.ui.main.rxjava

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.AutoDisposeConverter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

abstract class BaseNewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getResourceId())

        initView()
        initData()
    }


    abstract fun getResourceId(): Int
    abstract fun initView()
    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
    }

    fun <T> bindLifecycle(): AutoDisposeConverter<T>? {
        return AutoDispose.autoDisposable<T>(AndroidLifecycleScopeProvider.from(lifecycle))

//        return AutoDispose.autoDisposable<T>(
//            AndroidLifecycleScopeProvider.from(
//                lifecycleOwner,
//                Lifecycle.Event.ON_DESTROY
//            )
//        )
    }
}