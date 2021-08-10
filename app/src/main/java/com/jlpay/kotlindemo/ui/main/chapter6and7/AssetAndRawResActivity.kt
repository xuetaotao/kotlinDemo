package com.jlpay.kotlindemo.ui.main.chapter6and7

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

class AssetAndRawResActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assets_raw)
    }


    fun assetsStart(view: View) {
        val assets: AssetManager = assets
        try {
            val openFd: AssetFileDescriptor = assets.openFd("douzhe.mp3")
            val mediaPlayer: MediaPlayer = MediaPlayer()
            mediaPlayer.reset()//这里可以不加
//            mediaPlayer.setDataSource(openFd.fileDescriptor)//有点问题
            mediaPlayer.setDataSource(openFd.fileDescriptor, openFd.startOffset, openFd.length)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun rawStart(view: View) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.banyan)
        mediaPlayer.start()
    }


}