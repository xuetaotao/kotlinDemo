package com.jlpay.kotlindemo.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * BroadcastReceiver监听的事件源是Android应用中的其他组件,相当于一个全局的事件监听器
 * 实现了自己的BroadcastReceiver后，有两种方式来注册这个系统级的“事件监听器”
 * 1.在Java代码中通过 Context.registerReceiver() 方法注册
 * 2.在 AndroidManifest文件中使用 <receiver../>元素完成注册
 */
class MyReceiver : BroadcastReceiver() {

    //当其他组件通过 sendBroadcast()，sendOrderedBroadcast() 发送广播消息时，如果该 BroadcastReceiver 也对该消息
    //感兴趣（通过intent-filter配置），那么该BroadcastReceiver的onReceive()方法将会被触发
    override fun onReceive(context: Context?, intent: Intent?) {

    }
}