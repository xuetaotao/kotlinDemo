package com.example.mvvm.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object StringUtil {

    fun getString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
        val sb = StringBuilder()
        var s: String? = reader.readLine()
        while (s != null) {
            sb.append(s).append("\n")
            s = reader.readLine()
        }
        return sb.toString()
    }
}