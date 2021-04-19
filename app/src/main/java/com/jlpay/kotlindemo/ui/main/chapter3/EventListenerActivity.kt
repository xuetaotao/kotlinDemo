package com.jlpay.kotlindemo.ui.main.chapter3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

/**
 * Touch 事 件 一 般 的 传 递 流 程:
 * Activity-->window(唯一实现类是PhoneWindow)-->顶级View（DecorView）-->ViewGroup-->View：附文：https://blog.csdn.net/fitaotao/article/details/115864829
 */
class EventListenerActivity : AppCompatActivity() {

    private val TAG: String = EventListenerActivity::class.java.simpleName

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, EventListenerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eventlistener)

        initView()
    }

    fun initView() {
        externalClassListenerTest()
        eventDeliver()
    }

    //Activity的onTouchEvent方法，该方法可监听它所包含的所有组件上的触碰事件
    //TODO 有问题：不知道什么原因这里监听不到
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        Log.e("eventDeliver：", "onTouchEvent in Activity")
        return false //返回false，该事件会向外传播
    }

    fun eventDeliver() {
        val btnEventdeliver: Button = findViewById(R.id.btn_eventdeliver)
        btnEventdeliver.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    Log.e("eventDeliver：", "btnEventdeliver 基于回调的事件处理：OnTouchListener，ACTION_DOWN")
                } else if (event.action == MotionEvent.ACTION_UP) {
                    Log.e("eventDeliver：", "btnEventdeliver 基于回调的事件处理：OnTouchListener，ACTION_UP")
                }
                return false //返回false，该事件会向外传播
            }
        })
        btnEventdeliver.setOnClickListener {
            Log.e("eventDeliver：", "btnEventdeliver 基于监听的事件处理：OnClickListener")
        }
    }

    /**
     * 外部类作为事件监听器类
     * TODO 有问题：Invalid destinationAddress，暂时不知道怎么解
     */
    fun externalClassListenerTest() {
        val et_address: EditText = findViewById(R.id.et_address)
        val et_content: EditText = findViewById(R.id.et_content)
        val btn_send: Button = findViewById(R.id.btn_send)
        btn_send.setOnClickListener(
            SendSmsListener(
                this,
                et_address.text.toString().trim(),
                et_content.text.toString().trim()
            )
        )
    }
}