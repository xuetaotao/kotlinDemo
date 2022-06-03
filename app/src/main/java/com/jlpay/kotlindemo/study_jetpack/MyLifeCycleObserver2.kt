package com.jlpay.kotlindemo.study_jetpack

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 目前官方更推荐这种方式
 */
class MyLifeCycleObserver2 : DefaultLifecycleObserver {

    private val TAG: String = this::class.java.simpleName

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.e(TAG, "onMyCreate2: ")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.e(TAG, "onMyStart2: ")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.e(TAG, "onMyResume2")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.e(TAG, "onMyPause2")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.e(TAG, "onMyStop2: ")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.e(TAG, "onMyDestroy2: ")
    }
}