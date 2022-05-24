package com.jlpay.kotlindemo.jetpack_study

import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


/**
 * 解决LiveData粘性事件
 * Created on 2020-12-28.
 *
 * 泛型：  Java的 ? extends T  对应  Kotlin的 out T， 只可以被写入而不可以被读取
 * 泛型：  Java的 ? super T  对应  Kotlin的 in T， 只可以被读取
 * <p>
 * 如何理解Kotlin泛型中的in和out？
 * https://blog.csdn.net/weixin_38261570/article/details/112637337
 */
class SingleLiveDataEvent<T> : MutableLiveData<T>() {

    private val mPending: AtomicBoolean = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(@Nullable t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        setValue(null)
    }
}