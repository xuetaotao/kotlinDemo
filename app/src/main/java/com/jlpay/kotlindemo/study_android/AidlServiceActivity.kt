package com.jlpay.kotlindemo.study_android

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.remoteservice.ICat

/**
 * 步骤1：将服务端的AIDL文件所在的包复制到客户端目录下（Project/app/src/main），并进行编译
 * 步骤2：在主布局文件定义“绑定服务”的按钮
 * 步骤3：在MainActivity.java里
 * 使用Stub.asInterface接口获取服务器的Binder；通过Intent指定服务端的服务名称和所在包，进行Service绑定；根据需要调用服务提供的接口方法。
 */
class AidlServiceActivity : AppCompatActivity() {

    private var catService: ICat? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            //获取远程Service的onBind()方法所返回的对象的代理
            Log.e("AidlServiceActivity", "--AidlService Connected--")
            catService = ICat.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("AidlServiceActivity", "--AidlService Disconnected--")
            catService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidl_service)

        initView()
    }

    private fun initView() {
        //创建绑定Service所需的Intent
        val intent: Intent = Intent()
        intent.setAction("com.jlpay.remoteservice.action.AidlService")
        //设置要启动的Service所在包：变成显示Intent
        intent.setPackage("com.jlpay.remoteservice")
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE)

        val tv_color: TextView = findViewById(R.id.tv_color)
        val tv_weight: TextView = findViewById(R.id.tv_weight)
        val btn_get: Button = findViewById(R.id.btn_get)
        btn_get.setOnClickListener {
            try {
                tv_color.setText("颜色：" + catService?.color)
                tv_weight.setText("重量：" + catService?.weight)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        this.unbindService(serviceConnection)
    }
}