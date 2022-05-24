package com.jlpay.kotlindemo.daily.practice

import android.net.Uri

class ImagePickerResult {

    var resultCode: Int = 0
    var uri: Uri? = null

    constructor(resultCode: Int, uri: Uri?) {
        this.resultCode = resultCode
        this.uri = uri
    }

}