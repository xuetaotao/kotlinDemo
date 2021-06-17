package com.example.mvvm.httpUtils.interceptor

import com.example.mvvm.constant.HttpConstant
import com.example.mvvm.utils.MyMMKV
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 头部拦截器
 * Created on 2020-12-23.
 */
class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        builder.addHeader("Content-type", "application/json; charset=utf-8")
//            .header("token", token)
//            .method(request.method, request.body)

        val domain = request.url.host//域名
        val url = request.url.toString()
        //需要添加cookie的接口加上cookie
        if (domain.isNotEmpty() && (url.contains(HttpConstant.COLLECTIONS_WEBSITE)
                    || url.contains(HttpConstant.UNCOLLECTIONS_WEBSITE)
                    || url.contains(HttpConstant.ARTICLE_WEBSITE)
                    || url.contains(HttpConstant.TODO_WEBSITE)
                    || url.contains(HttpConstant.COIN_WEBSITE))
        ) {
            val spDomain = MyMMKV.mmkv.decodeString("domain") ?: ""
            val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
            if (cookie.isNotEmpty()) {
                // 将 Cookie 添加到请求头
                builder.addHeader(HttpConstant.COOKIE_NAME, cookie)
            }
        }
        return chain.proceed(builder.build())
    }
}