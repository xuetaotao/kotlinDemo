package com.jlpay.kotlindemo.library_study.rxjava

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

    fun <T> bindLifecycle(): AutoDisposeConverter<T> {
        return AutoDispose.autoDisposable<T>(AndroidLifecycleScopeProvider.from(lifecycleOwner))

//        return AutoDispose.autoDisposable<T>(
//            AndroidLifecycleScopeProvider.from(
//                lifecycleOwner,
//                Lifecycle.Event.ON_DESTROY
//            )
//        )
    }
}