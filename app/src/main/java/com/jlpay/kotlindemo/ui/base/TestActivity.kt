package com.jlpay.kotlindemo.ui.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {

    val TAG = TestActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "=======" + "onCreate()" + "=========")
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "=======" + "onStart()" + "=========")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "=======" + "onResume()" + "=========")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "=======" + "onPause()" + "=========")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "=======" + "onStop()" + "=========")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "=======" + "onDestroy()" + "=========")
    }
}