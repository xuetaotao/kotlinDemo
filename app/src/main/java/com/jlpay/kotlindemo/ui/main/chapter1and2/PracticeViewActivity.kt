package com.jlpay.kotlindemo.ui.main.chapter1and2

import android.content.*
import android.widget.Button
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.broadcast_receiver.MyReceiver
import com.jlpay.kotlindemo.ui.base.BaseActivity
import java.util.concurrent.Executor

class PracticeViewActivity : BaseActivity() {

    companion object {
        @JvmStatic
        public fun newInstance(context: Context) {
            val intent = Intent(context, PracticeViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getResourceId(): Int {
        return R.layout.activity_practice_view
    }

    override fun initView() {
        val btnWidget = findViewById<Button>(R.id.btn_widget)
        btnWidget.setOnClickListener {
            FirstUiActivity.newInstance(this)
        }
    }

    override fun initData() {
    }

    /**
     * 广播注册：动态注册
     */
    fun registerBroadcastReceiver() {
        val broadcastReceiver: BroadcastReceiver = MyReceiver()
        val intentFilter: IntentFilter = IntentFilter("com.jlpay.kotlindemo.MyReceiver")
        registerReceiver(broadcastReceiver, intentFilter)
    }

    /**
     * 启动一个广播（必须是显示Intent）
     * Intent封装了当前组件需要启动或触发的目标组件的信息
     */
    fun startBroadcast(context: Context) {
//        val intent = Intent(context, MyReceiver::class.java)
        val intent: Intent = Intent("com.jlpay.kotlindemo.MyReceiver")
        intent.setPackage("com.jlpay.kotlindemo")
        intent.putExtra("state", "简单的状态测试")
        context.sendBroadcast(intent)
    }

    fun startBroadcast(context: Context, receiverPermission: String) {
        val intent: Intent = Intent()
        intent.putExtra("state", "state")
        context.sendOrderedBroadcast(intent, receiverPermission)
    }

    /**
     * 启动一个Service
     */
    fun startService(context: Context) {
        val intent: Intent = Intent()
        intent.putExtra("URL", "www.baidu.com")
        context.startService(intent)
    }

    fun startService2(
        context: Context,
        flags: Int,
        executor: Executor,
        serviceConnection: ServiceConnection
    ) {
        val intent: Intent = Intent()
        context.bindService(intent, serviceConnection, flags)
    }
}