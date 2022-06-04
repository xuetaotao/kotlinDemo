package com.jlpay.kotlindemo.study_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.service.MusicPlayerService

class MusicServiceActivity : AppCompatActivity() {

    //TODO 放在这里定义会有问题
//    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_service)

//        val intent = Intent(this@MusicServiceActivity, MusicPlayerService::class.java)
    }

    public fun musicPlayerStart(view: View) {
        val intent = Intent(this@MusicServiceActivity, MusicPlayerService::class.java)
        startService(intent)
    }

    public fun musicPlayerPause(view: View) {
        val intent = Intent(this@MusicServiceActivity, MusicPlayerService::class.java)
        stopService(intent)
    }

    public fun musicPlayerLast(view: View) {

    }

    public fun musicPlayerNext(view: View) {

    }
}