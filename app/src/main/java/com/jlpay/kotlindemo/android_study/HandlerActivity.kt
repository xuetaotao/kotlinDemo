package com.jlpay.kotlindemo.android_study

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt

/**
 * Android多线程----异步消息处理机制之Handler详解
 * https://www.cnblogs.com/qianguyihao/p/4003922.html
 */
class HandlerActivity : AppCompatActivity() {

    private val TAG = "HandlerActivity"

    private lateinit var ivShow: ImageView
    private lateinit var etNum: EditText
    private lateinit var tvResult: TextView

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
        tvResult = findViewById(R.id.tv_result)

        //这个开启的runnable会在这个handler所依附线程中运行，而这个handler是在UI线程中创建的，所以
        //自然地依附在主线程中了
        //对于Handler的Post方式来说，它会传递一个Runnable对象到消息队列中
        // 在这个Runnable对象中，重写run()方法。一般在这个run()方法中写入需要在UI线程上的操作
        Handler().postDelayed(Runnable {
            Log.e(TAG, "fetchData current Thread:${Thread.currentThread().name}")//Main线程
            tvResult.text = "fetchData current Thread:${Thread.currentThread().name}\n过了五秒了"
        }, 5000)

        //定义一个计时器，周期性执行指定任务，TimerTask的本质就是启动一条新线程，所以必须通过Handler通知主线程更新UI
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.sendEmptyMessage(0x1233)
            }
        }, 0, 1200)

        calThread = CalThread()
        calThread.start()

        handlerThreadStudy()
    }

    fun cal(view: View) {
//        val msg: Message = Message()
        val msg: Message = Message.obtain()//官方建议,由系统自己负责message的创建和销毁
        msg.what = 0x1235
        val bundle: Bundle = Bundle()
        bundle.putInt("upper", etNum.text.toString().toInt())
        msg.data = bundle
        calThread.handler?.sendMessage(msg)
    }

    fun handlerThreadStudy() {
        val handlerThread = HandlerThread("myHandlerThreadStudy")
        handlerThread.start()
        val handler = object : Handler(handlerThread.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                //子线程
                Log.e(
                    TAG,
                    "handleMessage: handlerThreadStudy\ncurrent Thread:${Thread.currentThread().name}"
                )
            }
        }
        handler.sendEmptyMessage(1)
    }


    @SuppressLint("HandlerLeak")
    inner class CalThread : Thread() {
        var handler: Handler? = null

        override fun run() {
            //一个Handler对应一个Looper对象，一个Looper对应一个MessageQueue对象，
            // 使用Handler生成Message，所生成的Message对象的Target属性，就是该对象。
            // 而一个Handler可以生成多个Message，所以说，Handler和Message是一对多的关系
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

                        //This is a 子线程
                        Toast.makeText(
                            this@HandlerActivity,
                            "currentThread:${Thread.currentThread().name}\n${nums.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        //子线程不能更新UI
//                        tvResult.text =
//                            "currentThread:${Thread.currentThread().name}\n${nums.toString()}"


                        handler?.postDelayed({
                            Toast.makeText(
                                this@HandlerActivity,
                                "currentThread:${Thread.currentThread().name}",
                                Toast.LENGTH_SHORT
                            ).show()
                            //子线程不能更新UI
//                            tvResult.text = "currentThread:${Thread.currentThread().name}\n${nums.toString()}"
                        }, 3000)


                        //通过新的方式
                        runOnUiThread {
                            tvResult.text =
                                "runOnUiThread\ncurrentThread:${Thread.currentThread().name}\n${nums.toString()}"
                        }
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