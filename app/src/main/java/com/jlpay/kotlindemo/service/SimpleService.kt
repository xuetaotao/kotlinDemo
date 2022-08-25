package com.jlpay.kotlindemo.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

/**
 * Service组件的基本框架
 * 如果希望Service组件做什么事情，只要在onCreate或者onStartCommand方法中定义相关业务代码即可
 * 不能做耗时操作
 * <p>
 * 使用方法：
 * 1.通过 Context的 context.startService(intent) 方法启动 Service，stopService(intent)关闭 Service；访问者与Service之间没有关联，即使访问者退出了，
 * Service也仍然运行，因此Service与访问者之间无法进行通信、交换数据；
 * 2.通过 Context的 context.bindService(intent, serviceConnection, flags) 方法启动Service，unbindService()关闭服务；
 * 访问者与Service绑定在一起，访问者一旦退出，Service也就终止了
 */
class SimpleService : Service() {

    private val TAG: String = SimpleService::class.java.simpleName
    private var count: Int = 0
    private var quit: Boolean = false
    private val binder: MyBinder = MyBinder()

    //Service被绑定时回调该方法
    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "Service is Binded")
        return binder
    }

    //service第一次创建回调
    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "Service is Created")
        //启动一个线程，动态修改count的值
        Thread(Runnable {
            while (!quit) {
                try {
                    Thread.sleep(1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                this@SimpleService.count++
            }
        }).start()
    }

    //每次客户端调用 startService(Intent) 方法启动该Service时都会回调该方法
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand: ${Thread.currentThread().name}")//main，主线程，不能做耗时操作
        Log.e(TAG, "Service is Started")
//        return super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    //该service上绑定的所有客户端都断开链接时将会回调该方法
    override fun onUnbind(intent: Intent?): Boolean {
        Log.e(TAG, "Service is Unbinded")
        return super.onUnbind(intent)
    }

    //service被关闭之前回调该方法
    override fun onDestroy() {
        super.onDestroy()
        this.quit = true
//        stopSelf()//自己结束服务
        Log.e(TAG, "Service is Destroy")
    }

    //通过继承Binder来实现IBinder接口
    inner class MyBinder : Binder() {
        //获取Service的运行状态：count
        public fun getCount(): Int {
            return this@SimpleService.count
        }
    }
}