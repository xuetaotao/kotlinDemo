package com.jlpay.kotlindemo.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

/**
 * 隐藏ActionBar的两种方式：
 * 1.在AdnroidManifest中的 <application android:theme 属性修改为 NoActionBar
 * 2.代码修改控制
 */
class ActionBarActivity : AppCompatActivity() {

    //获取Activity的ActionBar
    private var actionBar: ActionBar? = null
    private lateinit var tv_test: TextView

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, ActionBarActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actionbar)

        //只有当应用没有关闭ActionBar时，该代码才能返回ActionBar
        actionBar = supportActionBar

        initView()
    }

    fun initView() {
        val btnShowactionbar: Button = findViewById(R.id.btn_showActionBar)
        btnShowactionbar.setOnClickListener {
            actionBar?.show()
        }
        val btnHideactionbar: Button = findViewById(R.id.btn_hideActionBar)
        btnHideactionbar.setOnClickListener {
            actionBar?.hide()
        }
    }


}