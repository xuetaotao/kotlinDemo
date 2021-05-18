package com.jlpay.kotlindemo.ui.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.main.chapter1and2.PracticeViewActivity

/**
 * Android通知栏微技巧，那些你所没关注过的小细节:
 * https://blog.csdn.net/guolin_blog/article/details/50945228
 *
 * Android通知栏微技巧，8.0系统中通知栏的适配:
 * https://blog.csdn.net/guolin_blog/article/details/79854070
 *
 * 对于通知栏的使用，Android各个版本其实都有比较大的调整，新版本的通知栏API无法兼容老系统这就会是一个很头疼的问题，为此Android在
 * appcompat-v7库中提供了一个NotificationCompat类来处理新老版本的兼容问题，只是把我们平时使用的Notification.Builder改成了NotificationCompat.Builder而已，
 * 其他用法都是一模一样的，这样我们的通知就具备各种Android版本的兼容性了
 *
 * 从Android 8.0系统开始，Google引入了通知渠道这个概念，每条通知都要属于一个对应的渠道。用户可以自由地选择这些通知渠道的重要程度，是否响铃、是否振动、或者是否要
 * 关闭这个渠道的通知。如果你将项目中的targetSdkVersion指定到了26或者更高，那么Android系统就会认为你的App已经做好了8.0系统的适配工作，当然包括了通知栏的适配。
 * 这个时候如果还不使用通知渠道的话，那么你的App的通知将完全无法弹出
 *
 * TODO 抽空优化和完善一下
 */
class NotificationUtils//首先会按顺序执行类中init代码块，然后再执行构造方法里代码，并且我可以在init代码块中
//使用类声明的属性。加入伴生对象后，那肯定就是伴生对象中的代码先执行了。
    (context: Context) {

    //1.获取系统的NotificationManager服务
    //2.创建NotificationChannel对象，并在NotificationManager上创建该Channel对象
    //3.创建Notification对象，并用NotificationManager发送或者取消该通知

    val CHANNEL_ID: String = "download"//渠道ID，可以随便定义，只要保证全局唯一性就可以
    val CHANNEL_NAME: String = "下载场景使用的通知类别"//渠道名称，给用户看的，需要能够表达清楚这个渠道的用途
    val NOTIFICATION_ID: Int = 0x1001;//通知ID，只要保证全局唯一性就可以

    private var context: Context = context.applicationContext

    fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun getNotification(
        notificationManager: NotificationManager,
        id: String,
        name: CharSequence
    ): Notification {

        val builder: NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel: NotificationChannel =
                    NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
                channel.enableLights(true)//设置通知出现时的闪光灯
                channel.enableVibration(true)//设置通知出现时震动
//                channel.importance = NotificationManager.IMPORTANCE_HIGH//设置渠道的重要等级
                notificationManager.createNotificationChannel(channel)

                NotificationCompat.Builder(context, CHANNEL_ID)

            } else {
                NotificationCompat.Builder(context)
            }

        //自定义通知栏样式
        val remoteViews: RemoteViews =
            RemoteViews(context.packageName, R.layout.notification_download)
        remoteViews.setTextViewText(R.id.fileName, "正在下载")

        //通知调转
        val intent: Intent = Intent(context, PracticeViewActivity::class.java)
        val pi: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        return builder
            .setTicker("APK升级")//状态栏显示的提示
            .setContentTitle("准备下载")//通知栏标题
            .setContentText("正在下载……")//通知正文
            .setContent(remoteViews)//自定义布局
            .setSmallIcon(R.mipmap.ic_launcher)//系统状态栏显示的小图标
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )//下拉显示的大图标
            .setWhen(System.currentTimeMillis())//通知弹出时间
            .setAutoCancel(true)//可以点击通知栏的删除按钮删除
            .setContentIntent(pi)//设置意图
            .build()
    }

    fun showNotify(notificationManager: NotificationManager, notification: Notification) {
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun delNotify(notificationManager: NotificationManager, notificationId: Int) {
        notificationManager.cancel(notificationId)
    }
}