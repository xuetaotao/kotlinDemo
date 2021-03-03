package com.jlpay.kotlindemo.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import java.lang.ref.WeakReference
import java.util.*

class FrameLayoutActitivy : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, FrameLayoutActitivy::class.java)
            context.startActivity(intent)
        }
    }

    private val names: IntArray =
        intArrayOf(R.id.view01, R.id.view02, R.id.view03, R.id.view04, R.id.view05, R.id.view06)

    private var views = arrayOfNulls<TextView>(6)

    private var handler: MyHandler = MyHandler(WeakReference<FrameLayoutActitivy>(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_layout)
        for ((i, name) in names.withIndex()) {
            views[i] = findViewById(name)
        }

        //定义一个线程周期性改变 currentColor 的值，每隔0.2秒执行一次
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.sendEmptyMessage(0x123)
            }
        }, 0, 200)
    }


    /**
     * accessModifier: 访问权限修饰符
     * private // 仅在同一个文件中可见
     * protected // 同一个文件中或子类可见
     * public // 所有调用的地方都可见
     * internal // 同一个模块中可见
     *
     * 直接使用匿名内部类定义 Handler的实例，该 Handler直接使用主线程的Looper或 MessageQueue，就可能导致内存泄漏
     */
    internal class MyHandler(private val activity: WeakReference<FrameLayoutActitivy>) : Handler() {

        private val colors: IntArray = intArrayOf(
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5,
            R.color.color6
        )

        private var currentColor: Int = 0

        override fun handleMessage(msg: Message) {
            if (msg.what == 0x123) {
                for ((i, name) in activity.get()!!.names.withIndex()) {
                    activity.get()!!.views[i]!!.setBackgroundResource(colors[(i + currentColor) % colors.size])
                }
                currentColor++
            }
            super.handleMessage(msg)
        }
    }
}