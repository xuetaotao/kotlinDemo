package com.jlpay.kotlindemo.study_jetpack

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

interface IMyLifecycleObserver : LifecycleObserver {

    private val TAG: String
        get() = "IMyLifecycleObserver"

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
}