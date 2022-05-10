package com.jlpay.kotlindemo.android_study

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

/**
 * Configuration类专门用于描述手机设备上的配置信息，这些配置信息既包括用户特定的配置项，也包括系统的动态设备配置
 */
class ConfigurationActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, ConfigurationActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        val tv_screen_orientation: TextView = findViewById(R.id.tv_screen_orientation)
        val tv_navigation: TextView = findViewById(R.id.tv_control_orientation)
        val tv_touchscreen: TextView = findViewById(R.id.tv_touch_screen)
        val tv_mnc: TextView = findViewById(R.id.tv_mcc)
        val tv_fontScale: TextView = findViewById(R.id.tv_fontScale)
        val btn_getCfg: Button = findViewById(R.id.btn_getCfg)
        btn_getCfg.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                //获取系统的Configuration对象
                val configuration = resources.configuration

                //获取系统屏幕方向
                val screenOrientation: String =
                    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        "横向屏幕"
                    } else {
                        "竖向屏幕"
                    }
                tv_screen_orientation.text = screenOrientation

                //手机方向控制设备
                val controlOrientation =
                    when (configuration.navigation) {
                        Configuration.NAVIGATIONHIDDEN_NO -> {
                            "没有方向控制"
                        }
                        Configuration.NAVIGATION_WHEEL -> {
                            "滚轮控制方向"
                        }
                        Configuration.NAVIGATION_DPAD -> {
                            "方向键控制方向"
                        }
                        else -> {
                            "轨迹球控制方向"
                        }
                    }
                tv_navigation.text = controlOrientation

                //获取系统触摸屏的触摸方式
                val touchscreen: String =
                    if (configuration.touchscreen == Configuration.TOUCHSCREEN_NOTOUCH) {
                        "支持触摸屏"
                    } else {
                        "无触摸屏"
                    }
                tv_touchscreen.text = touchscreen

                //获取移动信号的网络码
                tv_mnc.text = configuration.mnc.toString()

                //获取当前用户设置的字体的缩放因子(即系统字体大小)
                tv_fontScale.text = "${configuration.fontScale}号字体"
            }
        })

        val btn_setOrientation: Button = findViewById(R.id.btn_setOrientation)
        btn_setOrientation.setOnClickListener {

            //获取系统的Configuration对象
            val configuration = resources.configuration

            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //设为竖屏
                this@ConfigurationActivity.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                //设为横屏
                this@ConfigurationActivity.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

    //重写该方法，用于监听系统设置的更改，这里主要是监控屏幕方向的更改
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val screen: String = if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            "竖向屏幕"
        } else {
            "横向屏幕"
        }
        Toast.makeText(this, "系统的屏幕方向发生改变" + "\n改变后的屏幕方向为：${screen}", Toast.LENGTH_SHORT).show()
    }
}