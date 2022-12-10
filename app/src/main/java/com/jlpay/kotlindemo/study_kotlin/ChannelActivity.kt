package com.jlpay.kotlindemo.study_kotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.atomic.AtomicInteger

/**
 * 热数据通道Channel学习
 * Channel实际上是一个并发安全的队列，它可以用来连接协程，实现不同协程的通信。
 */
class ChannelActivity : AppCompatActivity() {

    public val TAG: String = ChannelActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)
    }

    fun onClickTest(view: View) {
//        channelDemo9()
//        selectDemo5()
        concurrentSafeDemo5()
    }

    //Channel实际上是一个并发安全的队列，它可以用来连接协程，实现不同协程的通信。
    fun channelDemo() = runBlocking {
        //这里创建的Channel默认容量大小是0，也就是不缓存
        val channel: Channel<Int> = Channel<Int>()
        //生产者
        val producer = GlobalScope.launch {
            var i = 0
            while (true) {
                delay(1000)
                channel.send(++i)
                Log.e(TAG, "channelDemo: send $i\t${Thread.currentThread().name}")
            }
        }
        //消费者
        val consumer = GlobalScope.launch {
            while (true) {
                //Channel实际上是一个并发安全的队列，队列中一定存在缓冲区，当缓冲区满了，
                //并且也一直没有人调用receive函数取走数据，send函数就需要挂起，直到receive执行之后才会继续往下执行
                //delay(2000)//测试二，加上这个后还是同样的结果，发送一个消费一个
                val element = channel.receive()
                Log.e(TAG, "channelDemo: receive $element\t${Thread.currentThread().name}")
            }
        }
        joinAll(producer, consumer)
        //结果：摘取部分
        //channelDemo: send 1	DefaultDispatcher-worker-1
        //channelDemo: receive 1	DefaultDispatcher-worker-2
        //channelDemo: send 2	DefaultDispatcher-worker-1
        //channelDemo: receive 2	DefaultDispatcher-worker-2
        //channelDemo: send 3	DefaultDispatcher-worker-1
        //channelDemo: receive 3	DefaultDispatcher-worker-2
    }

    //迭代Channel
    //Channel本身确实像序列，所以我们在读取的时候可以直接获取一个Channel的iterator。
    fun channelDemo2() = runBlocking {
        val channel = Channel<Int>(Channel.UNLIMITED)
        //生产者
        val producer = GlobalScope.launch {
            for (i in 1..5) {
                channel.send(i * i)
                Log.e(TAG, "channelDemo2: send ${i * i}\t${Thread.currentThread().name}")
            }
        }
        //消费者
        val consumer = GlobalScope.launch {
            //遍历方式一
            val iterator = channel.iterator()
            while (iterator.hasNext()) {
                val element = iterator.next()
                Log.e(TAG, "channelDemo2: receive $element\t${Thread.currentThread().name}")
                delay(2000)
            }
            //遍历方式二
//            for (element in channel){
//                Log.e(TAG, "channelDemo2: receive $element\t${Thread.currentThread().name}")
//                delay(2000)
//            }
        }
        joinAll(producer, consumer)
        //结果：一次性将数据全部发送，然后每隔2秒接收一个数据
        //channelDemo2: send 1	DefaultDispatcher-worker-1
        //channelDemo2: send 4	DefaultDispatcher-worker-1
        //channelDemo2: receive 1	DefaultDispatcher-worker-2
        //channelDemo2: send 9	DefaultDispatcher-worker-1
        //channelDemo2: send 16	DefaultDispatcher-worker-1
        //channelDemo2: send 25	DefaultDispatcher-worker-1
        //channelDemo2: receive 4	DefaultDispatcher-worker-1
        //channelDemo2: receive 9	DefaultDispatcher-worker-1
        //channelDemo2: receive 16	DefaultDispatcher-worker-1
        //channelDemo2: receive 25	DefaultDispatcher-worker-1
    }

    //produce与actor
    //构造生产者与消费者的便捷方法
    //可以通过produce方法启动一个生产者协程，并返回一个ReceiveChannel，
    //其他协程就可以使用这个ReceiveChannel来接收数据了。
    //反过来我们也可以用actor启动一个消费者协程。
    @OptIn(ExperimentalCoroutinesApi::class)
    fun channelDemo3() = runBlocking {
        //启动一个生产者协程，并返回ReceiveChannel
        val receiveChannel: ReceiveChannel<Int> = GlobalScope.produce {
            repeat(100) {
                delay(1000)
                send(it)
            }
        }
        val consumer = GlobalScope.launch {
            for (i in receiveChannel) {
                Log.e(TAG, "channelDemo3: $i")
            }
        }
        consumer.join()
        //结果：
        //channelDemo3: 0
        //channelDemo3: 1
        //channelDemo3: 2
        // .....后续省略
    }

    //用actor启动一个消费者协程
    @OptIn(ObsoleteCoroutinesApi::class)
    fun channelDemo4() = runBlocking {
        val sendChannel: SendChannel<Int> = GlobalScope.actor {
            while (true) {
                val element = receive()
                Log.e(TAG, "channelDemo4: $element")
            }
        }
        val producer = GlobalScope.launch {
            for (i in 0..3) {
                sendChannel.send(i)
            }
        }
        producer.join()
        //结果：
        //channelDemo4: 0
        //channelDemo4: 1
        //channelDemo4: 2
        //channelDemo4: 3
    }

    //Channel的关闭
    //produce和actor返回的Channel都会随着对应的协程执行完毕而关闭，也正是这样，Channel才被称为热数据流
    //对于一个Channel，如果我们调用了它的close方法，它会立即停止接收新元素，也就是说这时它的isClosedForSend会立即返回true。
    //而由于Channel缓冲区的存在，这时候可能还有一些元素没有被处理完，因此要等所有的元素都被读取之后isClosedForReceive才会返回true。
    //Channel的生命周期最好由主导方来维护，建议由主导的一方实现关闭
    fun channelDemo5() = runBlocking {
        val channel = Channel<Int>(3)
        //生产者
        val producer = GlobalScope.launch {
            List(3) {
                channel.send(it)
                Log.e(TAG, "channelDemo5: send $it")
            }
            channel.close()
            Log.e(
                TAG,
                "channelDemo5: Close channel.\n" +
                        "ClosedForSend:${channel.isClosedForSend}\n" +
                        "ClosedForReceive:${channel.isClosedForReceive}".trimMargin()
            )
        }
        //消费者
        val consumer = GlobalScope.launch {
            for (element in channel) {
                Log.e(TAG, "channelDemo5: receive $element")
                delay(1000)
            }
            Log.e(
                TAG, "channelDemo5: After consuming.\n" +
                        "ClosedForSend:${channel.isClosedForSend}\n" +
                        "ClosedForReceive:${channel.isClosedForReceive}".trimMargin()
            )
        }
        joinAll(producer, consumer)
        //结果：
        //channelDemo5: send 0
        //channelDemo5: send 1
        //channelDemo5: send 2
        //channelDemo5: receive 0
        //channelDemo5: Close channel.
        //ClosedForSend:true
        //ClosedForReceive:false
        //channelDemo5: receive 1
        //channelDemo5: receive 2
        //channelDemo5: After consuming.
        //ClosedForSend:true
        //ClosedForReceive:true
    }

    //BroadcastChannel
    //前面提到，发送端和接收端在Channel中存在一对多的情形，从数据处理本身来讲，虽然有多个接收端，
    //但是同一个元素只会被一个接收端读到。广播则不然多个接收端不存在互斥行为。
    @OptIn(ExperimentalCoroutinesApi::class)
    fun channelDemo6() = runBlocking {
        //BroadcastChannel创建方式一
        val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)
        //BroadcastChannel创建方式二
//        val channel = Channel<Int>()
//        val broadcastChannel = channel.broadcast(3)
        val producer = GlobalScope.launch {
            List(3) {
                delay(100)
                broadcastChannel.send(it)
            }
            broadcastChannel.close()
        }
        //开启三个子协程，都能收到所有的数据
        List(3) {
            GlobalScope.launch {
                val receiveChannel = broadcastChannel.openSubscription()
                for (i in receiveChannel) {
                    Log.e(TAG, "channelDemo6: 第${it}个协程received $i")
                }
            }
        }.joinAll()
        //结果：
        //channelDemo6: 第0个协程received 0
        //channelDemo6: 第2个协程received 0
        //channelDemo6: 第1个协程received 0
        //channelDemo6: 第0个协程received 1
        //channelDemo6: 第1个协程received 1
        //channelDemo6: 第2个协程received 1
        //channelDemo6: 第0个协程received 2
        //channelDemo6: 第2个协程received 2
        //channelDemo6: 第1个协程received 2
    }

    //多路复用select
    //数据通信系统或计算机网络系统中，传输媒体的带宽或容量往往会大于传输单一信号的需求，
    //为了有效地利用通信线路，希望一个信道同时传输多路信号，这就是所谓的多路复用技术(Multiplexing)
    //复用多个await
    //两个API分别从网络和本地缓存获取数据，期望哪个先返回就先用哪个做展示。
    fun selectDemo() = runBlocking {
        GlobalScope.launch {
            val localRequest = getInfoFromLocal("狗蛋蛋")
            val netRequest = getInfoFromNet("帅爸爸")
            val infoResponse = select<Response<String>> {
                //onAwait最终会调用await获取结果
                localRequest.onAwait { Response(it, true) }
                netRequest.onAwait { Response(it, false) }
            }
            infoResponse.value.let {
                Log.e(TAG, "channelDemo7: result is $it")
            }
        }.join()
        //结果:
        //channelDemo7: result is this is 狗蛋蛋 from local
        //或者：
        //channelDemo7: result is this is 帅爸爸 from Internet
    }

    data class Response<T>(val value: T, val isLocal: Boolean)

    final fun CoroutineScope.getInfoFromLocal(name: String) = async(Dispatchers.IO) {
        delay(3000)
        "this is $name from local"
    }

    fun CoroutineScope.getInfoFromNet(name: String) = async(Dispatchers.IO) {
        delay(2000)
        "this is $name from Internet"
    }

    //复用多个Channel
    //跟await类似，会接收到最快的那个Channel消息
    fun selectDemo2() = runBlocking {
        val channels = listOf<Channel<Int>>(Channel<Int>(), Channel<Int>())
        GlobalScope.launch {
            delay(100)
            channels[0].send(200)
        }
        GlobalScope.launch {
            delay(50)
            channels[1].send(100)
        }
        val result = select<Int> {
            channels.forEach { value ->
                value.onReceive { it }
            }
        }
        Log.e(TAG, "channelDemo8: $result")
        //结果：
        //channelDemo8: 100
    }

    //SelectClause
    //我们怎么知道哪些事件可以被select呢? 其实所有能够被select的事件都是SelectClauseN类型，包括:
    //SelectClause0: 对应事件没有返回值，例如join没有返回值，那么onJoin就是SelectClauseN类型。
    //使用时onJoin的参数是一个无参函数
    //SelectClause1: 对应事件有返回值，前面的onAwait和onReceive都是此类情况
    //SelectClause2: 对应事件有返回值，此外还需要一个额外的参数，例如Channel.onSend有两个参数，
    //第一个是Channel数据类型的值，表示即将发送的值;第二个是发送成功时的回调参数。
    //如果我们想要确认挂起函数是否支持select，只需要查看其是否存在对应的SelectClauseN类型可回调即可。
    fun selectDemo3() = runBlocking {
        val job1 = GlobalScope.launch {
            delay(100)
            Log.e(TAG, "selectDemo3: job1")
        }
        val job2 = GlobalScope.launch {
            delay(10)
            Log.e(TAG, "selectDemo3: job2")
        }
        select<Unit> {
            job1.onJoin { Log.e(TAG, "selectDemo3: job1 onJoin") }
            job2.onJoin { Log.e(TAG, "selectDemo3: job2 onJoin") }
        }
        delay(1000)
        //结果：
        //selectDemo3: job2
        //selectDemo3: job2 onJoin
        //selectDemo3: job1
    }

    //SelectClause
    fun selectDemo4() = runBlocking {
        val channels = listOf<Channel<Int>>(Channel<Int>(), Channel<Int>())
        Log.e(TAG, "selectDemo4: $channels")
        launch(Dispatchers.IO) {
            select {
                launch {
                    delay(10)
                    channels[1].onSend(200) { sendChannel ->
                        Log.e(TAG, "selectDemo4: send on $sendChannel")
                    }
                }
                launch {
                    delay(100)
                    channels[0].onSend(100) { sendChannel ->
                        Log.e(TAG, "selectDemo4: send on $sendChannel")
                    }
                }
            }
        }
        GlobalScope.launch {
            Log.e(TAG, "selectDemo4: ${channels[0].receive()}")
        }
        GlobalScope.launch {
            Log.e(TAG, "selectDemo4: ${channels[1].receive()}")
        }
        delay(1000)
        //结果：
        //selectDemo4: [RendezvousChannel@3cdc26c{EmptyQueue}, RendezvousChannel@b60e035{EmptyQueue}]
        //selectDemo4: 200
        //selectDemo4: send on RendezvousChannel@b60e035{EmptyQueue}
    }

    //使用Flow实现多路复用的效果
    //多数情况下，我们可以通过构造合适的Flow来实现多路复用的效果
    @OptIn(ExperimentalCoroutinesApi::class)
    fun selectDemo5() = runBlocking {
        val name = "Guest"
        coroutineScope {
            listOf(::getInfoFromLocalTwo, ::getInfoFromNetTwo)
                .map { function ->
                    function.invoke(name)
//                    function.call(name)//需要用到 kotlin-reflect 的库，使用老师讲的这个会崩溃，原因未知
                }
                .map { deferred ->
                    flow { emit(deferred.await()) }
                }
                .merge()
                .collect { user ->
                    Log.e(TAG, "selectDemo5: $user")
                }
        }
        //结果：
        //selectDemo5: this is Guest from Internet
        //selectDemo5: this is Guest from local
    }

    suspend fun getInfoFromLocalTwo(name: String) = GlobalScope.async(Dispatchers.IO) {
        delay(3000)
        "this is $name from local"
    }

    suspend fun getInfoFromNetTwo(name: String) = GlobalScope.async(Dispatchers.IO) {
        delay(2000)
        "this is $name from Internet"
    }

    //协程并发安全
    //下面这个Demo就显露出了协程的并发安全问题，count并不总是1000
    fun concurrentSafeDemo() = runBlocking {
        var count = 0
        List(1000) {
            GlobalScope.launch { count++ }
        }.joinAll()
        Log.e(TAG, "concurrentSafeDemo: $count")
        //结果：
        //concurrentSafeDemo: 1000
        //concurrentSafeDemo: 993
        //concurrentSafeDemo: 997
    }

    //使用Java的工具解决问题
    fun concurrentSafeDemo2() = runBlocking {
        val count = AtomicInteger(0)
        List(1000) {
            GlobalScope.launch {
                //++count
                count.incrementAndGet()
            }
        }.joinAll()
        Log.e(TAG, "concurrentSafeDemo2: $count")
        //结果：
        //concurrentSafeDemo2: 1000
    }

    //协程的并发工具
    //除了我们在线程中常用的解决并发问题的手段之外，协程框架也提供了一些并发安全的工具，包括:
    //Channel: 并发安全的消息通道，我们已经非常熟悉
    //Mutex: 轻量级锁，它的lck和unlock从语义上与线程锁比较类似，之所以轻量是因为它在获取不到锁时不会阻塞线程，
    // 而是挂起等待锁的释放。
    //Semaphore: 轻量级信号量，信号量可以有多个，协程在获取到信号量后即可执行并发操作。
    //当Semaphore的参数为1时，效果等价于Mutex。
    fun concurrentSafeDemo3() = runBlocking {
        var count = 0
        val mutex: Mutex = Mutex()
        List(1000) {
            GlobalScope.launch {
                mutex.withLock { count++ }
            }
        }.joinAll()
        Log.e(TAG, "concurrentSafeDemo3: $count")
        //结果：
        //concurrentSafeDemo3: 1000
    }

    //协程的并发工具
    //Semaphore: 轻量级信号量
    fun concurrentSafeDemo4() = runBlocking {
        var count = 0
        val semaphore: Semaphore = Semaphore(1)
        List(1000) {
            GlobalScope.launch {
                semaphore.withPermit { count++ }
            }
        }.joinAll()
        Log.e(TAG, "concurrentSafeDemo4: $count")
        //结果：
        //concurrentSafeDemo4: 1000
    }

    //协程的并发工具
    //避免访问外部可变状态(就是避免并发)，编写函数时要求它不得访问外部状态，只能基于参数做运算，通过返回值提供运算结果。
    fun concurrentSafeDemo5() = runBlocking {
        val count = 0
        val result = count + List(1000) {
            GlobalScope.async { 1 }
        }.map { it.await() }.sum()
        Log.e(TAG, "concurrentSafeDemo5: $result")
        //结果：
        //concurrentSafeDemo5: 1000
    }
}