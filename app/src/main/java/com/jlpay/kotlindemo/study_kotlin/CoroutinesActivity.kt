package com.jlpay.kotlindemo.study_kotlin

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

/**
 * 协程是什么？就是切线程,他就是一个比较方便的线程框架，好在哪？好在方便。最方便的地方是，它能够在同一个代码块里进行多次的线程切换
 * 协程,对我们来说就是launch或者async里面的代码块，它其实就是基于线程而实现的一套更上层的工具API，类似于Executor API，Handler API
 * 协程的挂起是什么？ 所谓的挂起，其实就是一个稍后会被自动切回来(切回来的动作在协程里叫做resume--恢复)的线程切换
 * 协程挂起的非阻塞式是什么？ 其实就是用看起来阻塞的代码写出非阻塞的操作,其实还是切换线程做耗时操作（IO/计算/等待），然后使得主线程不会卡顿阻塞
 *
 * 码上开学 ( kaixue.io ) 之：Kotlin 的协程第 1 期: https://www.bilibili.com/video/BV164411C7FK?spm_id_from=333.999.0.0
 * 码上开学 ( kaixue.io ) 之：Kotlin 的协程第 2 期 :https://www.bilibili.com/video/BV1KJ41137E9?spm_id_from=333.999.0.0
 * 码上开学 ( kaixue.io ) 之：Kotlin 的协程第 3 期: https://www.bilibili.com/video/BV1JE411R7hp?spm_id_from=333.999.0.0
 */
class CoroutinesActivity : AppCompatActivity() {

    private val TAG: String? = CoroutinesActivity::class.java.simpleName

    private lateinit var mBinding: ActivityCoroutinesBinding

    //MainScope用法一
    //MainScope，在Activity中使用，可以在onDestroy中取消协程
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityCoroutinesBinding>(
            this,
            R.layout.activity_coroutines
        )
    }

    fun customTest(view: View) {
//        okHttpTest()
//        requestBaidu2()
//        studyDemo2()
        mainScopeDemo2()
    }

    //注：requestBaidu2是一个耗时方法，但是如果requestBaidu这种，customTest方法前就不能加suspend，否则会崩溃
    fun suspendTest(view: View) {
        requestBaidu2()
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
     * launch方法参数解释：
     * ------> context：协程上下文，可以指定协程运行的线程。默认与指定的CoroutineScope中的coroutineContext保持一致，比如GlobalScope默认运行在一个后台工作线程内。
     * 也可以通过显示指定参数来更改协程运行的线程，Dispatchers提供了几个值可以指定：Dispatchers.Default、Dispatchers.Main、Dispatchers.IO、Dispatchers.Unconfined。
     * ------> start：协程的启动模式。默认的CoroutineStart.DEFAULT是指协程立即执行，除此之外还有CoroutineStart.LAZY、CoroutineStart.ATOMIC、CoroutineStart.UNDISPATCHED。
     * ------> block：协程主体。也就是要在协程内部运行的代码，可以通过lamda表达式的方式方便的编写协程内运行的代码。
     * ------> CoroutineExceptionHandler：指定CoroutineExceptionHandler来处理协程内部的异常。
     * ------> Job：返回值，对当前创建的协程的引用。可以通过Job的start、cancel、join等方法来控制协程的启动和取消。
     */
    fun requestBaidu() {
        GlobalScope.launch(Dispatchers.IO) {
            //方式一，这里并没有发挥出协程的优势，没有解决回调地狱的问题
            val result = baiduRequest()
            launch(Dispatchers.Main) {
                mBinding.tvContent.text = result.body?.string()
                Log.e(TAG, "requestBaidu测: ${Thread.currentThread().name}")
            }
            Log.e(
                TAG, "requestBaidu里: ${Thread.currentThread().name}\n" +
                        "result：${result.peekBody(1024).string()}"
            )
        }
        Log.e(TAG, "requestBaidu外: ${Thread.currentThread().name}")
        //结果：
        //requestBaidu外: main
        //requestBaidu里: DefaultDispatcher-worker-1  result：<!DOCTYPE html>...(已拿到请求结果)
        //requestBaidu测: main
    }

    /**
     * suspend: 挂起函数，并且这个挂起(挂起的是协程,也就是launch里面的代码块)是非阻塞式的，他不会阻挡你的线程
     * 所谓的挂起，其实就是一个稍后会被自动切回来(切回来的动作在协程里叫做resume--恢复)的线程切换
     * suspend函数需要在协程里被调用，或者在另一个suspend函数里被调用
     *
     * suspend关键字只起到了标志这个函数是一个耗时操作，必须放在协程中执行的作用，而withContext方法则进行了线程的切换工作。
     * 挂起操作靠的是挂起函数里的实际代码，而不是关键字
     *
     * 协程中的代码自动地切换到其他线程之后又自动地切换回了主线程！ 顺序编写保证了逻辑上的直观性，协程的自动线程切换又保证了代码的非阻塞性。
     * 挂起函数必须在协程或者其他挂起函数中被调用，也就是挂起函数必须直接或者间接地在协程中执行。
     *
     * 那为什么协程中的代码没有在主线程中执行呢？而且执行完毕为什么还会自动地切回主线程呢？
     * 协程的挂起可以理解为协程中的代码离开协程所在线程的过程，不是这个协程停下来了而是协程所在的线程从这行代码开始不再运行在这个线程了，线程和协程分2波走了
     * 协程的恢复可以理解为协程中的代码重新进入协程所在线程的过程。协程就是通过的这个挂起恢复机制进行线程的切换。
     */
    /**
     * 使用协程的场景：当你需要切线程或者指定线程的时候
     * launch():里面写上代码就能切换线程，它的含义是：我要创建一个新的协程，并在指定线程上运行它。被创建和运行的协程-->就是传给launch()的那些代码
     * Dispatchers():
     * withContext():挂起函数，指定线程来执行代码，并且在执行完成后，自动把线程切换回来继续执行; 这是一个suspend函数,它需要在协程里被调用，或者在另一个suspend函数里被调用
     */
    fun requestBaidu2() {
        //方式二
        GlobalScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                Log.e(
                    TAG,
                    "requestBaidu2-->A: Thread:${Thread.currentThread().name}\nTime:${System.currentTimeMillis()}"
                )
                baiduRequest()
//                Log.e(TAG, "requestBaidu2-->A: AfterRequest")
            }
            Log.e(
                TAG,
                "requestBaidu2-->B: Thread:${Thread.currentThread().name}\nTime:${System.currentTimeMillis()}"
            )
            Log.e(TAG, "requestBaidu2: Result:${result}")
//            Log.e(TAG, "requestBaidu2: Result:${result.body?.string()}")//这样会崩溃，原因不知道
            mBinding.tvContent.text = result.body?.string() ?: "加载失败"
        }
        //结果：
        //requestBaidu2-->A: Thread:DefaultDispatcher-worker-1   Time:1668769605619
        //requestBaidu2-->B: Thread:main   Time:1668769605813
        //requestBaidu2: Result:Response{protocol=http/1.1, code=200, message=OK, url=http://www.baidu.com/}
    }

    fun requestBaidu3() {
        //方式三
        val async: Deferred<Unit> = GlobalScope.async(Dispatchers.Main) {
            val result = suspendBaiduRequest()
            mBinding.tvContent.text = result.body?.string() ?: "加载失败"
        }
    }

    suspend fun suspendBaiduRequest(): Response {
        return withContext(Dispatchers.IO) {
            baiduRequest()
        }
    }

    fun baiduRequest(): Response {
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder().url("http://www.baidu.com").get().build()
        return okHttpClient.newCall(request).execute()
    }

    //mainScope学习
    fun mainScopeDemo() {
        mainScope.launch {
            //如果使用的是Retrofit请求，它会自动识别挂起函数，切换到IO线程，
            //不需要我们自己写withContext(Dispatchers.IO)
            val result = withContext(Dispatchers.IO) {
                Log.e(TAG, "mainScopeDemo-->1: ${Thread.currentThread().name}")
                baiduRequest()
            }
            mBinding.tvContent.text = result.body?.string()
            Log.e(TAG, "mainScopeDemo-->2: ${Thread.currentThread().name}")
        }
        //结果：
        //mainScopeDemo-->1: DefaultDispatcher-worker-1
        //mainScopeDemo-->2: main
    }

    //mainScope学习2：验证协程的取消
    fun mainScopeDemo2() {
        mainScope.launch {
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

    override fun onDestroy() {
        super.onDestroy()
        //MainScope，在Activity中使用，可以在onDestroy中取消协程
        //协程在取消的时候就会抛出异常
        mainScope.cancel()
    }

    /**
     * 挂起与阻塞的区别
     * 下面两段代码放到按键点击时间中的话：
     * studyDemo()并不会导致主线程卡顿丢帧，按键也会立即正常显示点击完成回弹的效果。
     * 但是studyDemo1()就会报主线程丢帧，按键也不会显示点击完成回弹的效果，直到10秒之后。它阻塞了主线程。
     */
    fun studyDemo() {
        //如果这段代码多次触发（如被多次点击），会启动多个协程，打印多次
        GlobalScope.launch(Dispatchers.Main) {
            delay(10000)//延时10秒，是一个挂起函数
            Log.e(TAG, "studyDemo: 延时完毕;${Thread.currentThread().name}")//这行代码延时结束后才会打印，是被阻塞的
        }
        Log.e(TAG, "studyDemo: 协程之外")
        //结果:
        //22:55:06.641   studyDemo: 协程之外
        //22:55:16.654   studyDemo: 延时完毕;main
    }

    fun studyDemo1() {
        //如果这段代码多次触发（如被多次点击），会报ANR
        Thread.sleep(10000)//阻塞，放在主线程会报丢帧
        Log.e(TAG, "studyDemo1: 延时完毕;${Thread.currentThread().name}")
        //studyDemo1: 延时完毕;main
        //Choreographer: Skipped 1202 frames!  The application may be doing too much work on its main thread.
    }

    fun studyDemo2() {
        //所有的协程必须在调度器中运行，即使它们在主线程中运行也是如此。
        //如果像下面协程没有指定调度器，就默认在Dispatchers.Default中运行
        GlobalScope.launch {
            Log.e(TAG, "studyDemo2: ${Thread.currentThread().name}")
        }
        Log.e(TAG, "studyDemo2: ${Thread.currentThread().name} 线程中继续运行")
        GlobalScope.launch(Dispatchers.IO) {
            Log.e(TAG, "studyDemo2: 切换到${Thread.currentThread().name}线程了")
        }
        //结果：
        //studyDemo2: main 线程中继续运行
        //studyDemo2: DefaultDispatcher-worker-1
        //studyDemo2: 切换到DefaultDispatcher-worker-1线程了
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

