package com.jlpay.kotlindemo.ui.main.dailytest

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.base.BaseActivity
import com.jlpay.kotlindemo.ui.utils.PermissionUtils
import com.jlpay.opensdk.location.LocationManager
import com.jlpay.opensdk.location.bean.LocationData
import com.jlpay.opensdk.location.listener.LocationListener

class LibTestKotlinActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, LibTestKotlinActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getResourceId(): Int {
        return R.layout.activity_libtest_kotlin
    }

    override fun initData() {

    }

    override fun initView() {
        val tvResult: TextView = findViewById(R.id.tv_result)

        PermissionUtils.getLocationPermission(this, PermissionUtils.PermissionListener {
            LocationManager.with(this)
//                .defaultLocationType(LocationType.GAODE)
                .locationListener(object : LocationListener {
                    override fun onFail(errorCode: Int, errorMsg: String?) {
                        tvResult.text = "定位失败：$errorMsg"
                    }

                    override fun onLocation(locationData: LocationData?) {
                        tvResult.text = """定位成功：${locationData.toString()}"""
                    }
                }).startLocation()
        })
    }
}