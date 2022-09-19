package com.jlpay.kotlindemo.study_library.blockcanary

import android.content.Context
import android.os.Bundle
import android.os.Debug
import android.os.Looper
import android.os.Trace
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.Printer
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityBlockcanaryBinding

/**
 * 性能优化之卡顿的监测
 */
class BlockCanaryActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityBlockcanaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_blockcanary)
        mBinding.click = OnClickProxy()
        mBinding.lifecycleOwner = this
    }

    /**
     * 线下监测卡顿工具TraceView
     * TraceView的第二种用法：埋点
     */
    fun traceViewDemo() {
        //如果是做启动优化监测
        //就在Application的onCreate()中调下面方法
        Debug.startMethodTracing()
//        Debug.startMethodTracingSampling()//这个方法也可以
        //doSomething
    }

    /**
     * 线下监测卡顿工具systrace
     * 下面两句代码放在对应的地方，就可以做启动优化
     */
    fun systraceDemo() {
        //在Application的attachBaseContext()方法中调这个方法
        Trace.beginSection("aa")
        //在MainActivity的onWindowFocusChanged()中调
        Trace.endSection()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //在MainActivity的onWindowFocusChanged()中调
        Debug.stopMethodTracing()
    }

    /**
     * blockCanary原理
     * 在Android中，应用的卡顿，主要是在主线程阻塞导致的。Looper是主线程的消息调度者，所以以它为突破点。
     * 在Looper的loop方法中，有一个Printer，它在每个Message处理的前后被调用，而如果主线程卡住了，
     * 就是dispatchMessage里卡住了。
     */
    fun blockCanaryTheory() {
        //第一步：获取主线程的Looper。
        //因为Looper在每个线程最多只有一个实例，所以只要获取到主线程的Looper，就可以设置一个自定义的Printer对象到里面。
        val mainLooper: Looper = Looper.getMainLooper();
        //第二步：创建自定义Printer
        val customerPrinter = object : CustomerPrinter() {
            override fun notifyBlockEvent(startMsgTime: Long, finishMsgTime: Long) {
                Toast.makeText(this@BlockCanaryActivity, "卡顿了", Toast.LENGTH_SHORT).show()
            }
        }
        //第三步：设置自定义Printer到主线程Looper
        mainLooper.setMessageLogging(customerPrinter)
    }

    /**
     * 测试XML文件中每个View创建的耗时时间
     */
    fun setLayoutInflaterFactory2() {
        //setFactory2这个方法一定要在super.onCreate(savedInstanceState)和setContentView()之前进行处理
        //前者方法里面会setFactory2
        //另外就是这个方法在DataBinding的setContentView()前使用有点问题，所以正常使用写在LeakCanaryActivity里
        LayoutInflaterCompat.setFactory2(layoutInflater, object : LayoutInflater.Factory2 {
            //这个方法每次由XML文件创建View都会被回调
            override fun onCreateView(
                parent: View?,
                name: String,
                context: Context,
                attrs: AttributeSet
            ): View? {
                //name是每个控件的名字
                if (TextUtils.equals(name, "Button")) {
                    //如果是Button控件，在这里可以进行自定义View的替换，类似于下面的方法
                    //androidx.appcompat.app.AppCompatViewInflater.createView(...)
                }
                val startTime: Long = System.currentTimeMillis()
                val createView: View = delegate.createView(parent, name, context, attrs)
                val stopTime: Long = System.currentTimeMillis()
                Log.e("TAG", "onCreateView: " + name + "cost " + "${stopTime - startTime}" + "ms")
                return createView
            }

            override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                return null
            }
        })
    }

    inner class OnClickProxy {
        fun blockCanaryClick(view: View) {
//            BlockCanaryContext
            Toast.makeText(this@BlockCanaryActivity, "hhh", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 创建自定义Printer
     * 在Printer的println方法去计算主线程一条Message处理的时长，当时长超过设定的阈值时就判定是卡顿了。
     */
    open class CustomerPrinter : Printer {

        val blockTime: Long = 5 * 1000;//ms
        var startMsgTime: Long = 0
        var finishMsgTime: Long = 0

        override fun println(x: String?) {
            if (x?.contains(">>>>> Dispatching to ") == true) {
                //这是开始
                startMsgTime = System.currentTimeMillis();
            }
            if (x?.contains("<<<<< Finished to ") == true) {
                //这是结束
                finishMsgTime = System.currentTimeMillis();
                if (isBlock()) {
                    //如果超时，搞个回调调出去
                    notifyBlockEvent(startMsgTime, finishMsgTime)
                }
            }
        }

        private fun isBlock(): Boolean {
            return finishMsgTime - startMsgTime > blockTime
        }

        open fun notifyBlockEvent(startMsgTime: Long, finishMsgTime: Long) {

        }
    }
}