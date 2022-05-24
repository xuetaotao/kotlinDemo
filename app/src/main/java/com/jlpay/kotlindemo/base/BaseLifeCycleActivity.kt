package com.jlpay.kotlindemo.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

abstract class BaseLifeCycleActivity : AppCompatActivity() {

    private val TAG = this::class.java.simpleName
    private var isShowLifecycleLog = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isShowLifecycleLog) {
            Log.e(TAG, "onCreate: ")
        }
    }

    override fun onStart() {
        super.onStart()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onStart: ")
        }
    }

    override fun onResume() {
        super.onResume()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onResume: ")
        }
    }

    override fun onPause() {
        super.onPause()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onPause: ")
        }
    }

    override fun onStop() {
        super.onStop()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onStop: ")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onDestroy: ")
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onRestart: ")
        }
    }

    fun setIsShowLifecycleLog(isShowLifecycleLog: Boolean) {
        this.isShowLifecycleLog = isShowLifecycleLog
    }
}