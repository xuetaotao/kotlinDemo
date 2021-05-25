package com.jlpay.kotlindemo.ui.main.rxjava

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.AutoDisposeConverter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

abstract class BaseNewPresenter :
    IPresenter {

    private lateinit var lifecycleOwner: LifecycleOwner

    override fun onCreate(owner: LifecycleOwner) {
        //doOnDispose：当调用 Disposable 的 dispose() 之后回调该方法
        Log.e("LifecycleOwner", "=======" + "onCreate：" + "=========")
        this.lifecycleOwner = owner
    }

    override fun onDestroy(owner: LifecycleOwner) {

    }

    override fun onLifecycleChanged(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

//    override fun subscribe() {
//
//    }
//
//    override fun unSubscribe() {
//
//    }

    fun <T> bindLifecycle(): AutoDisposeConverter<T>? {
        if (null == lifecycleOwner) {
            throw NullPointerException("lifecycleOwner == null")
        }
        return AutoDispose.autoDisposable<T>(AndroidLifecycleScopeProvider.from(lifecycleOwner))

//        return AutoDispose.autoDisposable<T>(
//            AndroidLifecycleScopeProvider.from(
//                lifecycleOwner,
//                Lifecycle.Event.ON_DESTROY
//            )
//        )
    }
}