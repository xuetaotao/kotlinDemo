package com.jlpay.kotlindemo.study_kotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

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
        multipleValues3()
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
}