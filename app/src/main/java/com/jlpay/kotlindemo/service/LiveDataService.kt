package com.jlpay.kotlindemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.jlpay.kotlindemo.study_jetpack.MyLiveData
import kotlin.concurrent.thread

/**
 * LiveData 模拟后台推送服务
 * service 是运行在主进程的 main 线程上
 */
class LiveDataService : Service() {

    private val TAG: String = this::class.java.simpleName

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //service 是运行在主进程的 main 线程上，需要可启动新线程处理耗时任务
        thread {
            for (x in 1..10) {//[1,10]，从1到10，两边都包括
                Log.e(TAG, "服务器给你推送消息啦(叮咚声响)，消息内容是${x}")
                MyLiveData.info1.postValue("服务器给你推送消息啦，消息内容是${x}")
                Thread.sleep(5000)//5秒钟推送一次
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}