package com.jlpay.kotlindemo.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.jlpay.kotlindemo.service.MusicPlayerService

class MusicPlayerReceiver : BroadcastReceiver() {

    private val TAG: String = this::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Log.e(TAG, "onReceive: $action")

        action?.let {
            when (action) {
                MusicPlayerService.ACTION_PLAYER -> {
                    Toast.makeText(context, "PLAYER", Toast.LENGTH_SHORT).show()
                }
                MusicPlayerService.ACTION_PAUSE -> {
                    Toast.makeText(context, "PAUSE", Toast.LENGTH_SHORT).show()
                }
                MusicPlayerService.ACTION_LAST -> {
                    Toast.makeText(context, "LAST", Toast.LENGTH_SHORT).show()
                }
                MusicPlayerService.ACTION_NEXT -> {
                    Toast.makeText(context, "ACTION_NEXT", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}