package com.jlpay.kotlindemo.study_android.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.widget.PlaneView

class PlaneViewActivity : AppCompatActivity() {

    private lateinit var planeView: PlaneView
    private lateinit var metrics: DisplayMetrics
    private val speed: Int = 10

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, PlaneViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //全屏显示
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        planeView = PlaneView(this)
        setContentView(planeView)
        planeView.setBackgroundResource(R.drawable.bg_white_button)
        //获取窗口管理器
        val windowManager: WindowManager = windowManager
        val display: Display = windowManager.defaultDisplay
        metrics = DisplayMetrics()
        //获取屏幕宽和高
        display.getMetrics(metrics)
        //设置飞机的初始位置
        planeView.currentX = metrics.widthPixels / 2f
        planeView.currentY = metrics.heightPixels - 200f
        planeView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (event.x < metrics.widthPixels / 8) {
                    planeView.currentX -= speed
                }
                if (event.x > metrics.widthPixels * 7 / 8) {
                    planeView.currentX += speed
                }
                if (event.y < metrics.heightPixels / 8) {
                    planeView.currentY -= speed
                }
                if (event.y > metrics.heightPixels * 7 / 8) {
                    planeView.currentY += speed
                }
                return true
            }
        })
    }
}