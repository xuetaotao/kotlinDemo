package com.jlpay.kotlindemo.daily.practice

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityJlLibraryDebugBinding
import com.jlpay.ocr.OcrManager

/**
 * 嘉联 OCR 组件库功能测试
 */
class JlLibraryDebugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mBinding =
            DataBindingUtil.setContentView<ActivityJlLibraryDebugBinding>(
                this,
                R.layout.activity_jl_library_debug
            )
        mBinding.lifecycleOwner = this

        mBinding.onClick = OnClickProxy()
    }


    inner class OnClickProxy {

        fun onClick(view: View) {
            when (view.id) {
                R.id.button -> {
                    OcrManager.with(this@JlLibraryDebugActivity)
                        .start()
                }
            }
        }
    }
}