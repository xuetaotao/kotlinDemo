package com.jlpay.kotlindemo.jetpack_study.mvvm2

import com.jlpay.kotlindemo.net.LogInterceptor
import com.jlpay.kotlindemo.net.UrlConfig
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Repository：数据仓库
 *
 * 一个数据仓库负责获取同类型的数据来源。
 *
 * 比如图书数据仓库能够获取各种条件筛选的图书数据，这份数据可以来自
 *
 * 网络（Retrofit + OKHttp），本地Database（Room），缓存 （HashMap）等等，
 *
 * ViewModel在从Repository获取数据时，不需关注数据具体是怎么来的
 */
class ImageRepository() {

    val DEFAULT_TIME = 30

    private lateinit var retrofit: Retrofit

    init {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIME.toLong(), TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIME.toLong(), TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIME.toLong(), TimeUnit.SECONDS)
            .addInterceptor(LogInterceptor())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(UrlConfig.JETPACK_MVVM_IMG_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private interface ImageService {

        @GET("HPImageArchive.aspx")
        fun getImage(
            @Query("format") format: String,
            @Query("idx") idx: Int,
            @Query("n") n: Int
        ): Observable<ImageBean>
    }

    fun getImage(format: String, idx: Int, n: Int): Observable<ImageBean> {
        return retrofit.create(ImageService::class.java).getImage(format, idx, n)
    }
}