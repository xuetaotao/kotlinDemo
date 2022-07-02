package com.jlpay.kotlindemo.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

/**
 * 建议学习使用：WorkManager 或者 JobIntentService
 * Android11已经过时，不推荐使用
 *
 * Service本身存在的两个问题：
 * 1.Service不会专门启动一个单独的进程，Service与它所在应用位于同一个进程中
 * 2.Service不是一个新的线程，因此不应该在Service中直接处理耗时的任务（如有需要可启动新线程处理耗时任务）
 * <p>
 * IntentService的特征：
 * 1.IntentService会创建单独的worker线程来处理所有的Intent请求
 * 2.IntentService会创建单独的worker线程来处理 onHandleIntent() 方法所实现的代码，因此开发者无需处理多线程的问题
 * 3.当所有请求处理完成后 IntentService 会自动停止，无需调用 stopSelf() 方法来停止该 Service
 * 4.为Service 的onBind方法提供了默认实现，默认实现的onBind()方法返回null
 * 5.为Service 的onStartCommand()方法提供了默认实现，该实现会将请求Intent添加到队列中
 */
class MyIntentService : IntentService {

    private var count: Int = 0
    private val jobIntentService: JobIntentService by lazy {
        MyJobIntentService()
    }

    constructor() : super("MyIntentService")

    constructor(name: String) : super(name)

    override fun onHandleIntent(intent: Intent?) {
        //这里可以通过intent传递一个参数过来，然后根据参数执行不同的任务，类似Rxjava处理的链式任务顺序执行
        //参考：Android多线程：IntentService使用教程(含实例讲解)：https://blog.csdn.net/carson_ho/article/details/90437126/
        //该方法内可以执行耗时任务，因为新开了子线程处理
        val endTime: Long = System.currentTimeMillis() + 20 * 1000
        Log.e("IntentServiceActivity", "onStartCommand")
        while (System.currentTimeMillis() < endTime) {
            try {
                Thread.sleep(1000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            this@MyIntentService.count++
            Log.e("IntentServiceActivity", count.toString())
        }
        Log.e("IntentServiceActivity", "耗时任务执行完成")
    }

    //service被关闭之前回调该方法
    override fun onDestroy() {
        super.onDestroy()
        Log.e("IntentServiceActivity", "Service is Destroy")
    }

    inner class MyJobIntentService : JobIntentService() {
        override fun onHandleWork(intent: Intent) {
            Log.e("IntentServiceActivity", "MyJobIntentService onHandleWork")
        }
    }
}