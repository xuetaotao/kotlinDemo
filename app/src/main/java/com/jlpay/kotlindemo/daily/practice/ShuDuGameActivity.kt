package com.jlpay.kotlindemo.daily.practice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityShudugameBinding

/**
 * 数独小游戏
 * 未完成
 * 核心算法见 #HuaWeiTestActivity.hj44()
 */
class ShuDuGameActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityShudugameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityShudugameBinding>(
            this,
            R.layout.activity_shudugame
        )
        mBinding.lifecycleOwner = this
    }


}