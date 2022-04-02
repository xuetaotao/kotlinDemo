package com.jlpay.eyepetizer.jlpay_library_debug

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.eyepetizer.R
import com.jlpay.eyepetizer.databinding.ActivityJlLibraryDebugBinding

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

                }
            }
        }
    }
}