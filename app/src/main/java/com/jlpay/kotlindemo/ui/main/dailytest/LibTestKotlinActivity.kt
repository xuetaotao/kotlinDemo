package com.jlpay.kotlindemo.ui.main.dailytest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.content.FileProvider
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.base.BaseActivity
import com.jlpay.kotlindemo.ui.utils.PermissionUtils
import com.jlpay.opensdk.location.LocationManager
import com.jlpay.opensdk.location.bean.LocationData
import com.jlpay.opensdk.location.listener.LocationListener
import java.io.File
import kotlin.random.Random

class LibTestKotlinActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, LibTestKotlinActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getResourceId(): Int {
        return R.layout.activity_libtest_kotlin
    }

    override fun initData() {
        mediaTest()
    }

    override fun initView() {
        val tvResult: TextView = findViewById(R.id.tv_result)

        PermissionUtils.getLocationPermission(this, PermissionUtils.PermissionListener {
            LocationManager.with(this)
//                .defaultLocationType(LocationType.GAODE)
                .locationListener(object : LocationListener {
                    override fun onFail(errorCode: Int, errorMsg: String?) {
                        tvResult.text = "定位失败：$errorMsg"
                    }

                    override fun onLocation(locationData: LocationData?) {
                        tvResult.text = """定位成功：${locationData.toString()}"""
                    }
                }).startLocation()
        })
    }


    fun mediaTest() {
        val btn_media = findViewById<Button>(R.id.btn_media)
        btn_media.setOnClickListener {
            PermissionUtils.getStoragePermission(this, object : PermissionUtils.PermissionListener {
                override fun allow() {
                    val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                    startActivityForResult(intent, 0x1002)
                }
            })
        }

        val btn_createDir = findViewById<Button>(R.id.btn_createDir)
        btn_createDir.setOnClickListener {
            //测试外部目录(非共享目录)文件夹创建，结论：targetSdkVersion在Android10以下可以成功，以上不能成功
            if (AndroidQAdapter.createDirs(this,
                    Environment.getExternalStorageDirectory().absolutePath + File.separator + "JLPay" + Random(
                        3))
            ) {
                Log.e("LibTestKotlinActivity", "文件夹创建成功")
                showToast("文件夹创建成功")
            } else {
                Log.e("LibTestKotlinActivity", "文件夹创建失败了")
                showToast("文件夹创建失败了")
            }
        }

        val btn_photo = findViewById<Button>(R.id.btn_photo)
        btn_photo.setOnClickListener {
            PermissionUtils.getCameraPermission(this, object : PermissionUtils.PermissionListener {
                override fun allow() {
                    val intent: Intent = Intent()
                    intent.action = MediaStore.ACTION_IMAGE_CAPTURE

                    //APP外部私有目录：content://media/external/images/media/6612
                    val IMG_APP_EXTERNAL: String =
                        getExternalFilesDir(null).toString() + File.separator + "Image" + File.separator
                    if (AndroidQAdapter.createDirs(this@LibTestKotlinActivity, IMG_APP_EXTERNAL)) {
                        val file =
                            File(IMG_APP_EXTERNAL + "IMG" + System.currentTimeMillis() + ".jpg")
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            val uri: Uri = FileProvider.getUriForFile(this@LibTestKotlinActivity,
                                "com.jlpay.kotlindemo.FileProvider",
                                file)
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        } else {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
                        }
                        startActivityForResult(intent, 0x1001)

                    } else {
                        Log.e("LibTestKotlinActivity", "拍照保存创建APP外部私有目录文件夹失败了")
                        showToast("拍照保存创建APP外部私有目录文件夹失败了")
                    }
                }
            })
        }

        val btn_photo_pic = findViewById<Button>(R.id.btn_photo_pic)
        btn_photo_pic.setOnClickListener {
            PermissionUtils.getCameraPermission(this, object : PermissionUtils.PermissionListener {
                override fun allow() {
                    //外部共享目录，不需要在这里自己调方法创建目录
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val externPicDirName = "JLPay223"
                    var uri: Uri? = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        uri = AndroidQAdapter.Images.createImgPicUri(this@LibTestKotlinActivity,
                            externPicDirName)
                    } else {
                        val IMG_APP_EXTERNAL: String =
                            Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_PICTURES + File.separator + externPicDirName + File.separator
                        if (AndroidQAdapter.createDirs(this@LibTestKotlinActivity,
                                IMG_APP_EXTERNAL)
                        ) {
                            val file =
                                File(IMG_APP_EXTERNAL + "IMG" + System.currentTimeMillis() + ".jpg")
                            uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                FileProvider.getUriForFile(this@LibTestKotlinActivity,
                                    "com.jlpay.kotlindemo.FileProvider",
                                    file)
                            } else {
                                Uri.fromFile(file)
                            }
                        } else {
                            Log.e("LibTestKotlinActivity", "拍照保存创建APP外部共享目录文件夹失败了")
                            showToast("拍照保存创建APP外部共享目录文件夹失败了")
                        }
                    }
                    if (uri != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        startActivityForResult(intent, 0x1003)
                    }
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }

        if (requestCode == 0x1002) {
            if (data == null || data.data == null) return
            val uri = data.data
            val aa = AndroidQAdapter("miHaYou")
            if (uri != null) {
                val copyImgFromPubPic = aa.copyImgFromPubPic(this, uri)
                Log.e("LibTestKotlinActivity", "复制后111：$copyImgFromPubPic")

                val image = AndroidQAdapter.Images.getImageFromPic(this, uri)
                if (image != null) {
                    val imgSaveToPubPic = aa.imgSaveToPubPic(this, image)
                    Log.e("LibTestKotlinActivity", "复制后222：$imgSaveToPubPic")
                }
            }

        } else if (requestCode == 0x1001 || requestCode == 0x1003) {
            Log.e("LibTestKotlinActivity", "拍照的 data is null")
            showToast("拍照保存成功")
        }
    }
}