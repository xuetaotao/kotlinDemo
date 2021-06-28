package com.jlpay.remoteservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class AidlService : Service() {

    private val colors: Array<String> = arrayOf("红色", "黄色", "蓝色")
    private val weights: Array<Double> = arrayOf(2.3, 3.1, 1.58)
    private var color: String? = null
    private var weight: Double = 0.0
    private lateinit var catBinder: CatBinder
    private var timer: Timer = Timer()

    override fun onBind(intent: Intent?): IBinder? {
        //返回catBinder对象，在绑定本地Service的情况下，该catBinder对象会直接传给客户端的ServiceConnection对象的onServiceConnected方法的第二个参数
        //在绑定远程Service的情况下，只将catBinder对象的代理传给客户端的ServiceConnection对象的onServiceConnected方法的第二个参数
        return catBinder
    }

    override fun onCreate() {
        super.onCreate()
        catBinder = CatBinder()
        timer.schedule(object : TimerTask() {
            override fun run() {
                //随机改变Service组件内的color、weight属性值
                val rand: Int = (Math.random() * 3).toInt()
                color = colors[rand]
                weight = weights[rand]
            }
        }, 0, 800)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    //继承Stub，也就是实现了ICat接口，并实现了IBinder接口
    inner class CatBinder : ICat.Stub() {

        override fun getColor(): String? {
            return this@AidlService.color
        }

        override fun getWeight(): Double {
            return this@AidlService.weight
        }
    }
}