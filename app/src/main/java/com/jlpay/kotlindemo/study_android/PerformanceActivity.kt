package com.jlpay.kotlindemo.study_android

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityPerformanceBinding

/**
 * 性能优化部分，几个重要部分
 * 1.内存泄漏   LeakCanaryActivity
 * 2.卡顿监测   BlockCanaryActivity
 * 3.启动优化   没写，DAG
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


    inner class OnClickProxy {
        fun performanceClick(view: View) {
            Toast.makeText(this@PerformanceActivity, "hhh", Toast.LENGTH_SHORT).show()
        }
    }
}