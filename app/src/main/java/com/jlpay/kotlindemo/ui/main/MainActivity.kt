package com.jlpay.kotlindemo.ui.main

import android.widget.Button
import android.widget.Toast
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.base.BaseMediaActivity
import com.jlpay.kotlindemo.ui.widget.CustomDialog
import okhttp3.*
import java.io.IOException

class MainActivity : BaseMediaActivity() {

    override fun getResourceId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        val btnFramelayout: Button = findViewById(R.id.btn_framelayout)
        btnFramelayout.setOnClickListener {
            FrameLayoutActitivy.newInstance(this)
//            openPhotoAlbum()
//            takePhoto()
        }
        val btnPracticeview: Button = findViewById(R.id.btn_practiceview)
        btnPracticeview.setOnClickListener {
            PracticeViewActivity.newInstance(this)
        }
    }

    override fun initData() {

    }

    fun showCustomDialog() {
        val builder: CustomDialog.Builder = CustomDialog.Builder(this@MainActivity)
        builder
            .setTitle("温馨提示")
            .setTitleVisible(true)
            .setContent("测试一下")
            .setPositiveButton(
                "确定"
            ) { dialog, which ->
                Toast.makeText(this, "好的，确定了", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton(
                "取消了"
            ) { dialog, which ->
                Toast.makeText(this, "那么，取消了", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun okHttpTest() {
        val client: OkHttpClient = OkHttpClient.Builder().build()
        val request: Request = Request.Builder()
            .url("http://www.baidu.com")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {

            }
        })
    }
}