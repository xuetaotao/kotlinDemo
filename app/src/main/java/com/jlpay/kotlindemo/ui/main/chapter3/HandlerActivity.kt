package com.jlpay.kotlindemo.ui.main.chapter3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.utils.AppUtils
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt

class HandlerActivity : AppCompatActivity() {

    private lateinit var ivShow: ImageView
    private lateinit var etNum: EditText

    private var handler: MyHandler = MyHandler(WeakReference<HandlerActivity>(this@HandlerActivity))
    private lateinit var calThread: CalThread

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, HandlerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler)

        ivShow = findViewById(R.id.iv_show)
        etNum = findViewById(R.id.et_num)

        //定义一个计时器，周期性执行指定任务，TimerTask的本质就是启动一条新线程，所以必须通过Handler通知主线程更新UI
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.sendEmptyMessage(0x1233)
            }
        }, 0, 1200)

        calThread = CalThread()
        calThread.start()
    }

    fun cal(view: View) {
        val msg: Message = Message()
        msg.what = 0x1235
        val bundle: Bundle = Bundle()
        bundle.putInt("upper", etNum.text.toString().toInt())
        msg.data = bundle
        calThread.handler?.sendMessage(msg)
    }


    @SuppressLint("HandlerLeak")
    class CalThread : Thread() {
        var handler: Handler? = null

        override fun run() {
            Looper.prepare()
//            handler = object : Handler() {
            handler = object : Handler(Looper.myLooper()!!, null) {
                override fun handleMessage(msg: Message) {
                    if (msg.what == 0x1235) {
                        val upper: Int = msg.data.getInt("upper")
                        val nums: ArrayList<Int> = ArrayList<Int>()
                        for (i in 2..upper) {//[2,upper]，计算从2开始，到upper的所有质数
                            var j: Int = 2
                            while (j <= sqrt(i.toDouble())) {
                                if (i != 2 && i % j == 0) {
                                    break
                                }
                                j++
                            }
                            nums.add(i)
                        }
                        Toast.makeText(AppUtils.getContext(), nums.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            Looper.loop()
        }
    }


    class MyHandler : Handler {
        private var activity: WeakReference<HandlerActivity>
        private val imageIds =
            intArrayOf(R.mipmap.hanfei, R.mipmap.liang, R.mipmap.nongyu, R.mipmap.zinv)
        private var currentImageId: Int = 0

        constructor(activity: WeakReference<HandlerActivity>) : this(activity, null, false)

        constructor(
            activity: WeakReference<HandlerActivity>,
            callback: Callback?,
            async: Boolean
        ) : super(Looper.getMainLooper(), callback) {
            this.activity = activity
        }

        override fun handleMessage(msg: Message) {
            if (msg.what == 0x1233) {
                activity.get()?.ivShow?.setImageResource(imageIds[currentImageId++ % imageIds.size])
            }
        }
    }
}