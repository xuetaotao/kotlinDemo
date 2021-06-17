package com.example.mvvm.httpUtils.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 设置缓存
 * Created on 2020-12-23.
 * TODO unfinished
 */
class CacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()


        val response = chain.proceed(request)

        return response
    }
}