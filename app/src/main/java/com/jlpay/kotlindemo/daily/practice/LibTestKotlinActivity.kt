package com.jlpay.kotlindemo.daily.practice

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.base.BaseActivity
import com.jlpay.kotlindemo.utils.PermissionUtils
import com.jlpay.opensdk.location.LocationManager
import com.jlpay.opensdk.location.bean.LocationData
import com.jlpay.opensdk.location.listener.LocationListener
import java.io.File
import java.io.InputStream

class LibTestKotlinActivity : BaseActivity() {

    private lateinit var imageView: ImageView
    private var copyImgFromPubPic: String? = null
    private var photoUri: Uri? = null

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
        imageView = findViewById<ImageView>(R.id.imageView)
        val btn_media = findViewById<Button>(R.id.btn_media)
        btn_media.setOnClickListener {
            PermissionUtils.getStoragePermission(this, object : PermissionUtils.PermissionListener {
                override fun allow() {
                    val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")

                    //ACTION_PICK在Android11上测试返回data为空,resultCode = 0
//                    val intent: Intent = Intent(Intent.ACTION_PICK)
//                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")

                    startActivityForResult(intent, 0x1002)
                }
            })
        }

        val btn_media2 = findViewById<Button>(R.id.btn_media2)
        btn_media2.setOnClickListener {
            PermissionUtils.getStoragePermission(this, object : PermissionUtils.PermissionListener {
                override fun allow() {
                    val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                    startActivityForResult(intent, 0x1004)
                }
            })
        }

        val btn_media3 = findViewById<Button>(R.id.btn_media3)
        btn_media3.setOnClickListener {
            PermissionUtils.getStoragePermission(this, object : PermissionUtils.PermissionListener {
                override fun allow() {
                    val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                    startActivityForResult(intent, 0x1005)
                }
            })
        }

        val btn_createDir = findViewById<Button>(R.id.btn_createDir)
        btn_createDir.setOnClickListener {
            //测试外部目录(非共享目录)文件夹创建，结论：targetSdkVersion在Android10以下可以成功，以上不能成功
            PermissionUtils.getStoragePermission(this, object : PermissionUtils.PermissionListener {
                override fun allow() {
                    if (AndroidQAdapter.createDirs(this@LibTestKotlinActivity,
                            Environment.getExternalStorageDirectory().absolutePath + File.separator + "XgdJLPay")
                    ) {
                        Log.e("LibTestKotlinActivity", "文件夹创建成功")
                        showToast("文件夹创建成功")
                    } else {
                        Log.e("LibTestKotlinActivity", "文件夹创建失败了")
                        showToast("文件夹创建失败了")
                    }
                }
            })
        }

        val btn_photo = findViewById<Button>(R.id.btn_photo)
        btn_photo.setOnClickListener {
            PermissionUtils.getCameraPermission(this, object : PermissionUtils.PermissionListener {
                override fun allow() {
                    val intent: Intent = Intent()
                    intent.action = MediaStore.ACTION_IMAGE_CAPTURE

                    //APP外部私有目录
                    var uri: Uri? = null
                    //方式一：
//                    val IMG_APP_EXTERNAL: String =
//                        getExternalFilesDir(null).toString() + File.separator + "Image" + File.separator
//                    if (AndroidQAdapter.createDirs(this@LibTestKotlinActivity, IMG_APP_EXTERNAL)) {
//                        val file =
//                            File(IMG_APP_EXTERNAL + "IMG" + System.currentTimeMillis() + ".jpg")
//                        uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            FileProvider.getUriForFile(this@LibTestKotlinActivity,
//                                "com.jlpay.kotlindemo.FileProvider",
//                                file)// content://com.jlpay.kotlindemo.FileProvider/external_files_path/Image/IMG1625475923370.jpg
//                        } else {
//                            Uri.fromFile(file)
//                        }
//                    } else {
//                        Log.e("LibTestKotlinActivity", "拍照保存创建APP外部私有目录文件夹失败了")
//                        showToast("拍照保存创建APP外部私有目录文件夹失败了")
//                    }
                    //方式三：综合一下
                    uri = AndroidQAdapter.Images.createImageContentUri(this@LibTestKotlinActivity,
                        false,
                        null,
                        "com.jlpay.kotlindemo.FileProvider")
                    if (uri != null) {
                        photoUri = uri
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivityForResult(intent, 0x1001)
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
                    //方式一：
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        uri = AndroidQAdapter.Images.createImgPicUri(this@LibTestKotlinActivity,
//                            externPicDirName)
//                    } else {
//                        val IMG_APP_EXTERNAL: String =
//                            Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_PICTURES + File.separator + externPicDirName + File.separator
//                        if (AndroidQAdapter.createDirs(this@LibTestKotlinActivity,
//                                IMG_APP_EXTERNAL)
//                        ) {
//                            val file =
//                                File(IMG_APP_EXTERNAL + "IMG" + System.currentTimeMillis() + ".jpg")
//                            uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                FileProvider.getUriForFile(this@LibTestKotlinActivity,
//                                    "com.jlpay.kotlindemo.FileProvider",
//                                    file)
//                            } else {
//                                Uri.fromFile(file)
//                            }
//                        } else {
//                            Log.e("LibTestKotlinActivity", "拍照保存创建APP外部共享目录文件夹失败了")
//                            showToast("拍照保存创建APP外部共享目录文件夹失败了")
//                        }
//                    }
                    //方式二：
//                    uri = AndroidQAdapter.Images.createImgPicUri(this@LibTestKotlinActivity,
//                        externPicDirName)
                    //方式三：综合一下
                    uri = AndroidQAdapter.Images.createImageContentUri(this@LibTestKotlinActivity,
                        true,
                        "HHE",
                        null)
                    if (uri != null) {
                        photoUri = uri
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        startActivityForResult(intent, 0x1003)
                    }
                }
            })
        }
    }

    fun safTest(view: View) {
        //通过系统的文件浏览器选择一个文件，注意Intent的action和category都是固定不变的
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        //筛选，只显示可以“打开”的结果，如文件(而不是联系人或时区列表)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        //type属性可以用于对文件类型进行过滤，如过滤只显示图像类型文件，设置为"*/*"表示显示所有类型的文件，type属性必须要指定，否则会产生崩溃
        intent.type = "image/*"// 文件类型："text/plain"
        startActivityForResult(intent, 0x1006)
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
                copyImgFromPubPic = aa.copyImgFromPicToAppPic(this, uri)
                Log.e("LibTestKotlinActivity", "复制到外部APP私有目录地址：$copyImgFromPubPic")
//                Glide.with(this).load(copyImgFromPubPic).into(imageView)

                val imageContentUri =
                    copyImgFromPubPic?.let {
                        AndroidQAdapter.Images.getImageContentUri(this,
                            it,
                            "com.jlpay.kotlindemo.FileProvider")
                    }
                Log.e("LibTestKotlinActivity", "复制到外部APP私有目录Uri地址：$imageContentUri")
                Glide.with(this).load(imageContentUri).into(imageView)
            }

        } else if (requestCode == 0x1005) {
            if (!TextUtils.isEmpty(copyImgFromPubPic) && copyImgFromPubPic != null) {
                val imageContentUri: Uri? =
                    copyImgFromPubPic?.let {
                        AndroidQAdapter.Images.copyImgFromAppPicToPic(this,
                            it,
                            "JLPay")
                    }
                Log.e("LibTestKotlinActivity", "APP外部私有目录复制到外部Pic目录：$imageContentUri")
                Glide.with(this).load(imageContentUri).into(imageView)
            }

        } else if (requestCode == 0x1004) {
            if (data == null || data.data == null) return
            val uri = data.data
            val aa = AndroidQAdapter("miHaYou")
            if (uri != null) {
                val image = AndroidQAdapter.Images.getImageFromPic(this, uri)
                if (image != null) {
                    val imgSaveToPubPic = aa.imgSaveToPubPic(this, image)
                    Log.e("LibTestKotlinActivity",
                        "复制到外部共享目录Uri地址：$imgSaveToPubPic")// content://media/external/images/media/38184
//                    Glide.with(this).load(imgSaveToPubPic)
//                        .into(imageView)//经测试：content://media/external/images/media/38184 Uri地址的String形式路径可以通过Glide加载

                    val path: String? =
                        imgSaveToPubPic?.let { getPath(it) }
                    Log.e("LibTestKotlinActivity",
                        "复制到外部共享目录Path地址:$path")// /storage/emulated/0/Pictures/miHaYou/IMG1625456205397.jpg

                    if (!TextUtils.isEmpty(path) && path != null) {
                        try {
                            //经测试： 直接路径地址访问的，Android11可以加载,Android10不可以
//                        Glide.with(this).load(path)
//                            .into(imageView)

//                        val inputStream = FileInputStream(path)
//                        val bitmap: Bitmap =
//                            BitmapFactory.decodeStream(inputStream)//注：Android11可以用直接文件路径访问，也就是除了 MediaStore API之外还有两种方式可以访问媒体文件:File API，原生库，例如 fopen()。那Android 10咋办呢？？要不就用MediaStore，要不就直接把分区存储关了吧 (requestLegacyExternalStorage=true)
//                        imageView.setImageBitmap(bitmap)

                            //经测试：外部共享目录path地址，经过getImageContentUri该方法转为Uri地址后可以使用访问(10和11都没问题)
                            val imageContentUri =
                                AndroidQAdapter.Images.getImageContentUri(this,
                                    path,
                                    "com.jlpay.kotlindemo.FileProvider")
                            Glide.with(this).load(imageContentUri).into(imageView)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        } else if (requestCode == 0x1001) {
            if (data == null) {
                Log.e("LibTestKotlinActivity", "拍照的 data is null")
            }
            Log.e("LibTestKotlinActivity", "测试拍照保存到APP外部私有目录成功了:$photoUri")
            showToast("拍照保存到APP外部私有目录成功了")
            Glide.with(this).load(photoUri).into(imageView)

        } else if (requestCode == 0x1003) {
            if (data == null) {
                Log.e("LibTestKotlinActivity", "拍照的 data is null")
            }
            Log.e("LibTestKotlinActivity", "测试拍照保存到外部Pic目录成功了:$photoUri")
            showToast("测试拍照保存到外部Pic目录成功了")
            Glide.with(this).load(photoUri).into(imageView)

        } else if (requestCode == 0x1006) {
            if (data == null) {
                Log.e("LibTestKotlinActivity", "data is null")
                return
            }
            // 获取选择文件Uri
            val uri: Uri? = data.data
            val IMAGE_PROJECTION = arrayOf(
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA)
            uri?.let { it ->
                //方式一：获取文件流，可以用来读取操作
                val inputStream: InputStream? = contentResolver.openInputStream(it)
                //方式二： 获取图片信息
                val cursor: Cursor? = contentResolver.query(it, IMAGE_PROJECTION, null, null, null)
                cursor?.let {
                    if (cursor.moveToFirst()) {
                        val displayName =
                            cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                        val size = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                        val path = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[3]))
                        Log.e("TAG", "Uri:$it\tDisplay:$displayName\tSize:$size\tPath:$path")
                    }
                    cursor.close()
                }
            }

        }
    }

    //Android Q开始，公有目录只能通过Content Uri+id的方式访问，以前的File路径全部无效
    @Deprecated("")
    private fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        return if (cursor == null) {
            uri.path
        } else {
            val column_index = cursor.getColumnIndexOrThrow(projection[0])
            cursor.moveToFirst()
            val cursorString = cursor.getString(column_index)
            cursor.close()
            cursorString
        }
    }
}