package com.jlpay.kotlindemo.base

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.jlpay.kotlindemo.utils.PermissionUtils
import java.io.File

abstract class BaseMediaActivity : BaseActivity() {

    private val CAMERA_CODE: Int = 0x1001
    private val PHOTO_ALBUM_CODE: Int = 0x1002

    fun openPhotoAlbum() {
        PermissionUtils.getStoragePermission(
            this
        ) {
//            val intent: Intent = Intent(Intent.ACTION_PICK)//TODO 小米10 Android11有问题,Intent会为空
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            //type属性可以用于对文件类型进行过滤，
            //如过滤只显示图像类型文件("image/*")，文件类型("text/plain")，设置为"*/*"表示显示所有类型的文件，type属性必须要指定，否则会产生崩溃
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, PHOTO_ALBUM_CODE)
        }
    }

    fun takePhoto() {
        PermissionUtils.getCameraPermission(
            this
        ) {
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val file: File =
                File(Constants.IMAGE_SAVE_DIR + "IMG" + System.currentTimeMillis() + ".jpg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //Android 7.0使用FileProvider，不再允许在app中把file://Uri暴露给其他app，包括但不局限于通过Intent或ClipData 等方法
                //FileProvider，使用它可以生成content://Uri来替代file://Uri
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val uri: Uri =
                    FileProvider.getUriForFile(this, "com.jlpay.kotlindemo.FileProvider", file)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            } else {
                intent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(file)
                )//file文件直接转换成"file://XXX/XXX/XXX"的uri格式
            }
            startActivityForResult(intent, CAMERA_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (data != null && data.data != null) {
            val data1 = data.data
        }
        when (requestCode) {
            CAMERA_CODE -> {

            }
            PHOTO_ALBUM_CODE -> {

            }
        }
    }
}