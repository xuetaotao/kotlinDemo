package com.jlpay.kotlindemo.ui.main.dailytest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.base.BaseActivity
import com.jlpay.kotlindemo.ui.utils.IAndroid11Upgrade
import com.jlpay.kotlindemo.ui.utils.PermissionUtils
import java.io.File

class MediaUtilsActivity : BaseActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btn_createDir: Button
    private lateinit var btn_copy1: Button
    private lateinit var btn_copy2: Button
    private lateinit var btn_copy3: Button
    private lateinit var btn_photo: Button
    private lateinit var btn_photo_pic: Button

    private var copyImgFromPubPic: String? = null
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getResourceId(): Int {
        return R.layout.activity_mediautils
    }

    override fun initView() {
        imageView = findViewById(R.id.imageView)
        btn_createDir = findViewById(R.id.btn_createDir)
        btn_copy1 = findViewById(R.id.btn_copy1)
        btn_copy2 = findViewById(R.id.btn_copy2)
        btn_copy3 = findViewById(R.id.btn_copy3)
        btn_photo = findViewById(R.id.btn_photo)
        btn_photo_pic = findViewById(R.id.btn_photo_pic)
    }

    override fun initData() {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            0x1001 -> {
                if (data == null) {
                    Log.e("TAG", "拍照的 data is null")
                }
                Log.e("TAG", "测试拍照保存到APP外部私有目录成功了:$photoUri")
                showToast("拍照保存到APP外部私有目录成功了")
                Glide.with(this).load(photoUri).into(imageView)
            }
            0x1003 -> {
                if (data == null) {
                    Log.e("TAG", "拍照的 data is null")
                }
                Log.e("TAG", "测试拍照保存到外部Pic目录成功了:$photoUri")
                showToast("测试拍照保存到外部Pic目录成功了")
                Glide.with(this).load(photoUri).into(imageView)
            }
            0x1002 -> {
                if (data == null || data.data == null) return
                val uri = data.data
                val aa: IAndroid11Upgrade = MediaUtils("miHaYou")
                if (uri != null) {
                    copyImgFromPubPic = aa.copyImgFromPicToAppPic(this, uri)
                    Log.e("TAG", "复制到外部APP私有目录地址：$copyImgFromPubPic")
                    Glide.with(this).load(copyImgFromPubPic).into(imageView)
                }
            }
            0x1004 -> {
                if (data == null || data.data == null) return
                val uri = data.data
                val aa: IAndroid11Upgrade = MediaUtils("JLPay")
                if (uri != null) {
                    val imgSaveToPubPic: Uri? = aa.getImgFromPubPic(this, uri)?.let {
                        aa.imgSaveToPubPic(this, it)
                    }
                    Log.e("TAG", "复制到外部共享目录Uri地址：$imgSaveToPubPic")
                    Glide.with(this).load(imgSaveToPubPic).into(imageView)
                }
            }
        }
    }

    fun createDir(view: View) {
        val dir =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "XgdJLPay"
        PermissionUtils.getStoragePermission(this
        ) {
            val fileDir = File(dir)
            if (!fileDir.exists()) {
                if (fileDir.mkdirs()) {
                    showToast("文件夹创建成功")
                } else {
                    showToast("文件夹创建失败了")
                }
            } else {
                showToast("文件夹已经存在了")
            }
        }
    }

    /**
     * 选择相册图片复制到APP外部私有目录
     */
    fun copy1(view: View) {
        getImageContent(0x1002)
    }

    /**
     * 选择相册图片复制到外部Pic目录
     */
    fun copy2(view: View) {
        getImageContent(0x1004)
    }

    /**
     * APP外部私有目录复制到外部Pic目录
     */
    fun copy3(view: View) {
        val aa: IAndroid11Upgrade = MediaUtils("JLPay666")
        val imageContentUri: Uri? = copyImgFromPubPic?.let {
            aa.copyImgFromAppPicToPic(this, it)
        }
        Log.e("TAG", "APP外部私有目录复制到外部Pic目录：$imageContentUri")
        Glide.with(this).load(imageContentUri).into(imageView)
    }

    /**
     * 测试拍照保存到APP外部私有目录
     */
    fun photo(view: View) {
        PermissionUtils.getCameraPermission(this) {
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val aa = MediaUtils("XGD")
            aa.createImgContentAppPicUri(this, "com.jlpay.kotlindemo.FileProvider")?.let {
                photoUri = it
                intent.putExtra(MediaStore.EXTRA_OUTPUT, it)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivityForResult(intent, 0x1001)
            }
        }
    }

    /**
     * 测试拍照保存到外部Pic目录
     */
    fun photo_pic(view: View) {
        PermissionUtils.getCameraPermission(this) {
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val aa = MediaUtils("XGD666")
            aa.createImgContentPicUri(this)?.let {
                photoUri = it
                intent.putExtra(MediaStore.EXTRA_OUTPUT, it)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivityForResult(intent, 0x1003)
            }
        }
    }

    fun getImageContent(requestCode: Int) {
        PermissionUtils.getStoragePermission(this) {
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, requestCode)
        }
    }
}