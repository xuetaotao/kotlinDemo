package com.jlpay.kotlindemo.jetpack_study.mvcadvance

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.LinearLayout
import com.jlpay.kotlindemo.R

class MvcAdvanceView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private lateinit var et_name: EditText
    private lateinit var et_password: EditText

    override fun onFinishInflate() {
        super.onFinishInflate()
        et_name = findViewById(R.id.et_name)
        et_password = findViewById(R.id.et_password)
    }

    fun showData(data: List<String>) {
        et_name.setText(data[0])
        et_password.setText(data[1])
    }
}