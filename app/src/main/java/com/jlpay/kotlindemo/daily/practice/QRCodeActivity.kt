package com.jlpay.kotlindemo.daily.practice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.base.BaseActivity
//import com.jlpay.kotlindemo.utils.PermissionUtils
//import com.yxing.ScanCodeActivity
//import com.yxing.ScanCodeConfig
//import com.yxing.def.ScanStyle


class QRCodeActivity : BaseActivity() {

    private lateinit var tvCode: TextView
    private lateinit var btnScan: Button

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, QRCodeActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getResourceId(): Int {
        return R.layout.activity_qrcode
    }

    override fun initView() {
        tvCode = findViewById(R.id.tvCode)
        btnScan = findViewById(R.id.btn_scan)
        btnScan.setOnClickListener {
//            yxingTest()
        }
    }


    /**
     * 扫码第三方库，个人维护，不建议使用，这里仅做测试：https://github.com/amggg/YXing
     * Android 一行代码接入扫码功能 （CameraX + zxing）：
     * https://www.jianshu.com/p/c549f91cb9c5
     */
//    fun yxingTest() {
//        PermissionUtils.getCameraPermission(this, object : PermissionUtils.PermissionListener {
//            override fun allow() {
//                ScanCodeConfig.create(this@QRCodeActivity) //设置扫码页样式 ScanStyle.NONE：无  ScanStyle.QQ ：仿QQ样式   ScanStyle.WECHAT ：仿微信样式    ScanStyle.CUSTOMIZE ： 自定义样式
//                    .setStyle(ScanStyle.WECHAT) //扫码成功是否播放音效  true ： 播放   false ： 不播放
//                    .setPlayAudio(false)
//                    .buidler() //跳转扫码页   扫码页可自定义样式
//                    .start(ScanCodeActivity::class.java)
//            }
//        })
//    }

    override fun initData() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        //接收扫码结果
//        if (requestCode == ScanCodeConfig.QUESTCODE && data != null) {
//            val extras = data.extras
//            if (extras != null) {
//                val code = extras.getString(ScanCodeConfig.CODE_KEY)
//                tvCode.text = String.format("%s%s", "结果： ", code)
//            }
//        }
    }
}