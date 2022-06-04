package com.jlpay.kotlindemo.study_jetpack

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityLifecycleJetpackBinding

/**
 * jetpack之 lifecycle
 * 能够监听Activity/Fragment生命周期行为的各种变化(观察者模式)
 * AppCompatActivity的父类实现了LifecycleOwner接口
 */
class JetpackLifeCycleActivity : AppCompatActivity() {

    private val TAG: String = this::class.java.simpleName
    private lateinit var mBinding: ActivityLifecycleJetpackBinding
    private var isShowLifecycleLog = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isShowLifecycleLog) {
            Log.e(TAG, "onCreate: ")
        }

        //添加自己设置的监听LifeCycleObserver，可以同时添加多个监听
        //一般是在BaseActivity关联注册
//        lifecycle.addObserver(MyLifeCycleObserver())
        lifecycle.addObserver(MyLifeCycleObserver2())

        mBinding = DataBindingUtil.setContentView<ActivityLifecycleJetpackBinding>(
            this,
            R.layout.activity_lifecycle_jetpack
        )
        mBinding.lifecycleOwner = this
        mBinding.onClick = OnClickProxy()
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
//        lifecycle.addObserver(MyLifeCycleObserver2())//在这里注册会有问题
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

    /**
     * 相关的类
     */
    fun relatedClassed() {
        //是一个表示android生命周期及状态的对象，可以有效的避免内存泄漏和解决android生命周期的常见难题
        var lifecycle: Lifecycle
        //用于连接有生命周期的对象，如activity，fragment
        val lifecycleOwner: LifecycleOwner
        //用于观察lifecycleOwner
        val lifecycleObserver: LifecycleObserver
    }

    inner class OnClickProxy {
        fun onClick(view: View) {
            when (view.id) {
                R.id.btn_lifecycle -> {
                    Toast.makeText(this@JetpackLifeCycleActivity, "LifeCycle", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}