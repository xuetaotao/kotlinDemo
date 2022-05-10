package com.jlpay.kotlindemo.android_study.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

class FirstUiActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        public fun newInstance(context: Context) {
            val intent = Intent(context, FirstUiActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //用编程的方式开发UI界面
        val linearLayout = LinearLayout(this)
        super.setContentView(linearLayout)
        linearLayout.orientation = LinearLayout.VERTICAL
        val textView = TextView(this)
        val button = Button(this)
        button.setText(R.string.confirm)
        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        button.layoutParams = layoutParams
        linearLayout.addView(textView)
        linearLayout.addView(button)
        button.setOnClickListener { textView.setText(R.string.app_name) }
    }
}