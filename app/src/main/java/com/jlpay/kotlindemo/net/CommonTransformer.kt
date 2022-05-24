package com.jlpay.kotlindemo.net

import com.jlpay.kotlindemo.base.BaseActivity
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object CommonTransformer {

    @JvmStatic
    fun <T> getTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    @JvmStatic
    fun <T> getLifeTransformer(baseActivity: BaseActivity): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream: Observable<T> ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(baseActivity.getActivityLifecycleProvider())
        }
    }
}