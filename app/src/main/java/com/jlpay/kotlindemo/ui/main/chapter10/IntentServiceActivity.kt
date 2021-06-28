package com.jlpay.kotlindemo.ui.main.chapter10

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.service.MyIntentService

class IntentServiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_service)

        initView()
    }

    private fun initView() {
        val btn_service: Button = findViewById(R.id.btn_service)
        btn_service.setOnClickListener {
            val intent: Intent = Intent(this@IntentServiceActivity, MyService::class.java)
            startService(intent)
        }
        val btn_intentService: Button = findViewById(R.id.btn_intentService)
        btn_intentService.setOnClickListener {
            val intent: Intent = Intent(this@IntentServiceActivity, MyIntentService::class.java)
            startService(intent)
        }
    }

    class MyService : Service() {

        private var count: Int = 0

        override fun onBind(intent: Intent?): IBinder? {
            return null
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            //该方法内执行耗时任务可能导致ANR(Application Not Responding)
            val endTime: Long = System.currentTimeMillis() + 20 * 1000
            Log.e("IntentServiceActivity", "onStartCommand")
            while (System.currentTimeMillis() < endTime) {
                try {
                    Thread.sleep(1000)
                    Log.e("IntentServiceActivity", count++.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Log.e("IntentServiceActivity", "耗时任务执行完成")
            return START_STICKY
        }

        //service被关闭之前回调该方法
        override fun onDestroy() {
            super.onDestroy()
            Log.e("IntentServiceActivity", "Service is Destroy")
        }
    }
}