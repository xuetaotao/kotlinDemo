package com.jlpay.kotlindemo.ui.utils

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import io.reactivex.subjects.PublishSubject

class ImagePickerFragment : Fragment() {

    private val TAG: String = "ImagePicker"
    private val TAKE_PHOTO_REQUEST_CODE: Int = 0x1001
    private val CHOOSE_PIC: Int = 0x1002

    private var mLogging: Boolean = false
    private var mSubjects: HashMap<String, PublishSubject<ImagePickerResult>> = HashMap()
    private var imageOperationKind: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun takePhoto(imageOperationKind: String, uri: Uri) {
        this.imageOperationKind = imageOperationKind
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
    }

    fun choosePic(imageOperationKind: String) {
        this.imageOperationKind = imageOperationKind
//        val intent: Intent = Intent(Intent.ACTION_PICK)//小米机型Android11版本返回resultCode!=RESULT_OK
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, CHOOSE_PIC)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO 后续可以加上请求权限
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PHOTO_REQUEST_CODE -> {
                onActivityResult(resultCode, data)
            }
            CHOOSE_PIC -> {
                onActivityResult(resultCode, data)
            }
        }
    }

    fun onActivityResult(resultCode: Int, data: Intent?) {
        val subject: PublishSubject<ImagePickerResult>? = this.mSubjects[imageOperationKind]
        this.mSubjects.remove(imageOperationKind)
        subject.run {
            if (this == null) {
                Log.e(TAG,
                    "ImagePicker.onActivityResult invoked but didn't find the corresponding imageOperationKind request.")
                return
            }
            mSubjects.remove(imageOperationKind)
            onNext(ImagePickerResult(resultCode, data?.data))
            onComplete()
        }
    }

    fun getSubjectByOperationKind(imageOperationKind: String): PublishSubject<ImagePickerResult>? {
        return if (this.mSubjects[imageOperationKind] != null) {
            this.mSubjects[imageOperationKind] as PublishSubject<ImagePickerResult>
        } else {
            null
        }
    }

    fun setSubjectForOperationKind(
        imageOperationKind: String,
        subject: PublishSubject<ImagePickerResult>
    ) {
        this.mSubjects[imageOperationKind] = subject
    }

    fun containsByOperationKind(imageOperationKind: String): Boolean {
        return this.mSubjects.containsKey(imageOperationKind)
    }

    fun setLogging(logging: Boolean) {
        this.mLogging = logging
    }

    fun log(message: String) {
        if (this.mLogging) {
            Log.d(TAG, message)
        }
    }
}