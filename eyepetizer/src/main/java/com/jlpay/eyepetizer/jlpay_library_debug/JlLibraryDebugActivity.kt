package com.jlpay.eyepetizer.jlpay_library_debug

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jlpay.eyepetizer.R
import com.jlpay.eyepetizer.databinding.ActivityJlLibraryDebugBinding
//import com.jlpay.features.imageload.ImagePicker

class JlLibraryDebugActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityJlLibraryDebugBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding =
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
//                    ImagePicker.with(this@JlLibraryDebugActivity)
//                        .isCamera(false)
////                        .compress(true)
////                        .compressType(ImageCompress.ImageCompressType.LuBan)
////                        .compressIgnoreSize(1000)
//                        .crop(true)
//                        .imagePickerListener(object : ImagePicker.ImagePickerListener {
//                            override fun onFailed(msg: String, code: String) {
//                                Toast.makeText(
//                                    this@JlLibraryDebugActivity,
//                                    msg,
//                                    Toast.LENGTH_SHORT
//                                )
//                                    .show()
//                            }
//
//                            override fun onSuccess(imagePath: String) {
//                                Glide.with(this@JlLibraryDebugActivity)
//                                    .load(imagePath)
//                                    .into(mBinding.ivImage)
//                            }
//                        })
//                        .startPick()
                }
            }
        }
    }
}