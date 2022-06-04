package com.jlpay.kotlindemo.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.broadcast.MusicPlayerReceiver
import com.jlpay.kotlindemo.utils.NotificationUtils

/**
 * 创建一个前台服务
 * 前台服务是那些被认为用户知道（用户所认可的）且在系统内存不足的时候不允许系统杀死的服务。
 * 前台服务必须给状态栏提供一个通知，它被放到正在运行(Ongoing)标题之下——这就意味着通知只有
 * 在这个服务被终止或从前台主动移除通知后才能被解除。
 *
 * 在一般情况下，Service几乎都是在后台运行，一直默默地做着辛苦的工作。但这种情况下，后台运行的Service系统优先级
 * 相对较低，当系统内存不足时，在后台运行的Service就有可能被回收。
 * 那么，如果我们希望Service可以一直保持运行状态且不会在内存不足的情况下被回收时，可以选择将需要保持运行的Service
 * 设置为前台服务。
 *
 * 例如：App中的音乐播放服务应被设置在前台运行(前台服务)——在App后台运行时，便于用户明确知道它的当前操作、
 * 在状态栏中指明当前歌曲信息、提供对应操作
 */
class MusicPlayerService : Service() {

    private val TAG: String = this::class.java.simpleName
    private var musicPlayerReceiver: MusicPlayerReceiver? = null

    //通知ID，只要保证全局(整个apk包)唯一性就可以 TODO 可以写成1001看看
    private val NOTIFICATION_ID: Int = 0x2001;

    companion object {
        const val ACTION_PLAYER = "player";
        const val ACTION_PAUSE = "pause";
        const val ACTION_LAST = "last";
        const val ACTION_NEXT = "next";
    }


    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "MusicPlayerService onCreate：")
        registerMusicPlayer()
    }

    private fun registerMusicPlayer() {
        musicPlayerReceiver = MusicPlayerReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Companion.ACTION_PLAYER)
        intentFilter.addAction(ACTION_PAUSE)
        intentFilter.addAction(ACTION_LAST)
        intentFilter.addAction(ACTION_NEXT)
        registerReceiver(musicPlayerReceiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "MusicPlayerService onStartCommand: ")

        val notificationUtils = NotificationUtils(this)
        val notificationManager2 = notificationUtils.getNotificationManager(this)
        val notification = notificationUtils.getNotification(
            notificationManager2, notificationUtils.CHANNEL_ID, notificationUtils.CHANNEL_NAME
        )

        //启动前台服务
        startForeground(NOTIFICATION_ID, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "MusicPlayerService onBind: ")
        return null
    }

    /**
     * TODO 这个方法有问题，后面有空了找找，暂时先用NotificationUtils了
     */
    private fun buildNotification(): Notification {
        //1.创建NotificationManager
        val notificationManagerCompat: NotificationManagerCompat =
            NotificationManagerCompat.from(this)
        //2.构建Notification
        val CHANNEL_ID: String = "player"//渠道ID，可以随便定义，只要保证全局唯一性就可以(整个apk包)
        val notificationChannelCompat: NotificationChannelCompat =
            NotificationChannelCompat.Builder(
                CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_HIGH
            )
                .setLightsEnabled(true)
                .setVibrationEnabled(true)
                .build()
        notificationManagerCompat.createNotificationChannel(notificationChannelCompat)
        //自定义通知栏样式
        val remoteViews: RemoteViews =
            RemoteViews(applicationContext.packageName, R.layout.notification_download)
        remoteViews.setTextViewText(R.id.fileName, "music player")
        //通知调转
        val intent: Intent = Intent(this, MusicPlayerReceiver::class.java)
//        val intent: Intent = Intent(this, PracticeViewActivity::class.java)
        intent.action = Companion.ACTION_PLAYER
        val pi: PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
//        val pi: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            //TODO 暂时不写一堆必须的配置看看效果如何
            .setContentTitle("我是前台通知")//1。设置通知栏标题，必须
            .setContentText("music player")//2。设置通知正文内容，必须
            .setSmallIcon(R.drawable.ic_baseline_add_alarm_24)//3。设置小图标，必须，系统状态栏显示的小图标，Android5.0开始要求　应该使用alpha图层来进行绘制，而不应该包括RGB图层（也就是图片不能带颜色）
            .setContent(remoteViews)//自定义布局
            .setAutoCancel(true)//设置点击通知后自动清除通知，可以点击通知栏的删除按钮删除
            .setContentIntent(pi)//设置点击通知后的跳转意图
            .build()
        return notification
        //3.发送通知
//        val NOTIFICATION_ID: Int = 0x1002;//通知ID，只要保证全局(整个apk包)唯一性就可以
//        notificationManagerCompat.notify(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        // 停止前台服务
        stopForeground(true)
        musicPlayerReceiver?.let {
            unregisterReceiver(it)
        }
        super.onDestroy()
    }

}