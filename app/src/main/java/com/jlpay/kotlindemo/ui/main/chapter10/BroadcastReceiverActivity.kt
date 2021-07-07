package com.jlpay.kotlindemo.ui.main.chapter10

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.broadcast_receiver.MyReceiver

class BroadcastReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcast)
    }

    /**
     * 发送普通广播
     */
    fun sendBroadcast(view: View) {
//        val intent = Intent(this, MyReceiver::class.java)
        val intent = Intent()
        intent.setAction("com.jlpay.kotlindemo.MyReceiver")
        intent.setPackage("com.jlpay.kotlindemo")
        intent.putExtra("msg", "这是一条小道消息")
        sendBroadcast(intent)
    }

    fun sendOrderBroadcast(view: View) {
        val intent = Intent()
        intent.setAction("com.jlpay.kotlindemo.MyReceiver")
        intent.setPackage("com.jlpay.kotlindemo")
        intent.putExtra("msg", "这是一条有序的小道消息")
        sendOrderedBroadcast(intent, null)
    }


    /**
     * 广播注册：动态注册
     */
    fun registerBroadcastReceiver() {
        val intentFilter = IntentFilter("com.jlpay.kotlindemo.MyReceiver")
        val receiver = MyReceiver()
        registerReceiver(receiver, intentFilter)
    }

    fun telephonyService(view: View) {
        val telephony_service: TelephonyManager =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val smsManager = SmsManager.getDefault()
        val audioManager: AudioManager = getSystemService(Service.AUDIO_SERVICE) as AudioManager
        //手势
        GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent?) {
                super.onLongPress(e)
            }
        })
    }
}