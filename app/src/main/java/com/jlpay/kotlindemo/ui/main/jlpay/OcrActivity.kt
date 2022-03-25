package com.jlpay.kotlindemo.ui.main.jlpay

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityOcrBinding
import com.jlpay.ocr.OcrManager

class OcrActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mBinding =
            DataBindingUtil.setContentView<ActivityOcrBinding>(this, R.layout.activity_ocr)
        mBinding.lifecycleOwner = this

        mBinding.onClick = OnClickProxy()
    }


    inner class OnClickProxy {

        fun onClick(view: View) {
            when (view.id) {
                R.id.button -> {
                    OcrManager.with(this@OcrActivity)
                        .start()
                }
            }
        }
    }
}