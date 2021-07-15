package com.jlpay.imagepick

import androidx.core.content.FileProvider

class ImagePickerFileProvider : FileProvider() {

    override fun onCreate(): Boolean {
        return true
    }
}