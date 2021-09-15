package com.jlpay.kotlindemo.bean

import java.io.File
import java.util.*

class MimeTypeBean

/**
 * 根据文件后缀名获得对应的MIME类型
 * Returns the value corresponding to the given [key], or `null` if such a key is not present in the map.
 */
fun getMimeType(fileName: String): String? {
//    val split = fileName.split(".")
//    val nameSuffix = "." + split[split.size - 1]

    //获取后缀名前的分隔符"."在fName中的位置
    val lastDotIndex = fileName.lastIndexOf(".")
    if (lastDotIndex < 0) {
        return null
    }
    val nameSuffix = fileName.substring(lastDotIndex, fileName.length).toLowerCase(Locale.ROOT)
    if (nameSuffix.isBlank()) {
        return null
    }
    return mimeTypeMap[nameSuffix]
}

fun getMimeType(file: File): String? {
    return getMimeType(file.name)
}

val mimeTypeMap = mapOf(Pair(".3gp", "video/3gpp"),
    Pair(".apk", "application/vnd.android.package-archive"),
    ".asf" to "video/x-ms-asf",
    ".avi" to "video/x-msvideo",
    ".bin" to "application/octet-stream",
    ".bmp" to "image/bmp",
    ".c" to "text/plain",
    ".class" to "application/octet-stream",
    ".conf" to "text/plain",
    ".cpp" to "text/plain",
    ".doc" to "application/msword",
    ".docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    ".xls" to "application/vnd.ms-excel",
    ".xlsx" to "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    ".exe" to "application/octet-stream",
    ".gif" to "image/gif",
    ".gtar" to "application/x-gtar",
    ".gz" to "application/x-gzip",
    ".h" to "text/plain",
    ".htm" to "text/html",
    ".html" to "text/html",
    ".jar" to "application/java-archive",
    ".java" to "text/plain",
    ".jpeg" to "image/jpeg",
    ".jpg" to "image/jpeg",
    ".js" to "application/x-javascript",
    ".log" to "text/plain",
    ".m3u" to "audio/x-mpegurl",
    ".m4a" to "audio/mp4a-latm",
    ".m4b" to "audio/mp4a-latm",
    ".m4p" to "audio/mp4a-latm",
    ".m4u" to "video/vnd.mpegurl",
    ".m4v" to "video/x-m4v",
    ".mov" to "video/quicktime",
    ".mp2" to "audio/x-mpeg",
    ".mp3" to "audio/x-mpeg",
    ".mp4" to "video/mp4",
    ".mpc" to "application/vnd.mpohun.certificate",
    ".mpe" to "video/mpeg",
    ".mpeg" to "video/mpeg",
    ".mpg" to "video/mpeg",
    ".mpg4" to "video/mp4",
    ".mpga" to "audio/mpeg",
    ".msg" to "application/vnd.ms-outlook",
    ".ogg" to "audio/ogg",
    ".ofd" to "application/ofd",
    ".pdf" to "application/pdf",
    ".png" to "image/png",
    ".pps" to "application/vnd.ms-powerpoint",
    ".ppt" to "application/vnd.ms-powerpoint",
    ".pptx" to "application/vnd.openxmlformats-officedocument.presentationml.presentation",
    ".prop" to "text/plain",
    ".rc" to "text/plain",
    ".rmvb" to "audio/x-pn-realaudio",
    ".rtf" to "application/rtf",
    ".sh" to "text/plain",
    ".tar" to "application/x-tar",
    ".tgz" to "application/x-compressed",
    ".txt" to "text/plain",
    ".wav" to "audio/x-wav",
    ".wma" to "audio/x-ms-wma",
    ".wmv" to "audio/x-ms-wmv",
    ".wps" to "application/vnd.ms-works",
    ".xml" to "text/plain",
    ".z" to "application/x-compress",
    ".zip" to "application/x-zip-compressed",
    "" to "*/*")







