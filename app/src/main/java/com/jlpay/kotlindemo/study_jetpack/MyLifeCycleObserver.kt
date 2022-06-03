package com.jlpay.kotlindemo.study_jetpack

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * Marks a class as a LifecycleObserver
 * @see Lifecycle Lifecycle - for samples and usage patterns.
 * {@link androidx.lifecycle.Lifecycle}开头的注释中有LifecycleObserver用法说明
 * 添加这个引用后发现注释已经变了，如果要看的话可以注释掉试试，也可以不看了，直接用官方新推荐的方式做
 * implementation 'androidx.lifecycle:lifecycle-common-java8:2.4.1'
 *
 * 在Activity/Fragment中通过addObserver方法注册
 * 目前官方更推荐使用继承DefaultLifecycleObserver 的方式，
 * 也就是 {@link com.jlpay.kotlindemo.study_jetpack.MyLifeCycleObserver2}
 */
class MyLifeCycleObserver : LifecycleObserver {

    private val TAG = this::class.java.simpleName

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onMyCreate(lifecycleOwner: LifecycleOwner) {
        Log.e(TAG, "onMyCreate: ")
    }

    //Observer methods can receive zero or one argument.
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    fun onMyStart(lifecycleOwner: LifecycleOwner) {
    fun onMyStart() {
        Log.e(TAG, "onMyStart: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onMyResume(lifecycleOwner: LifecycleOwner) {
        Log.e(TAG, "onMyResume: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onMyPause(lifecycleOwner: LifecycleOwner) {
        Log.e(TAG, "onMyPause: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    fun onMyStop(lifecycleOwner: LifecycleOwner) {
    fun onMyStop() {
        Log.e(TAG, "onMyStop: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onMyDestroy(lifecycleOwner: LifecycleOwner) {
        Log.e(TAG, "onMyDestroy: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onMyAnyEvent(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {
        Log.e(TAG, "onMyAnyEvent: $event")
    }
}