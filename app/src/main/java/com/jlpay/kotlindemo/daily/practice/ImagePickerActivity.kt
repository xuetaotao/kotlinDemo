package com.jlpay.kotlindemo.daily.practice

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.base.BaseActivity
import com.jlpay.kotlindemo.utils.ImageCompress
import com.jlpay.kotlindemo.utils.ImagePicker

class ImagePickerActivity : BaseActivity() {

    private lateinit var imageView: ImageView

    override fun getResourceId(): Int {
        return R.layout.activity_imagepicker
    }

    override fun initView() {
        imageView = findViewById(R.id.imageView)

    }

    override fun initData() {
    }


    /**
     * 选择相册图片复制到APP外部私有目录
     */
    fun copy1(view: View) {
        ImagePicker.with(this)
            .imagePickerListener(object : ImagePicker.ImagePickerListener {
                override fun onSuccess(imagePath: String) {
                    Log.e("TAG", "复制到APP外部私有目录地址：$imagePath")
                    Glide.with(this@ImagePickerActivity).load(imagePath).into(imageView)
                }

                override fun onFailed(msg: String, code: String) {
                    Log.e("TAG", msg)
                    showToast(msg)
                }
            })
            .compress(true)
//            .crop(true, "com.jlpay.kotlindemo.FileProvider")
            .isCamera(false)
            .startPick()
    }


    /**
     * 测试拍照保存到外部Pic目录
     */
    fun photo_pic(view: View) {
        ImagePicker.Builder(this)
            .imagePickerListener(object : ImagePicker.ImagePickerListener {
                override fun onSuccess(imagePath: String) {
                    Log.e("TAG", "拍照保存到：$imagePath")
                    showToast("拍照成功")
                    Glide.with(this@ImagePickerActivity).load(imagePath).into(imageView)
                }

                override fun onFailed(msg: String, code: String) {
                    Log.e("TAG", msg)
                    showToast(msg)
                }
            })
            .compress(true)
//            .crop(true, "com.jlpay.kotlindemo.FileProvider")
            .compressType(ImageCompress.ImageCompressType.OriginCompress)
            .startPick()
    }
}