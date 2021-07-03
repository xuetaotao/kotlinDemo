package com.jlpay.kotlindemo.ui.main.dailytest

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.jlpay.kotlindemo.ui.utils.IAndroid11Upgrade
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class AndroidQAdapter(@NonNull imgDirName: String) : IAndroid11Upgrade {


    constructor() : this("jlpay")

    override fun getImgFromPubPic(context: Context, uri: Uri): InputStream? {
        return Images.getImageFromPic(context, uri)
    }

    override fun imgSaveToPubPic(context: Context, inputStream: InputStream): String? {
        return Images.insertImageToPic(context, inputStream, DEFAULT_EXTERN_DIR_NAME)
    }

    override fun copyImgFromPubPic(context: Context, uri: Uri): String? {
        return Images.copyImgFromPicToAppDir(context, uri)
    }


    class Images {

        companion object {

            private fun getPicPath(imgDirName: String): String {
                return Environment.DIRECTORY_PICTURES + File.separator + imgDirName + File.separator
            }

            private fun getDcimPath(imgDirName: String): String {
                return Environment.DIRECTORY_DCIM + File.separator + imgDirName + File.separator
            }

            /**
             * 保存图片到外部共享Pic(暂时没有优先DCIM)目录下
             * @return 创建图片的URL地址(eg: content://media/external/images/media/6612)或null
             */
            fun createImgPicUri(context: Context, imgDirName: String): Uri? {
                if (!checkPermission(context)) {
                    return null
                }

                //1.创建ContentValues
                val imgFileName = "IMG" + System.currentTimeMillis() + ".jpg"
                val contentValues = ContentValues()
                contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME,
                    imgFileName)//设置带扩展名的文件名，如：IMG1024.JPG
                contentValues.put(MediaStore.Images.ImageColumns.MIME_TYPE,
                    getImgMimeType(imgFileName))//设置文件类型，类似于image/jpeg
                contentValues.put(MediaStore.Images.ImageColumns.DATE_ADDED,
                    System.currentTimeMillis())//第一次被添加的时间
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //AndroidQ 中不再使用DATA字段，而用RELATIVE_PATH代替，RELATIVE_PATH是相对路径不是绝对路径，DCIM是系统文件夹
                    contentValues.put(MediaStore.Images.ImageColumns.RELATIVE_PATH,
                        getPicPath(imgDirName))//文件存储的相对路径
                } else {
                    //AndroidQ以下版本，直接使用外部公共存储目录下的图片绝对路径，不能去掉，因为上面的要求API>=29，在10.0以下设备中下一步insert产生的Uri会为空
                    val imgFileDir = EXTERN_STORAGE_PATH + getPicPath(imgDirName)
                    if (createDirs(context, imgFileDir)) {
                        val imgFilePath = imgFileDir + imgFileName
                        contentValues.put(MediaStore.Images.ImageColumns.DATA,
                            imgFilePath)//文件存储的绝对路径
                    }
                }

                //2.执行insert操作，向系统文件夹中添加文件；若生成了uri，则表示该文件添加成功，使用流将内容写入该uri中即可。
                // 这一步会在Picture目录下创建imgDirName文件夹(没有该文件夹时)
                val contentResolver = context.contentResolver
                val pendingUri: Uri? =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues)
                return pendingUri
            }


            /**
             * 保存图片到外部共享目录
             */
            fun insertImageToPic(
                context: Context,
                inputStream: InputStream,
                imgDirName: String
            ): String? {
                val contentResolver = context.contentResolver
                val pendingUri = createImgPicUri(context, imgDirName)
                //3.执行图片数据插入操作
                return if (pendingUri != null) {
                    try {
                        val outputStream: OutputStream? =
                            contentResolver.openOutputStream(pendingUri)
                        if (outputStream != null) {
                            copy(inputStream, outputStream)
                        }
                        pendingUri.toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        contentResolver.delete(pendingUri, null, null)
                        null
                    }
                } else {
                    null
                }
            }

            /**
             * 从外部共享目录获取图片
             */
            fun getImageFromPic(context: Context, uri: Uri): InputStream? {
                return try {
                    context.contentResolver.openInputStream(uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }

            /**
             * 复制外部共享目录下图片到外部APP私有目录
             */
            fun copyImgFromPicToAppDir(context: Context, uri: Uri): String? {
                //外部存储APP私有目录
                val IMG_APP_EXTERNAL: String =
                    context.getExternalFilesDir(null)
                        .toString() + File.separator + "Image" + File.separator
                if (createDirs(context, IMG_APP_EXTERNAL)) {
                    val imgFileName = "IMG" + System.currentTimeMillis() + ".jpg"
                    val file = File(IMG_APP_EXTERNAL, imgFileName)
                    val inputStream = getImageFromPic(context, uri)
                    try {
                        val fileOutputStream = FileOutputStream(file)
                        if (inputStream != null) {
                            copy(inputStream, fileOutputStream)
                            return file.absolutePath
                        }
                        return null
                    } catch (e: Exception) {
                        e.printStackTrace()
                        return null
                    }
                }
                return null
            }

            fun getImgMimeType(imgFileName: String): String {
                val toLowerCase = imgFileName.toLowerCase(Locale.ROOT)
                if (toLowerCase.endsWith("jpg") || toLowerCase.endsWith("jpeg")) {
                    return "image/jpeg"
                } else if (toLowerCase.endsWith("png")) {
                    return "image/png"
                } else if (toLowerCase.endsWith("gif")) {
                    return "image/gif"
                }
                return "image/jpeg"
            }
        }
    }


    companion object {

        val DEFAULT_EXTERN_DIR_NAME = "JLPay"

        //Android10以下版本使用
        var EXTERN_STORAGE_PATH: String =
            Environment.getExternalStorageDirectory().absolutePath + File.separator

        private fun checkPermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }

        fun createDirs(context: Context, dir: String): Boolean {
            if (!checkPermission(context)) {
                return false
            }
            if (TextUtils.isEmpty(dir)) {
                return false
            }
            val fileDir = File(dir)
            if (!fileDir.exists()) {
                return fileDir.mkdirs()
            }
            return true
        }

        private fun copy(inputStream: InputStream, outputStream: OutputStream) {
            val buffer: ByteArray = ByteArray(1024)
            var readLength: Int
            try {
                while (inputStream.read(buffer).also { readLength = it } != -1) {
                    outputStream.write(buffer, 0, readLength)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    inputStream.close()
                    outputStream.flush()
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}