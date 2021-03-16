package com.jlpay.kotlindemo.ui.base

import androidx.lifecycle.LifecycleOwner
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment


abstract class BaseFragment : RxFragment() {

    open fun <T> getActivityLifecycleProvider(): LifecycleTransformer<T> {
//        return bindToLifecycle() //可以绑定Fragment生命周期
        return bindUntilEvent(FragmentEvent.DESTROY_VIEW);//可以绑定Fragment生命周期
    }

    fun getLifecycleOwner(): LifecycleOwner {
        return this
    }
}