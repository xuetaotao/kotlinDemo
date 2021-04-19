package com.jlpay.kotlindemo.ui.main.chapter3

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast

class SendSmsListener(
    var activity: Activity,
    var address: String,
    var content: String
) : View.OnClickListener {

    override fun onClick(v: View?) {
        //获取短信管理器
        val smsManager: SmsManager = SmsManager.getDefault()
        //创建发送短信的PendingIntent
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(activity, 0, Intent(), 0)
        //发送文本短信
        smsManager.sendTextMessage(address, null, content, pendingIntent, null)
        Toast.makeText(activity, "短信发送完成", Toast.LENGTH_SHORT).show()
    }
}