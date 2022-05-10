package com.jlpay.kotlindemo.android_study.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

class ViewFlipperActivity : AppCompatActivity() {

    private lateinit var viewflipper: ViewFlipper

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, ViewFlipperActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_flipper)

        initView()
    }

    fun initView() {
        viewflipper = findViewById(R.id.viewflipper)
    }

    fun prev(view: View) {
        viewflipper.setInAnimation(this, R.anim.slide_in_right)
        viewflipper.setOutAnimation(this, R.anim.slide_out_left)
        viewflipper.showPrevious()//显示上一个组件
        viewflipper.stopFlipping()//停止自动播放
    }

    fun auto(view: View) {
        viewflipper.setInAnimation(this, R.anim.slide_in_right)
        viewflipper.setOutAnimation(this, R.anim.slide_out_left)
        viewflipper.startFlipping()
    }

    fun next(view: View) {
        viewflipper.setInAnimation(this, R.anim.slide_in_right)
        viewflipper.setOutAnimation(this, R.anim.slide_out_left)
        viewflipper.showNext()//显示上一个组件
        viewflipper.stopFlipping()//停止自动播放
    }
}