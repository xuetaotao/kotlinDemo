package com.jlpay.kotlindemo.daily.practice

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject

class ImagePicker private constructor(builder: Builder) {

    val TAG: String = ImagePicker::class.java.simpleName

    val imgDirName: String = builder.imgDirName
    val compress: Boolean = builder.compress
    val compressType: ImageCompress.ImageCompressType = builder.compressType
    val compressIgnoreSize: Int = builder.compressIgnoreSize
    val crop: Boolean = builder.crop
    val authority: String? = builder.authority
    val listener: ImagePickerListener? = builder.listener
    val fragmentActivity: FragmentActivity = builder.fragmentActivity

    var mImagePickerFragment: Lazy<ImagePickerFragment>
    var mediaUtils: MediaUtils

    init {
        mImagePickerFragment = getLazySingleton(fragmentActivity.supportFragmentManager)
        mediaUtils = getLazyMediaUtils(imgDirName)
    }

    companion object {
        @JvmStatic
        fun with(fragmentActivity: FragmentActivity): Builder {
            return Builder(fragmentActivity)
        }
    }

    private fun getLazyMediaUtils(imgDirName: String): MediaUtils {
        if (this.mediaUtils == null) {
            synchronized(ImagePicker::class.java) {
                if (this.mediaUtils == null) {
                    this.mediaUtils = MediaUtils(imgDirName)
                }
            }
        }
        return mediaUtils
    }

    private fun getLazySingleton(fragmentManager: FragmentManager): Lazy<ImagePickerFragment> {
        return object : Lazy<ImagePickerFragment> {

            private var imagePickerFragment: ImagePickerFragment? = null

            @Synchronized
            override fun get(): ImagePickerFragment {
                if (this.imagePickerFragment == null) {
                    this.imagePickerFragment = getImagePickerFragment(fragmentManager)
                }
                return this.imagePickerFragment!!
            }
        }
    }

    private fun getImagePickerFragment(fragmentManager: FragmentManager): ImagePickerFragment? {
        var imagePickerFragment = findImagePickerFragment(fragmentManager)
        val isNewInstance: Boolean = imagePickerFragment == null
        if (isNewInstance) {
            imagePickerFragment = ImagePickerFragment()
            fragmentManager.beginTransaction().add(imagePickerFragment, TAG).commitNow()
        }
        return imagePickerFragment
    }

    private fun findImagePickerFragment(fragmentManager: FragmentManager): ImagePickerFragment? {
        val fragment: Fragment? = fragmentManager.findFragmentByTag(TAG)
        return if (fragment != null) {
            fragment as ImagePickerFragment
        } else {
            null
        }
    }

    private fun requestImplementation(
        imageOperationKind: String,
        uri: Uri?,
        cropOutputUri: Uri?
    ): Observable<ImagePickerResult> {
        val list: ArrayList<Observable<ImagePickerResult>> = ArrayList()
        var subject: PublishSubject<ImagePickerResult>? =
            this.mImagePickerFragment.get().getSubjectByOperationKind(imageOperationKind)
        if (subject == null) {
            subject = PublishSubject.create()
            this.mImagePickerFragment.get().setSubjectForOperationKind(imageOperationKind, subject)
        }
        list.add(subject)

        when (imageOperationKind) {
            ImageOperationKind.TAKE_PHOTO -> {
                takePhotoRequestFromFragment(imageOperationKind, uri!!)
            }
            ImageOperationKind.CHOOSE_PIC -> {
                choosePicRequestFromFragment(imageOperationKind)
            }
            ImageOperationKind.IMAGE_CROP -> {
                imageCropRequestFromFragment(imageOperationKind, uri!!, cropOutputUri!!)
            }
        }

        return Observable.concat(Observable.fromIterable(list))
    }

    private fun setLogging(logging: Boolean) {
        this.mImagePickerFragment.get().setLogging(logging)
    }

    private fun takePhotoRequestFromFragment(
        imageOperationKind: String,
        uri: Uri
    ) {
        this.mImagePickerFragment.get()
            .log("takePhotoRequestFromFragment:\t" + "imageOperationKind:$imageOperationKind, uri:${uri.toString()}")
        this.mImagePickerFragment.get().takePhoto(imageOperationKind, uri)
    }

    private fun choosePicRequestFromFragment(imageOperationKind: String) {
        this.mImagePickerFragment.get()
            .log("choosePicRequestFromFragment:\timageOperationKind:$imageOperationKind")
        this.mImagePickerFragment.get().choosePic(imageOperationKind)
    }

    private fun imageCropRequestFromFragment(
        imageOperationKind: String,
        uri: Uri,
        cropOutputUri: Uri
    ) {
        this.mImagePickerFragment.get()
            .log("imageCropRequestFromFragment:\timageOperationKind:$imageOperationKind")
        this.mImagePickerFragment.get().imageCrop(imageOperationKind, uri, cropOutputUri)
    }

    fun takePhoto() {
        var createImgContentPicUri: Uri? = null
        var cropOutputUri: Uri? = null
        val subscribe = RxPermissions(fragmentActivity)
            .request(android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .flatMap(object : Function<Boolean, ObservableSource<ImagePickerResult>> {
                override fun apply(t: Boolean): ObservableSource<ImagePickerResult> {
                    if (t) {
                        createImgContentPicUri =
                            mediaUtils.createImgContentPicUri(fragmentActivity)
                        if (crop) {
                            cropOutputUri =
                                mediaUtils.createImgContentPicUri(fragmentActivity)
                        }
                        return if (createImgContentPicUri != null) {
                            requestImplementation(
                                ImageOperationKind.TAKE_PHOTO,
                                createImgContentPicUri,
                                cropOutputUri)
                        } else {
                            Observable.error(Exception("外部共享目录Uri创建失败"))
                        }

                    } else {
                        return Observable.error(Exception("权限获取失败"))
                    }
                }
            })
            .compose(object : ObservableTransformer<ImagePickerResult, ImagePickerResult> {
                override fun apply(upstream: Observable<ImagePickerResult>): ObservableSource<ImagePickerResult> {
                    if (crop) {
                        return upstream.flatMap(object :
                            Function<ImagePickerResult, ObservableSource<ImagePickerResult>> {
                            override fun apply(t: ImagePickerResult): ObservableSource<ImagePickerResult> {
                                if (t.resultCode != Activity.RESULT_OK) {
                                    return Observable.error(Exception("resultCode!=RESULT_OK，拍照回调失败"))
                                } else {
                                    return if (cropOutputUri == null) {
                                        Observable.error(Exception("裁剪图片的外部共享目录Uri创建失败"))
                                    } else {
                                        requestImplementation(
                                            ImageOperationKind.IMAGE_CROP,
                                            createImgContentPicUri,
                                            cropOutputUri)
                                    }
                                }
                            }
                        })
                    } else {
                        return upstream
                    }
                }
            })
            .subscribe({ t ->
                if (t != null) {
                    if (t.resultCode != Activity.RESULT_OK) {
                        listener?.onFailed("resultCode!=RESULT_OK，回调失败",
                            t.resultCode.toString())
                    } else {
                        var uri: Uri = createImgContentPicUri!!
                        if (crop && cropOutputUri != null) {
                            uri = cropOutputUri as Uri
                        }
                        val copyImgFromPicToAppPic: String? =
                            mediaUtils.copyImgFromPicToAppPic(fragmentActivity,
                                uri)
                        if (copyImgFromPicToAppPic == null || TextUtils.isEmpty(
                                copyImgFromPicToAppPic)
                        ) {
                            listener?.onFailed("拍照照片复制到APP外部私有目录失败", "01")
                        } else {
                            if (compress) {
                                imageCompress(fragmentActivity,
                                    copyImgFromPicToAppPic,
                                    compressType,
                                    compressIgnoreSize,
                                    listener)
                            } else {
                                listener?.onSuccess(copyImgFromPicToAppPic)
                            }
                        }
                    }
                } else {
                    listener?.onFailed("ImagePickerResult返回为空", "01")
                }
            },
                { t -> listener?.onFailed(t?.message ?: "未知错误", "01") })
    }

    fun choosePic() {
        var cropOutputUri: Uri? = null
        val subscribe = RxPermissions(fragmentActivity)
            .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .flatMap(object : Function<Boolean, ObservableSource<ImagePickerResult>> {
                override fun apply(t: Boolean): ObservableSource<ImagePickerResult> {
                    if (t) {
                        if (crop) {
                            cropOutputUri =
                                mediaUtils.createImgContentPicUri(fragmentActivity)
                        }
                        return requestImplementation(
                            ImageOperationKind.CHOOSE_PIC,
                            null,
                            cropOutputUri)
                    } else {
                        return Observable.error(Exception("权限获取失败"))
                    }
                }
            })
            .compose(object : ObservableTransformer<ImagePickerResult, ImagePickerResult> {
                override fun apply(upstream: Observable<ImagePickerResult>): ObservableSource<ImagePickerResult> {
                    if (crop) {
                        return upstream.flatMap(object :
                            Function<ImagePickerResult, ObservableSource<ImagePickerResult>> {
                            override fun apply(t: ImagePickerResult): ObservableSource<ImagePickerResult> {
                                if (t.resultCode != Activity.RESULT_OK) {
                                    return Observable.error(Exception("resultCode!=RESULT_OK，相册选择照片回调失败"))
                                } else {
                                    when {
                                        cropOutputUri == null -> {
                                            return Observable.error(Exception("裁剪图片的外部共享目录Uri创建失败"))
                                        }
                                        t.uri == null -> {
                                            return Observable.error(Exception("选择照片返回的ImagePickerResult.Uri为空"))
                                        }
                                        else -> {
                                            if (authority == null || TextUtils.isEmpty(authority)) {
                                                return Observable.error(Exception("裁剪图片时传入的authority为空"))
                                            }
                                            val outPutUri: Uri =
                                                mediaUtils.copyImgFromPicToAppPic(
                                                    fragmentActivity,
                                                    t.uri!!)?.let {
                                                    mediaUtils.getImageContentUri(
                                                        fragmentActivity,
                                                        it,
                                                        authority)
                                                }
                                                    ?: return Observable.error(Exception("裁剪图片时复制原图到APP私有目录下并获取图片Uri出错"))
                                            return requestImplementation(
                                                ImageOperationKind.IMAGE_CROP,
                                                outPutUri,
                                                cropOutputUri)
                                        }
                                    }
                                }
                            }
                        })
                    } else {
                        return upstream
                    }
                }
            })
            .subscribe({ t ->
                if (t != null) {
                    var uri: Uri? = t.uri
                    if (crop && cropOutputUri != null) {
                        uri = cropOutputUri
                    }
                    if (t.resultCode != Activity.RESULT_OK) {
                        listener?.onFailed("resultCode!=RESULT_OK，回调失败",
                            t.resultCode.toString())
                    } else {
                        if (uri != null) {
                            val copyImgFromPicToAppPic: String? =
                                mediaUtils.copyImgFromPicToAppPic(fragmentActivity,
                                    uri)
                            if (copyImgFromPicToAppPic == null || TextUtils.isEmpty(
                                    copyImgFromPicToAppPic)
                            ) {
                                listener?.onFailed("选择的图片复制到APP外部私有目录失败", "02")
                            } else {
                                if (compress) {
                                    imageCompress(fragmentActivity,
                                        copyImgFromPicToAppPic,
                                        compressType,
                                        compressIgnoreSize,
                                        listener)
                                } else {
                                    listener?.onSuccess(copyImgFromPicToAppPic)
                                }
                            }
                        } else {
                            listener?.onFailed("ImagePickerResult返回Uri为空",
                                t.resultCode.toString())
                        }
                    }
                } else {
                    listener?.onFailed("ImagePickerResult返回为空", "02")
                }
            },
                { t -> listener?.onFailed(t?.message ?: "未知错误", "02") })
    }

    fun imageCompress(
        context: Context,
        imagePath: String,
        type: ImageCompress.ImageCompressType,
        ignoreSize: Int,
        listener: ImagePickerListener?
    ) {
        ImageCompress(imgDirName).compress(context,
            imagePath,
            type,
            ignoreSize,
            object : ImageCompress.ImageCompressListener {
                override fun onSuccess(imageCompressPath: String) {
                    listener?.onSuccess(imageCompressPath)
                }

                override fun onFailed(msg: String, code: String) {
                    listener?.onFailed(msg, code)
                }
            })
    }


    class Builder(internal var fragmentActivity: FragmentActivity) {

        internal var imgDirName: String = "ImagePicker"
        internal var isCamera: Boolean = true//默认为相机拍照
        internal var compress: Boolean = false//压缩
        internal var compressType: ImageCompress.ImageCompressType =
            ImageCompress.ImageCompressType.LuBan//默认使用LuBan压缩
        internal var compressIgnoreSize: Int = 1024//默认压缩阈值:单位KB
        internal var crop: Boolean = false//裁剪
        internal var authority: String? = null//Provider授权标志
        internal var listener: ImagePickerListener? = null

        fun imgDirName(imgDirName: String) = apply {
            this.imgDirName = imgDirName
        }

        fun isCamera(isCamera: Boolean) = apply {
            this.isCamera = isCamera
        }

        fun compress(compress: Boolean) = apply {
            this.compress = compress
        }

        fun compressType(compressType: ImageCompress.ImageCompressType) = apply {
            this.compressType = compressType
        }

        fun compressIgnoreSize(compressIgnoreSize: Int) = apply {
            this.compressIgnoreSize = compressIgnoreSize
        }

        //解决Android11调用ACTION_GET_CONTENT选择最近照片时，返回uri再去裁剪的如下问题:(故而选择先复制到APP外部私有目录下，再根据其路径获取uri进行操作)
        //UID 10363 does not have permission to content://com.android.providers.media.documents/document/image%3A7402 [user 0]; you could obtain access using ACTION_OPEN_DOCUMENT or related APIs
        fun crop(crop: Boolean, authority: String) = apply {
            this.crop = crop
            this.authority = authority
        }

        fun imagePickerListener(listener: ImagePickerListener) = apply {
            this.listener = listener
        }

        fun build(): ImagePicker = ImagePicker(this)

        private fun checkRxJavaSdk(): Boolean {
            return try {
                val clazz = Class.forName("io.reactivex.Observable")
                true
            } catch (e: Exception) {
                false
            } catch (e: Error) {
                false
            }
        }

        private fun checkRxPermissionsSdk(): Boolean {
            return try {
                val clazz = Class.forName("com.tbruyelle.rxpermissions2.RxPermissions")
                true
            } catch (e: Exception) {
                false
            } catch (e: Error) {
                false
            }
        }

        private fun checkLuBanSdk(): Boolean {
            return try {
                val clazz = Class.forName("top.zibin.luban.Luban")
                true
            } catch (e: Exception) {
                false
            } catch (e: Error) {
                false
            }
        }

//        fun takePhoto() {
//            build().takePhoto()
//        }

//        fun choosePic() {
//            build().choosePic()
//        }

        fun startPick() {
            if (!checkRxJavaSdk()) {
                listener?.onFailed("缺少RxJava依赖库", "04")
                return
            }
            if (!checkRxPermissionsSdk()) {
                listener?.onFailed("缺少RxPermissions依赖库", "04")
                return
            }
            if (compress && compressType == ImageCompress.ImageCompressType.LuBan && !checkLuBanSdk()) {
                listener?.onFailed("缺少Luban依赖库", "04")
                return
            }
            if (isCamera) {
                build().takePhoto()
            } else {
                build().choosePic()
            }
        }
    }


    interface ImagePickerListener {

        fun onSuccess(imagePath: String)

        fun onFailed(msg: String, code: String)
    }

    interface Lazy<V> {
        fun get(): V
    }


}