package com.jlpay.kotlindemo.kotlin_study

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityCoroutinesBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

class CoroutinesActivity : AppCompatActivity() {

    private val TAG: String? = CoroutinesActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding: ActivityCoroutinesBinding =
            DataBindingUtil.setContentView<ActivityCoroutinesBinding>(
                this,
                R.layout.activity_coroutines
            )
    }


    /**
     * 协程的三种启动方式
     * 1.runBlocking
     * 2.launch
     * 3.async
     */
    fun startWay(view: View) {
        runBlocking<Unit> {
            val jobByLaunch: Job = launch {
                Log.e(TAG, "launch:---> " + Thread.currentThread().name)
            }
            val jobByAsync: Deferred<String> = async() {
                delay(500L)
                Log.e(TAG, "async:--->" + Thread.currentThread().name)
                return@async "Hello"
            }
            val jobByAsyncResult = jobByAsync.await()
            Log.e(TAG, "jobByAsyncResult:--->$jobByAsyncResult")
            delay(1300L)
        }
    }


    /**
     * Continuation 接口
     * 作用就是代替 Java 异步线程 中的那些回调
     * 所有由suspend 关键字修饰的函数或者lambda表达式，都会被插入一个Continuation类型的参数
     */
    fun continuationCallBack() {
        object : Continuation<String> {
            override val context: CoroutineContext
                get() = kotlin.coroutines.EmptyCoroutineContext//乱写的

            override fun resumeWith(result: Result<String>) {
                Log.e(TAG, "resumeWith: $result")
            }
        }
    }

    /**
     * produce
     */
    fun produceDemo() {
        GlobalScope.produce<String> {
            var i = 0
        }
    }

    /**
     * Channel
     */
    suspend fun channelDemo(channel: Channel<Int>) {
        var i = 0
        Log.e(TAG, "channelDemo: " + channel.receive())
        Log.e(TAG, "channelDemo: " + channel.send(i++))
    }


    fun customTest(view: android.view.View) {
        okHttpTest()
    }

    fun okHttpTest() {
        Log.e(TAG, "okHttpTest: " + Thread.currentThread().name)
        val client: OkHttpClient = OkHttpClient.Builder().build()
        val request: Request = Request.Builder()
            .url("http://www.baidu.com")
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val threadName = Thread.currentThread().name
                Log.e(TAG, "onFailure: $threadName")
            }

            override fun onResponse(call: Call, response: Response) {
                val threadName = Thread.currentThread().name
                Log.e(TAG, "onResponse: $threadName\n" + (response.body?.string() ?: "加载失败"))
                //这里是子线程
//                Handler().post {
//                    //更新UI
//                }
            }
        })
    }

}

