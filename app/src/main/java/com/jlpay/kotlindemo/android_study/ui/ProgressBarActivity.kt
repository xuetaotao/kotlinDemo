package com.jlpay.kotlindemo.android_study.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import java.lang.ref.WeakReference

class ProgressBarActivity : AppCompatActivity() {

    private lateinit var bar: ProgressBar
    private lateinit var bar2: ProgressBar

    private val data = IntArray(100)
    private var hasData: Int = 0
    private var status: Int = 0
    private lateinit var myHandler: MyHandler

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, ProgressBarActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progressbar)

        bar = findViewById(R.id.bar)
        bar2 = findViewById(R.id.bar2)

        myHandler =
            MyHandler(
                WeakReference<ProgressBarActivity>(
                    this
                )
            )

        //启动线程来执行任务
        val thread: Thread = object : Thread() {
            override fun run() {
                while (status < 100) {
                    Log.e("TAG", "doWork：$status")
                    status = doWork()
                    myHandler.sendEmptyMessage(0x1001)//发送消息
                }
            }
        }
        thread.start()


        //SeekBar
        val image: ImageView = findViewById(R.id.image)
        val seekbar: SeekBar = findViewById(R.id.seekbar)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                image.imageAlpha = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        val seekbar2: SeekBar = findViewById(R.id.seekbar2)


        //RatingBar
        val image2: ImageView = findViewById(R.id.image2)
        val ratingbar: RatingBar = findViewById(R.id.ratingbar)
        ratingbar.setOnRatingBarChangeListener(object : RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
                image2.imageAlpha = (rating * 255 / 5).toInt()
            }
        })
    }

    /**
     * 类的修饰符包括 classModifier 和_accessModifier_:
     * classModifier: 类属性修饰符，标示类本身特性。abstract // 抽象类 final // 类不可继承，默认属性 enum // 枚举类 open // 类可继承，类默认是final的 annotation // 注解类
     * accessModifier: 访问权限修饰符private // 仅在同一个文件中可见 protected // 同一个文件中或子类可见 public // 所有调用的地方都可见 internal // 同一个模块中可见
     */
    internal class MyHandler(var activity: WeakReference<ProgressBarActivity>) :
        Handler() {

        override fun handleMessage(msg: Message) {
            if (msg.what == 0x1001) {
                activity.get()!!.bar.progress = activity.get()!!.status
                activity.get()!!.bar2.progress = activity.get()!!.status
            }
        }
    }

    /**
     * 模拟耗时操作
     */
    private fun doWork(): Int {
        data[hasData++] = (Math.random() * 100).toInt()
        try {
            Thread.sleep(100)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return hasData
    }
}