package com.jlpay.kotlindemo.study_kotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

/**
 * Flow-异步流学习
 */
class FlowActivity : AppCompatActivity() {

    private val TAG = FlowActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)
    }

    fun onClickTest(view: View) {
//        multipleValues3()
        flowDemo17()
    }

    //使用集合返回多个值，但不是异步
    fun multipleValues() {
        val list: List<Int> = listOf(1, 2, 3)
        list.forEach {
            Log.e(TAG, "current value: $it")
        }
        //结果:
        //current value: 1
        //current value: 2
        //current value: 3
    }

    //使用序列返回多个值，但不是异步
    fun multipleValues2() {
        val sequence: Sequence<Int> = sequence {
            for (i in 1..3) {
                Thread.sleep(1000)//阻塞，假装在计算，每隔1s打印一个值
//                delay(1000)//Sequence中不可以使用其他挂起函数，原因看源码
                yield(i)
            }
        }
        sequence.forEach {
            Log.e(TAG, "current value2: $it")
        }
        //结果：
        //22:15:45  current value2: 1
        //22:15:46  current value2: 2
        //22:15:47  current value2: 3
    }

    suspend fun simpleList(): List<Int> {
        delay(1000)
        return listOf(1, 2, 3)
    }

    //返回了多个值，是异步，但是是一次性返回了多个值，
    // 但是我们要的是2上面的效果，一次给一个值，还是没达到效果
    fun multipleValues3() = runBlocking {
        simpleList().forEach {
            Log.e(TAG, "multipleValues3: $it")
        }
        //结果:
        //22:21:28  multipleValues3: 1
        //22:21:28  multipleValues3: 2
        //22:21:28  multipleValues3: 3
    }


    //返回多个值，而且是异步的，一次给一个值那种
    //Flow与其他方式的区别
    //通过名为flow的Flow类型构建器函数构建Flow对象
    //flow{...}构建块中的代码可以挂起
    //包裹flow{...}的函数可以不再标有suspend修饰符
    //流使用emit函数发射值
    //流使用collect函数收集值
    fun flowDemo() = runBlocking {
        launch {
            for (k in 1..3) {
                Log.e(TAG, "flowDemo-->A: I'm not blocked \t${Thread.currentThread().name}")
                delay(1500)
            }
        }
        val flow: Flow<Int> = flow<Int> {
            for (i in 1..3) {
                delay(1000)
                Log.e(TAG, "flowDemo-->B: ${Thread.currentThread().name}")
                emit(i)
            }
        }
        flow.collect {
            Log.e(TAG, "flowDemo-->C: $it\t${Thread.currentThread().name}")
        }
        //结果：
        //flowDemo-->A: I'm not blocked 	main
        //flowDemo-->B: main
        //flowDemo-->C: 1	main
        //flowDemo-->A: I'm not blocked 	main
        //flowDemo-->B: main
        //flowDemo-->C: 2	main
        //flowDemo-->A: I'm not blocked 	main
        //flowDemo-->B: main
        //flowDemo-->C: 3	main
    }

    //冷流
    //Flow是一种类似于序列的冷流，flow构建器中的代码直到流被收集的时候才运行。
    //和RxJava一样，只有subscribe触发了才会执行Observable中的代码
    //也有点像lazy启动，只有调用了collect之后才会去发射元素
    fun flowDemo2() = runBlocking {
        val flow: Flow<Int> = flow {
            Log.e(TAG, "flowDemo2-->A: Flow started\t${Thread.currentThread().name}")
            for (i in 1..3) {
                delay(1000)
                emit(i)
            }
        }
        Log.e(TAG, "Calling collect")
        //从结果看出，调用了collect之后flow构建器中的代码才执行
        flow.collect {
            Log.e(TAG, "flowDemo2-->B: value is $it")
        }
        Log.e(TAG, "Calling collect again")
        flow.collect {
            Log.e(TAG, "flowDemo2-->C: value is $it")
        }
        //结果：
        //Calling collect
        //flowDemo2-->A: Flow started	main
        //flowDemo2-->B: value is 1
        //flowDemo2-->B: value is 2
        //flowDemo2-->B: value is 3
        //Calling collect again
        //flowDemo2-->A: Flow started	main
        //flowDemo2-->C: value is 1
        //flowDemo2-->C: value is 2
        //flowDemo2-->C: value is 3
    }

    //流的连续性
    //流的每次单独收集都是按顺序执行的，除非使用特殊操作符
    //从上游到下游每个过渡操作符都会处理每个发射出的值，然后再交给末端操作符
    fun flowDemo3() = runBlocking {
        (1..5).asFlow().filter {
            it % 2 == 0
        }.map {
            "String is $it"
        }.collect {
            Log.e(TAG, "flowDemo3: collect $it")
        }
        //结果：
        //flowDemo3: collect String is 2
        //flowDemo3: collect String is 4
    }

    //流构建器
    //flow(...)构建器
    //flowOf构建器定义了一个发射固定值集的流
    //使用.asFlow()扩展函数，可以将各种集合与序列转化为流
    fun flowDemo4() = runBlocking {
        //flowOf构建器定义了一个发射固定值集的流
        //每隔1s打印一个元素
        flowOf("one", "two", "three")
            .onEach { delay(1000) }
            .collect {
                Log.e(TAG, "flowDemo4: flowOf collect $it")
            }
        //使用.asFlow()扩展函数，可以将各种集合与序列转化为流
        (1..3).asFlow().collect {
            Log.e(TAG, "flowDemo4: asFlow collect $it")
        }
        //flow(...)构建器
        flow {
            for (i in 1..3) {
                emit(i)
            }
        }.collect {
            Log.e(TAG, "flowDemo4: flow collect $it")
        }
        //结果:
        //flowOf collect one
        //flowOf collect two
        //flowOf collect three
        //asFlow collect 1
        //asFlow collect 2
        //asFlow collect 3
        //flow collect 1
        //flow collect 2
        //flow collect 3
    }

    //流上下文
    //流的收集总是在调用协程的上下文中发生，流的该属性称为上下文保存(例flowDemo()方法构建和收集线程一样)
    //flow{...}构建器中的代码必须遵循上下文保存属性，并且不允许从其他上下文中发射（emit）
    //flowOn操作符，该函数用于更改流发射的上下文
    fun flowDemo5() = runBlocking {
        val flow: Flow<Int> = flow {
            Log.e(TAG, "flowDemo5: Flow Started\t${Thread.currentThread().name}")
            for (i in 1..3) {
                delay(1000)
                emit(i)
            }
        }.flowOn(Dispatchers.IO)
        flow.collect {
            Log.e(TAG, "flowDemo5: Collected $it\t${Thread.currentThread().name}")
        }
        //结果：
        //flowDemo5: Flow Started	DefaultDispatcher-worker-1
        //flowDemo5: Collected 1	main
        //flowDemo5: Collected 2	main
        //flowDemo5: Collected 3	main
    }

    //在指定协程中收集流
    //使用launchIn替换collect，可以在单独的协程中启动流的收集
    fun flowDemo6() = runBlocking {
        val job = (1..3).asFlow()
            .onEach {
                delay(1000)
            }
            .flowOn(Dispatchers.Default)
            .onEach {
                Log.e(TAG, "flowDemo6: $it\t${Thread.currentThread().name}")
            }
            .launchIn(CoroutineScope(Dispatchers.IO))
//            .launchIn(this)
        job.join()
        //结果:
        //flowDemo6: 1	DefaultDispatcher-worker-1
        //flowDemo6: 2	DefaultDispatcher-worker-3
        //flowDemo6: 3	DefaultDispatcher-worker-1
    }

    //流的取消
    //流采用与协程同样的协作取消。
    //流的取消可以是当流在一个可取消的挂起函数（如delay）中挂起的时候取消
    fun flowDemo7() = runBlocking {
        val flow: Flow<Int> = flow {
            for (i in 1..3) {
                delay(1000)
                emit(i)
                Log.e(TAG, "flowDemo7: Emitting $i\t${Thread.currentThread().name}")
            }
        }
        withTimeoutOrNull(2500) {
            flow.collect {
                Log.e(TAG, "flowDemo7: value is $it\t${Thread.currentThread().name}")
            }
        }
        Log.e(TAG, "flowDemo7: Done")
        //结果:
        //flowDemo7: value is 1	main
        //flowDemo7: Emitting 1	main
        //flowDemo7: value is 2	main
        //flowDemo7: Emitting 2	main
        //flowDemo7: Done
    }

    //流的取消检测
    //为方便起见，流构建器对每个发射值执行附加的 ensureActive 检测以进行取消，这意味着从flow{...}发出的繁忙循环是可以取消的。
    fun flowDemo8() = runBlocking {
        val flow: Flow<Int> = flow {
            for (i in 1..5) {
                emit(i)//一次发射一个数，不繁忙
                Log.e(TAG, "flowDemo7: Emitting $i\t${Thread.currentThread().name}")
            }
        }
        flow.collect {
            Log.e(TAG, "flowDemo8: value is $it\t${Thread.currentThread().name}")
            if (it == 3) {
                cancel()
            }
        }
        //结果：会崩溃，但能取消成功
        // Caused by: kotlinx.coroutines.JobCancellationException: BlockingCoroutine was cancelled;
        // job=BlockingCoroutine{Cancelled}@958d9d2
        //flowDemo8: value is 1	main
        //flowDemo7: Emitting 1	main
        //flowDemo8: value is 2	main
        //flowDemo7: Emitting 2	main
        //flowDemo8: value is 3	main
        //flowDemo7: Emitting 3	main
    }

    //流的取消检测
    //出于性能原因，大多数其他流操作不会自行执行其他取消检测，在协程处于繁忙循环的情况下，必须明确检测是否取消
    //通过 cancellable 操作符来执行此操作
    fun flowDemo9() = runBlocking {
        //一次全部发射所有数，繁忙
        (1..5).asFlow().collect {
            Log.e(TAG, "flowDemo9: $it\t${Thread.currentThread().name}")
            if (it == 3) {
                cancel()//不能成功取消
            }
        }
        //结果：会崩溃，不能取消成功
        //Caused by: kotlinx.coroutines.JobCancellationException: BlockingCoroutine was cancelled;
        //job=BlockingCoroutine{Cancelled}@60b0f46
        //flowDemo9: 1	main
        //flowDemo9: 2	main
        //flowDemo9: 3	main
        //flowDemo9: 4	main
        //flowDemo9: 5	main
    }

    //流的取消检测
    //出于性能原因，大多数其他流操作不会自行执行其他取消检测，在协程处于繁忙循环的情况下，必须明确检测是否取消
    //通过 cancellable 操作符来执行此操作
    fun flowDemo10() = runBlocking {
        //一次全部发射所有数，繁忙
        (1..5).asFlow().cancellable().collect {
            Log.e(TAG, "flowDemo10: $it\t${Thread.currentThread().name}")
            if (it == 3) {
                cancel()
            }
        }
        //结果:会崩溃，但能取消成功
        //Caused by: kotlinx.coroutines.JobCancellationException: BlockingCoroutine was cancelled;
        //flowDemo10: 1	main
        //flowDemo10: 2	main
        //flowDemo10: 3	main
    }

    //背压：生产者生产效率 > 消费者消费效率，管子不够用了
    //解决方案：降低生产者生产效率，或者提高消费者消费效率
    fun flowDemo11() = runBlocking {
        val flow = flow<Int> {
            for (i in 1..3) {
                delay(100)
                emit(i)
                Log.e(TAG, "flowDemo11: Emitting $i\t${Thread.currentThread().name}")
            }
        }
        val time = measureTimeMillis {
            flow.collect {
                delay(300)
                Log.e(TAG, "flowDemo11: Collect $it\t${Thread.currentThread().name}")
            }
        }
        Log.e(TAG, "flowDemo11: cost $time ms\t${Thread.currentThread().name}")
        //结果：(100+300)*3=1200ms
        //flowDemo11: Collect 1	main
        //flowDemo11: Emitting 1	main
        //flowDemo11: Collect 2	main
        //flowDemo11: Emitting 2	main
        //flowDemo11: Collect 3	main
        //flowDemo11: Emitting 3	main
        //flowDemo11: cost 1211 ms	main
    }

    //flowDemo11优化一下
    //处理背压的方式一：加长管子，加缓冲
    //buffer：并发运行流中发射元素的代码
    fun flowDemo12() = runBlocking {
        val flow = flow<Int> {
            for (i in 1..3) {
                delay(100)
                emit(i)
                Log.e(TAG, "flowDemo11: Emitting $i\t${Thread.currentThread().name}")
            }
        }
        val time = measureTimeMillis {
            flow
                //当必须更改CoroutineDispatcher时，flowOn操作符使用了相同的缓冲机制，但是buffer函数显式地请求缓冲而不改变执行上下文
                //.flowOn(Dispatchers.Default)//并行发射的话也可以使用flowOn，更改流发射的上下文
                .buffer(50)//加缓冲，加长管子来应对背压
                .collect {
                    delay(300)
                    Log.e(TAG, "flowDemo11: Collect $it\t${Thread.currentThread().name}")
                }
        }
        Log.e(TAG, "flowDemo11: cost $time ms\t${Thread.currentThread().name}")
        //结果：100+300*3=1000
        //flowDemo11: Emitting 1	main
        //flowDemo11: Emitting 2	main
        //flowDemo11: Emitting 3	main
        //flowDemo11: Collect 1	main
        //flowDemo11: Collect 2	main
        //flowDemo11: Collect 3	main
        //flowDemo11: cost 1014 ms	main
    }

    //处理背压的方式二
    //conflate：合并发射项，不对每个值进行处理
    fun flowDemo13() = runBlocking {
        val flow = flow<Int> {
            for (i in 1..3) {
                delay(100)
                emit(i)
                Log.e(TAG, "flowDemo11: Emitting $i\t${Thread.currentThread().name}")
            }
        }
        val time = measureTimeMillis {
            flow
                .conflate()
                .collect {
                    delay(300)
                    Log.e(TAG, "flowDemo11: Collect $it\t${Thread.currentThread().name}")
                }
        }
        Log.e(TAG, "flowDemo11: cost $time ms\t${Thread.currentThread().name}")
        //结果：合并发射项，不对每个值进行处理，所以会漏掉部分发射的值
        //flowDemo11: Emitting 1	main
        //flowDemo11: Emitting 2	main
        //flowDemo11: Emitting 3	main
        //flowDemo11: Collect 1	main
        //flowDemo11: Collect 3	main
        //flowDemo11: cost 727 ms	main
    }

    //处理背压的方式三
    //collectLatest：取消并重新发射最后一个值
    fun flowDemo14() = runBlocking {
        val flow = flow<Int> {
            for (i in 1..3) {
                delay(100)
                emit(i)
                Log.e(TAG, "flowDemo11: Emitting $i\t${Thread.currentThread().name}")
            }
        }
        val time = measureTimeMillis {
            flow
                .collectLatest {
                    delay(300)
                    Log.e(TAG, "flowDemo11: Collect $it\t${Thread.currentThread().name}")
                }
        }
        Log.e(TAG, "flowDemo11: cost $time ms\t${Thread.currentThread().name}")
        //结果：只接收了最后一个值
        //flowDemo11: Emitting 1	main
        //flowDemo11: Emitting 2	main
        //flowDemo11: Emitting 3	main
        //flowDemo11: Collect 3	main
        //flowDemo11: cost 641 ms	main
    }

    //操作符
    //过渡流操作符
    //可以使用操作符转换流，就像使用集合与序列一样
    //过渡流操作符应用于上游流，并返回下游流
    //这些操作符也是冷操作符，就像流一样。这类操作符本身不是挂起函数
    //它运行的速度很快，返回新的转换流的定义
    //转换操作符
    fun flowDemo15() = runBlocking {
        Log.e(TAG, "*******map转换操作符************\n")
        (1..3).asFlow()
            .map { performRequest(it) }
            .collect { Log.e(TAG, "map转换后：$it") }
        Log.e(TAG, "*******transform转换操作符************\n")
        (1..3).asFlow()
            .transform {
                emit("post request $it")
                emit(performRequest(it))
            }
            .collect { Log.e(TAG, "transform转换后：$it") }
        //结果：
        //map转换后：response 1
        //map转换后：response 2
        //map转换后：response 3
        //transform转换后：post request 1
        //transform转换后：response 1
        //transform转换后：post request 2
        //transform转换后：response 2
        //transform转换后：post request 3
        //transform转换后：response 3
    }

    suspend fun performRequest(request: Int): String {
        delay(1000)
        return "response $request"
    }

    //限长操作符 take
    fun flowDemo16() = runBlocking {
        flow<Int> {
            try {
                emit(1)
                emit(2)
                Log.e(TAG, "flowDemo16: This line will not execute")
                emit(3)
            } catch (e: Exception) {
                Log.e(TAG, "flowDemo16: $e")
            } finally {
                Log.e(TAG, "flowDemo16: Finally in numbers")
            }
        }
            .take(2)
            .collect { Log.e(TAG, "flowDemo16: $it") }
        //结果：不会崩溃
        //flowDemo16: 1
        //flowDemo16: 2
        //flowDemo16: kotlinx.coroutines.flow.internal.AbortFlowException: Flow was aborted, no more elements needed
        //flowDemo16: Finally in numbers
    }

    //末端流操作符
    //末端操作符是在流上用于启动流收集的挂起函数。collect是最基础的末端操作符，但是
    //还有另外一些更方便使用的末端操作符：
    //1)转化为各种集合，例如toList与toSet。
    //2)获取第一个（first）值与确保流发射单个（single)值的操作符。
    //3)使用reduce与fold将流规约到单个值。
    fun flowDemo17() = runBlocking {
        //计算1到5每个数的平方和
        val sum = (1..5).asFlow()
            .map { it * it }
            .reduce { a, b -> a + b }
        Log.e(TAG, "flowDemo17: $sum")//55
    }
}