package com.jlpay.kotlindemo.android_study

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.service.SimpleService

class SimpleServiceActivity : AppCompatActivity() {

    //保持所启动的Service的IBinder对象
    private lateinit var binder: SimpleService.MyBinder

    //定义一个Ser对象
    private val serviceConnection = object : ServiceConnection {
        //当Activity与Service链接成功时回调该方法
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e("SimpleServiceActivity", "--Service Connected--")
            binder = service as SimpleService.MyBinder
        }

        //当Activity与Service异常断开链接时回调该方法（调用unbindService()主动关闭不会回调）
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("SimpleServiceActivity", "--Service Disconnected--")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_service)

        val intent: Intent = Intent(this@SimpleServiceActivity, SimpleService::class.java)

        //Service的第一种启动方式
        val btn_start: Button = findViewById(R.id.btn_start)
        btn_start.setOnClickListener {
            startService(intent)
        }
        val btn_stop: Button = findViewById(R.id.btn_stop)
        btn_stop.setOnClickListener {
            stopService(intent)
        }


        //Service的第二种启动方式
        val btn_bind: Button = findViewById(R.id.btn_bind)
        btn_bind.setOnClickListener {
            //最后的flag参数意义：指示绑定时是否自动创建Service(如果还未创建)：0：不自动创建；BIND_AUTO_CREATE：自动创建
            bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE)
        }
        val btn_unbind: Button = findViewById(R.id.btn_unbind)
        btn_unbind.setOnClickListener {
            unbindService(serviceConnection)
        }
        val btn_status: Button = findViewById(R.id.btn_status)
        btn_status.setOnClickListener {
            Toast.makeText(this, "Service的count值为：" + binder.getCount(), Toast.LENGTH_SHORT).show()
        }
    }
}