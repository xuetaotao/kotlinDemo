package com.jlpay.kotlindemo.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

abstract class BaseActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getResourceId())

        initView()
        initData()
    }


    abstract fun getResourceId(): Int
    abstract fun initView()
    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
    }

    open fun <T> getActivityLifecycleProvider(): LifecycleTransformer<T> {
//        return bindToLifecycle() //可以绑定Activity生命周期
        return bindUntilEvent(ActivityEvent.DESTROY);//可以绑定Activity生命周期
    }

    fun getLifecycleOwner(): LifecycleOwner {
        return this
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}