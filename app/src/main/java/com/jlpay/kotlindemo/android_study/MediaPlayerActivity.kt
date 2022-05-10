package com.jlpay.kotlindemo.android_study

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

/**
 * Android MediaPlayer 基础简介
 * https://juejin.cn/post/6844903946113269767
 *
 * Android 音频开发之 MediaPlayer
 * https://juejin.cn/post/6844903952618618887
 *
 * Android多媒体之认识MP3与内置媒体播放（MediaPlayer）
 * https://juejin.cn/post/6844903752445476878#heading-8
 *
 * android音视频指南-MediaPlayer概述
 * https://juejin.cn/post/6844903702877192206
 *
 * MediaPlayer 播放音频文件
 * 1.播放应用的资源文件 {@link com.jlpay.kotlindemo.android_study.AssetAndRawResActivity#rawStart()}
 * 2.播放应用的原始资源文件 {@link com.jlpay.kotlindemo.android_study.AssetAndRawResActivity#assetsStart()}
 *
 * int getDuration()：获取流媒体的总播放时长，单位是毫秒
 * int getCurrentPosition()：获取当前流媒体的播放的位置，单位是毫秒
 * void seekTo(int msec)：设置当前MediaPlayer的播放位置，单位是毫秒
 * void setLooping(boolean looping)：设置是否循环播放
 * boolean isLooping()：判断是否循环播放
 * boolean isPlaying()：判断是否正在播放
 * void prepare()：同步的方式装载流媒体文件
 * void prepareAsync()：异步的方式装载流媒体文件
 * void release ()：回收流媒体资源
 * void setAudioStreamType(int streamtype)：设置播放流媒体类型
 * void setWakeMode(Context context, int mode)：设置 CPU 唤醒的状态
 * setNextMediaPlayer(MediaPlayer next)：设置当前流媒体播放完毕，下一个播放的 MediaPlayer
 */
class MediaPlayerActivity : AppCompatActivity() {

    private val TAG: String = MediaPlayerActivity::class.java.simpleName
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        mediaPlayer = MediaPlayer()
        mediaPlayer?.isLooping = true//循环播放；如果调用setLooping(boolean)为true，MediaPlayer会停留在Started状态
//        mediaPlayer?.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)//使用‘唤醒锁 wake locks’保证 CPU 保持清醒
        mediaPlayer?.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                Toast.makeText(this@MediaPlayerActivity, "播放出错啦", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "MediaPlayer  onError")// 返回true表示在此处理错误，不会回调onCompletion
                return true
            }
        })
        mediaPlayer?.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer?) {
                Log.e(TAG, "MediaPlayer  onCompletion")
            }
        })
        mediaPlayer?.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: MediaPlayer?) {
                Log.e(TAG, "MediaPlayer  onPrepared")
                mediaPlayer?.start()
            }
        })
        mediaPlayer?.setOnSeekCompleteListener(object : MediaPlayer.OnSeekCompleteListener {
            override fun onSeekComplete(mp: MediaPlayer?) {
                Log.e(TAG, "MediaPlayer  onPrepared")
            }
        })
        //网络流媒体的缓冲变化时回调
        mediaPlayer?.setOnBufferingUpdateListener(object : MediaPlayer.OnBufferingUpdateListener {
            override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
                Log.e(TAG, "MediaPlayer  onBufferingUpdate is $percent")
            }
        })
    }

    fun externalAudio(view: View) {
        val intent: Intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "audio/*"

        startActivityForResult(intent, 0x1001)

//        //registerForActivityResult(): https://blog.csdn.net/jingzz1/article/details/107338872/
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            val resultCode: Int = it.resultCode
//            val intent: Intent? = it.data
//            if (resultCode == RESULT_OK && intent != null && intent.data != null) {
//                val uri: Uri = intent.data!!
//                val mediaPlayer: MediaPlayer = MediaPlayer()
//                mediaPlayer.setDataSource(this@MediaPlayerActivity, uri)
//                mediaPlayer.prepare()
//                mediaPlayer.start()
//            }
//        }.launch(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            val uri: Uri = data.data!!
            mediaPlayer?.let {
                try {
                    it.reset()
                    it.setDataSource(this@MediaPlayerActivity, uri)
                    //prepare() 的调用可能需要很长时间执行，因为它可能涉及到获取和解码媒体数据
                    //为了避免挂起UI线程，生成另一个线程来准备 MediaPlayer，并在完成时通知主线程。
//                    it.prepare()
//                    it.start()
                    //建议下面这样写，用异步的方式装载流媒体资源，避免装载超时引发 ANR
                    it.prepareAsync()//加载完成后跳转到setOnPreparedListener，执行onPrepared方法
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    //MediaPlayer暂停时，start()方法可以从暂停的位置继续播放。成功调用start方法后会进入Started状态
    fun start(view: View) {
        if (mediaPlayer?.isPlaying == true) {
            return
        }
        mediaPlayer?.start()//这里不能调prepare()方法
    }

    fun stop(view: View) {
        mediaPlayer?.stop()
    }

    fun pause(view: View) {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
        }
        mediaPlayer?.release()
        mediaPlayer = null
    }

}