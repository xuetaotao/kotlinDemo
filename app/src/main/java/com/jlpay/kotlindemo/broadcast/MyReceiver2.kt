package com.jlpay.kotlindemo.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyReceiver2 : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = getResultExtras(true)
        val string = bundle.getString("firstAdd")
        Toast.makeText(context, "第一个Receiver存入的消息：$string", Toast.LENGTH_SHORT).show()
        Log.e("TAG", "原本的消息：${intent?.getStringExtra("msg")}\n第一个Receiver存入的消息：$string")
    }
}