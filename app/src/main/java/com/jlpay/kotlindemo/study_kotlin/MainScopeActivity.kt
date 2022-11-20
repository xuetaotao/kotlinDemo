package com.jlpay.kotlindemo.study_kotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

//MainScope用法二，使用MainScope委托
class MainScopeActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val TAG: String? = MainScopeActivity::class.java.simpleName

    private lateinit var textView3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainscope)
        textView3 = findViewById(R.id.textView3)
    }

    fun testClick(view: View) {
        mainScopeDemo2()
    }


    fun mainScopeDemo() {
        launch {
            val result = withContext(Dispatchers.IO) {
                Log.e(TAG, "mainScopeDemo-->1: ${Thread.currentThread().name}")
                baiduRequest()
            }
            textView3.text = result.body?.string()
            Log.e(TAG, "mainScopeDemo-->2: ${Thread.currentThread().name}")
            //结果：
            //mainScopeDemo-->1: DefaultDispatcher-worker-1
            //mainScopeDemo-->2: main
        }
    }

    fun mainScopeDemo2() {
        launch {
            try {
                delay(10000)//延时10秒，是一个挂起函数
            } catch (e: Exception) {
                //延时未结束关闭Activity的话就会抛出异常
                //kotlinx.coroutines.JobCancellationException: Job was cancelled;
                e.printStackTrace()
                Log.e(TAG, "mainScopeDemo2: 协程异常抛出,${e.message}")
            }
            Log.e(TAG, "mainScopeDemo2-->1: ${Thread.currentThread().name}")
        }
        //延时未结束关闭Activity的结果:
        //mainScopeDemo2: 协程异常抛出,Job was cancelled
        //mainScopeDemo2-->1: main
        //正常结果是延时10秒后打印 mainScopeDemo2-->1: main
    }

    private fun baiduRequest(): Response {
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder().url("http://www.baidu.com").get().build()
        return okHttpClient.newCall(request).execute()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}