package com.jlpay.kotlindemo.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast

/**
 * BroadcastReceiver监听的事件源是Android应用中的其他组件,相当于一个全局的事件监听器
 * 实现了自己的BroadcastReceiver后，有两种方式来注册这个系统级的“事件监听器”
 * 1.在Java代码中通过 Context.registerReceiver() 方法注册
 * 2.在 AndroidManifest文件中使用 <receiver../>元素完成注册
 * <p>
 * 不要进行耗时操作，需要的话可以用Intent启动一个Service来完成该操作，
 * 因为是在主线程，超过10秒会报ANR
 */
class MyReceiver : BroadcastReceiver() {

    //当其他组件通过 sendBroadcast()，sendOrderedBroadcast() 发送广播消息时，如果该 BroadcastReceiver 也对该消息
    //感兴趣（通过intent-filter配置），那么该BroadcastReceiver的onReceive()方法将会被触发
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(
            context,
            "接收到的Intent的Action是：${intent?.action}\n消息内容是：${intent?.getStringExtra("msg")}",
            Toast.LENGTH_SHORT
        ).show()
        Log.e("TAG", "接收到的Intent的Action是：${intent?.action}\n消息内容是：${intent?.getStringExtra("msg")}")
        Log.e("TAG", "onReceive的线程：${Thread.currentThread().name}")//main，主线程

        val bundle: Bundle = Bundle()
        bundle.putString("firstAdd", "这是第一个MyReceiver添加后的消息")
        setResultExtras(bundle)
        //取消有序广播的继续传播
//        abortBroadcast()
    }
}