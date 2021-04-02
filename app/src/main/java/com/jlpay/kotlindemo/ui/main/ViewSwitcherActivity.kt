package com.jlpay.kotlindemo.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.bean.DataItem

class ViewSwitcherActivity : AppCompatActivity() {

    private lateinit var viewSwitcher: ViewSwitcher
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button

    private var dataItems: List<DataItem> = ArrayList<DataItem>()

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, ViewSwitcherActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_switch)

        initView()
        initData()
    }

    private fun initView() {
        viewSwitcher = findViewById(R.id.viewSwitcher)
        btnPrev = findViewById(R.id.btn_prev)
        btnNext = findViewById(R.id.btn_next)

        val baseAdapter: BaseAdapter = object : BaseAdapter() {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                TODO("Not yet implemented")
            }

            override fun getItem(position: Int): Any {
                TODO()
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getCount(): Int {
                TODO("Not yet implemented")
            }
        }
    }

    private fun initData() {
        for (i in 0 until 40) {//[0,40)

        }
    }

    fun prev(view: View) {

    }

    fun next(view: View) {

    }
}