package com.jlpay.kotlindemo.ui.main.jlpay

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityJlLibraryDebugBinding
import com.jlpay.ocr.OcrManager

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