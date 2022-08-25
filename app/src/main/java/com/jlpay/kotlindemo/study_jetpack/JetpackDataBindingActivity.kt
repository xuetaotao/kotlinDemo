package com.jlpay.kotlindemo.study_jetpack

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityDatabindingBinding
import kotlin.concurrent.thread

/**
 * Jetpack dataBinding(数据绑定库)
 * 官网地址：https://developer.android.google.cn/topic/libraries/data-binding?hl=zh_cn
 * Android从零开始搭建MVVM架构（1）————DataBinding：https://juejin.cn/post/6844903955693043725
 *
 * dataBinding 双向绑定就不存在数据粘性的问题了
 * 注：单向绑定是由数据驱动 UI 变化，当 UI 发生变化时并不会引起数据的改变。
 * 双向绑定才是UI和数据改变互相影响。
 */
class JetpackDataBindingActivity : AppCompatActivity() {

    private val TAG: String = this::class.java.simpleName
    private lateinit var mbinding: ActivityDatabindingBinding
    val name: ObservableField<String> by lazy {
        ObservableField<String>().apply {
            addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    val nameObservableField = sender as ObservableField<String>
                    Log.e(TAG, "name发生变化了：${nameObservableField.get()}")
                    Toast.makeText(
                        this@JetpackDataBindingActivity,
                        "name发生变化了：${nameObservableField.get()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
    val password: ObservableField<String> by lazy {
        ObservableField("这是密码")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_databinding)
        mbinding.lifecycleOwner = this
        mbinding.click = OnClickProxy()
        mbinding.jetpackDataBindingActivity = this

        thread {
            Thread.sleep(3000)
            password.set("i love you")
        }
    }

    inner class OnClickProxy {
        fun onClick(view: View) {
            when (view.id) {
                R.id.button20 -> {
                    Log.e(TAG, "提交结果:\n姓名：${name.get()}\n密码：${password.get()}")
                    Toast.makeText(
                        this@JetpackDataBindingActivity, "提交结果:\n" +
                                "姓名：${name.get()}\n" +
                                "密码：${password.get()}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}