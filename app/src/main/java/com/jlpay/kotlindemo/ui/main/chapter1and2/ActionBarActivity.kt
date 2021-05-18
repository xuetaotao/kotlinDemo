package com.jlpay.kotlindemo.ui.main.chapter1and2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

        //设置是否显示应用程序图标
        actionBar?.setDisplayShowHomeEnabled(true)
        //将应用程序图标设置为可点击的按钮，并在图标上添加向左的箭头
        actionBar?.setDisplayHomeAsUpEnabled(true)

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
        tv_test = findViewById(R.id.tv_test)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
//        menuInflater.inflate(R.menu.actionview_menu, menu)//在ACtionBar上添加普通的UI组件：ActionView
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.isCheckable) {
            item.isChecked = true
        }
        when (item.itemId) {
            R.id.font_10 -> tv_test.textSize = 10 * 2f
            R.id.font_12 -> tv_test.textSize = 12 * 2f
            R.id.font_14 -> tv_test.textSize = 14 * 2f
            R.id.font_16 -> tv_test.textSize = 16 * 2f
            R.id.font_18 -> tv_test.textSize = 18 * 2f
            R.id.red_font -> tv_test.setTextColor(Color.RED)
            R.id.green_font -> tv_test.setTextColor(Color.GREEN)
            R.id.blue_font -> tv_test.setTextColor(Color.BLUE)
            R.id.plain_item -> Toast.makeText(this, "common options Menu", Toast.LENGTH_SHORT)
                .show()
            android.R.id.home -> {
                val intent: Intent = Intent(this, DialogActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)//将Activity栈中处于DialogActivity之上的Activity弹出
                startActivity(intent)
            }
        }
        return true
    }
}