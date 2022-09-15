package com.jlpay.kotlindemo.study_android

import android.os.Bundle
import android.os.Debug
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityPerformanceBinding

/**
 * 性能优化部分
 */
class PerformanceActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityPerformanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityPerformanceBinding>(
            this,
            R.layout.activity_performance
        )
        mBinding.click = OnClickProxy()
        mBinding.lifecycleOwner = this
    }

    /**
     * SystemTrace的第二种用法：埋点
     */
    fun systemTraceDemo() {
        //如果是做启动优化监测
        //就在Application的onCreate()中调下面方法
        Debug.startMethodTracing()
//        Debug.startMethodTracingSampling()//这个方法也可以
        //doSomething
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //在MainActivity的onWindowFocusChanged()中调
        Debug.stopMethodTracing()
    }

    inner class OnClickProxy {
        fun performanceClick(view: View) {
//            systemTraceDemo()
            Toast.makeText(this@PerformanceActivity, "hhh", Toast.LENGTH_SHORT).show()
        }
    }
}