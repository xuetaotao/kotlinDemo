package com.jlpay.kotlindemo.study_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.TrafficStats
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.os.PowerManager
import android.provider.Settings
import android.view.Choreographer
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

    /**
     * 电量优化
     * 添加白名单
     */
    fun batteryOptimization(context: Context) {
        val powerManager: PowerManager =
            context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //是否处于白名单
            if (!powerManager.isIgnoringBatteryOptimizations(context.packageName)) {
                //直接询问用户是否允许把我们应用加入白名单
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:" + context.packageName)
                context.startActivity(intent)
                //跳转到电量优化管理设置中
                context.startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
            }
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    fun powerManagerWake(context: Context) {
        val powerManager: PowerManager =
            context.getSystemService(Context.POWER_SERVICE) as PowerManager
        //这个还要在清单文件里申请权限 WAKE_LOCK
        val wakeLock: PowerManager.WakeLock =
            powerManager.newWakeLock(
                PowerManager.ON_AFTER_RELEASE or PowerManager.PARTIAL_WAKE_LOCK,
                "aa"
            )
        wakeLock.acquire()
        //do something
        wakeLock.release()
    }

    /**
     * FPS卡顿检测指标
     * https://github.com/Tencent/matrix/wiki/Matrix-Android-TraceCanary
     */
    fun fpsDemo() {
        val choreographer: Choreographer = Choreographer.getInstance()
        var mLastFrameTimeNanos: Long = 0
        val frameCallback: Choreographer.FrameCallback = object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                if (frameTimeNanos - mLastFrameTimeNanos > 100) {
                    //...
                }
                mLastFrameTimeNanos = frameTimeNanos;
                choreographer.postFrameCallback { this }
            }
        }
        choreographer.postFrameCallback { frameCallback }
    }

    /**
     * 流量消耗监控（并非网络监控）
     * （某个时间段用户消耗了多少流量）
     * https://blog.csdn.net/u012587637/article/details/51833253
     */
    fun flowDemo() {
        val uid: Int = 1
        //获取某个进程的接受字节数
        val uidRxBytes: Long = TrafficStats.getUidRxBytes(uid)
        //获取总的接受字节数，包含Mobile和WiFi等
        val totalRxBytes = TrafficStats.getTotalRxBytes()
        //总的发送字节数，包含Mobile和WiFi等
        val totalTxBytes = TrafficStats.getTotalTxBytes()
    }

    /**
     * 内存
     * 线下系统API：Debug.MemoryInfo的使用 获取各项指标
     * https://juejin.cn/post/6872151038305140744
     */
    fun debugMemInfoDemo() {
        val memoryInfo: Debug.MemoryInfo = Debug.MemoryInfo()
        Debug.getMemoryInfo(memoryInfo)
        //shl(bits) – 左移位 (Java’s <<)
        //shr(bits) – 右移位 (Java’s >>)
        //native内存
        val appNativePss = memoryInfo.nativePss shr 10
        //java内存 OOM原因
        val appDalvikPss = memoryInfo.dalvikPss shr 10
        //整体内存，native+dalvik+共享
        val appTotalPss = memoryInfo.totalPss shr 10
    }

    inner class OnClickProxy {
        fun performanceClick(view: View) {
            Toast.makeText(this@PerformanceActivity, "hhh", Toast.LENGTH_SHORT).show()
        }
    }
}