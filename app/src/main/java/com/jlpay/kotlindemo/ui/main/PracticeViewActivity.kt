package com.jlpay.kotlindemo.ui.main

import android.content.Context
import android.content.Intent
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.base.BaseActivity

class PracticeViewActivity : BaseActivity() {

    companion object {
        @JvmStatic
        public fun newInstance(context: Context) {
            val intent = Intent(context, PracticeViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getResourceId(): Int {
        return R.layout.activity_practice_view
    }

    override fun initView() {
    }

    override fun initData() {
    }
}