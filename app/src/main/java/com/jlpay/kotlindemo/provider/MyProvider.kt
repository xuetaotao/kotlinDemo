package com.jlpay.kotlindemo.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * 用于多个Android应用程序之间的数据交换
 * 通常与 ContentResolver 结合使用，一个应用程序使用 ContentProvider暴露自己的数据，而另一个应用程序则通过 ContentResolver 来访问数据
 */
class MyProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }
}