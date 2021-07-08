package com.jlpay.kotlindemo.ui.utils

import android.app.Activity
import android.net.Uri
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject

class ImagePicker private constructor(builder: Builder) {

    val TAG: String = ImagePicker::class.java.simpleName

    val imgDirName: String? = builder.imgDirName
    val compress: Boolean = builder.compress
    val crop: Boolean = builder.crop
    val listener: ImagePicker.ImagePickerListener? = builder.listener
    val fragmentActivity: FragmentActivity = builder.fragmentActivity

    var mImagePickerFragment: ImagePicker.Lazy<ImagePickerFragment>

    init {
        mImagePickerFragment = getLazySingleton(fragmentActivity.supportFragmentManager)
    }

    private fun getLazySingleton(fragmentManager: FragmentManager): ImagePicker.Lazy<ImagePickerFragment> {
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
        uri: Uri?
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
        }

        return Observable.concat(Observable.fromIterable(list))
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


    fun takePhoto() {
        val createImgContentPicUri: Uri? =
            MediaUtils(imgDirName).createImgContentPicUri(fragmentActivity)
        val subscribe = RxPermissions(fragmentActivity)
            .request(android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .flatMap(Function<Boolean, ObservableSource<ImagePickerResult>> { t ->
                if (t) {
                    if (createImgContentPicUri != null) {
                        requestImplementation(ImageOperationKind.TAKE_PHOTO, createImgContentPicUri)
                    } else {
                        Observable.error(Exception("外部共享目录Uri创建失败"))
                    }

                } else {
                    Observable.error(Exception("权限获取失败"))
                }
            })
            .subscribe({ t ->
                if (t != null) {
                    if (t.resultCode != Activity.RESULT_OK) {
                        listener?.onFailed("resultCode!=RESULT_OK，回调失败",
                            t.resultCode.toString())
                    } else {
                        val copyImgFromPicToAppPic: String? =
                            MediaUtils(imgDirName).copyImgFromPicToAppPic(fragmentActivity,
                                createImgContentPicUri!!)
                        if (copyImgFromPicToAppPic == null || TextUtils.isEmpty(
                                copyImgFromPicToAppPic)
                        ) {
                            listener?.onFailed("拍照照片复制到APP外部私有目录失败", "01")
                        } else {
                            listener?.onSuccess(copyImgFromPicToAppPic)
                        }
                    }
                } else {
                    listener?.onFailed("ImagePickerResult返回为空", "01")
                }
            },
                { t -> listener?.onFailed(t?.message ?: "未知错误", "01") })
    }

    fun choosePic() {
        val subscribe = RxPermissions(fragmentActivity)
            .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .flatMap(Function<Boolean, ObservableSource<ImagePickerResult>> { t ->
                if (t) {
                    requestImplementation(ImageOperationKind.CHOOSE_PIC, null)
                } else {
                    Observable.error(Exception("权限获取失败"))
                }
            })
            .subscribe({ t ->
                if (t != null) {
                    val uri: Uri? = t.uri
                    if (t.resultCode != Activity.RESULT_OK) {
                        listener?.onFailed("resultCode!=RESULT_OK，回调失败",
                            t.resultCode.toString())
                    } else {
                        if (uri != null) {
                            val copyImgFromPicToAppPic: String? =
                                MediaUtils(imgDirName).copyImgFromPicToAppPic(fragmentActivity,
                                    uri)
                            if (copyImgFromPicToAppPic == null || TextUtils.isEmpty(
                                    copyImgFromPicToAppPic)
                            ) {
                                listener?.onFailed("选择的图片复制到APP外部私有目录失败", "02")
                            } else {
                                listener?.onSuccess(copyImgFromPicToAppPic)
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


    class Builder(internal var fragmentActivity: FragmentActivity) {

        internal var imgDirName: String? = "MediaImage"
        internal var compress: Boolean = false//压缩
        internal var crop: Boolean = false//裁剪
        internal var listener: ImagePicker.ImagePickerListener? = null

        fun imgDirName(imgDirName: String) = apply {
            this.imgDirName = imgDirName
        }

        fun compress(compress: Boolean) = apply {
            this.compress = compress
        }

        fun crop(crop: Boolean) = apply {
            this.crop = crop
        }

        fun imagePickerListener(listener: ImagePicker.ImagePickerListener) = apply {
            this.listener = listener
        }

        fun build(): ImagePicker = ImagePicker(this)

        //下面这些方法感觉写在Builder里面不太好
        fun takePhoto() {
            build().takePhoto()
        }

        fun choosePic() {
            build().choosePic()
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