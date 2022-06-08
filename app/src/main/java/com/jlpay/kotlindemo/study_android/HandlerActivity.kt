package com.jlpay.kotlindemo.study_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.*
import android.text.TextUtils
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
import kotlin.math.sqrt

/**
 * Android多线程----异步消息处理机制之Handler详解
 * https://www.cnblogs.com/qianguyihao/p/4003922.html
 *
 * Android中Handler、Thread、HandlerThread三者的区别
 * https://www.jb51.net/article/226211.htm
 *
 * Handler sendMessage与post/postDelayed区别(TODO)
 */
class HandlerActivity : AppCompatActivity() {

    private val TAG = "HandlerActivity"

    private lateinit var ivShow: ImageView
    private lateinit var etNum: EditText
    private lateinit var tvResult: TextView

    private var handler: MyHandler = MyHandler(WeakReference<HandlerActivity>(this@HandlerActivity))

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
    }

    /**
     * Handler的post方法和postDelayed方法
     */
    fun handlerPostMethod(view: View) {
        //这个开启的runnable会在这个handler所依附线程中运行，而这个handler是在UI线程中创建的，所以
        //自然地依附在主线程中了
        //对于Handler的Post方式来说，它会传递一个Runnable对象到消息队列中
        //在这个Runnable对象中，重写run()方法。一般在这个run()方法中写入需要在UI线程上的操作
        Handler().post(object : Runnable {
            override fun run() {
                Log.e(TAG, "fetchData current Thread:${Thread.currentThread().name}")//Main线程
                tvResult.text = "fetchData current Thread:${Thread.currentThread().name}\n立刻更新"
            }
        })
        Handler().postDelayed(Runnable {
            Log.e(TAG, "fetchData current Thread:${Thread.currentThread().name}")//Main线程
            tvResult.text = "fetchData current Thread:${Thread.currentThread().name}\n过了3秒了"
        }, 3000)
    }

    /**
     * 子线程计算一个数的质数
     */
    fun calPrimeNum(view: View) {
        if (TextUtils.isEmpty(etNum.text.toString())) {
            Toast.makeText(this, "请先输入质数", Toast.LENGTH_SHORT).show()
            return
        }
//        val msg: Message = Message()
        val msg: Message = Message.obtain()//官方建议,由系统自己负责message的创建和销毁
        msg.what = 0x1235
        val bundle: Bundle = Bundle()
        bundle.putInt("upper", etNum.text.toString().toInt())
        msg.data = bundle

        //开启一个子线程计算
        val calThread = CalThread()
        calThread.start()

        //这里如果立马执行sendMessage的话，有个问题，就是CalThread中的handler对象还没有实例化好
        //就会导致handleMessage丢失这条消息，所以这里先做个延时
        //延时的三种方法：(1)Thread.sleep (2)timer.schedule (3)handler.postDelayed
//        Thread.sleep(1000);
        timer.schedule(object : TimerTask() {
            override fun run() {
                calThread.handler?.sendMessage(msg)
            }
        }, 2000)
    }

    /**
     * 子线程中的handler使用
     */
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

                        //------------------更新显示UI----------------------------------------------------
                        //This is a 子线程，TODO 这里为什么子线程可以弹toast？
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
                            //handler是子线程的，所以线程还是在子线程，子线程不能更新UI
//                            tvResult.text = "currentThread:${Thread.currentThread().name}\n${nums.toString()}"
                        }, 3000)

                        //通过新的方式，本质是通过Activity中在主线程创建的handler把消息发送到主线程
                        runOnUiThread {
                            tvResult.text =
                                "runOnUiThread\tcurrentThread:${Thread.currentThread().name}\n${nums.toString()}"
                        }
                        //------------------更新显示UI----------------------------------------------------
                    }
                }
            }
            Looper.loop()
        }
    }


    /**
     * 这个其实就是上面CalThread的官方封装版
     * <p>
     * 线程间通信的时候，比如Android中常见的更新UI，涉及到的是子线程和主线程之间的通信，
     * 实现方式就是Handler+Looper，但是要自己手动操作Looper，不推荐，所以谷歌封装了HandlerThread类（类似于AsyncTask类）
     * <p>
     * HandlerThread继承于Thread，所以它本质就是个Thread。与普通Thread的差别就在于，在内部直接实现了
     * Looper的实现，这是Handler消息机制必不可少的。有了自己的looper,可以让我们在自己的线程中分发和处理消息。
     * 如果不用HandlerThread的话，需要手动去调用Looper.prepare()和Looper.loop()这些方法
     * <p>
     * 大多数情况下，handleMessage所在线程和 handler 初始化所在的线程相同，但 handler 初始化的时候可以传入一个 Looper 对象，
     * 此时handleMessage所在线程和参数looper所在线程相同
     */
    fun handlerThreadStudy(view: View) {
        val currentThreadName = Thread.currentThread().name//这里是主线程
        val handlerThread = HandlerThread("myHandlerThreadStudy")
        handlerThread.start()
        val handler = object : Handler(handlerThread.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                val arg1 = msg.arg1
                val arg2 = msg.arg2
                val data = msg.data
                val what = msg.what
                //子线程
                Log.e(
                    TAG,
                    "currentThread:\t${Thread.currentThread().name};\t" + "message.what:\t${what}"
                )
            }
        }
        handler.sendEmptyMessage(1)
    }


    /**
     * 图片轮播
     * 注意这里使用handler时用了虚引用
     */
    private val timer: Timer by lazy { Timer() }
    fun takeTurnsImage(view: View) {
        //定义一个计时器，周期性执行指定任务，
        // TimerTask的本质就是启动一条新线程，所以必须通过Handler通知主线程更新UI
        timer.schedule(object : TimerTask() {
            override fun run() {
                //这里是子线程
                Log.e(TAG, "currentThread:${Thread.currentThread().name}")
                handler.sendEmptyMessage(0x1233)
            }
        }, 0, 1200)
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

    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) {
            timer.cancel()
        }
    }
}