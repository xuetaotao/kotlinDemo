package com.jlpay.eyepetizer.ui

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.eyepetizer.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun testBlockCanary(view: View?) {
        SystemClock.sleep(2000)
    }
}
