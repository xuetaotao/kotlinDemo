package com.jlpay.kotlindemo.study_jetpack.mvvm6

import com.jlpay.kotlindemo.net.LogInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

class MainRepository private constructor() {

    private var service: WanAndroidService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(LogInterceptor())
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        service = retrofit.create(WanAndroidService::class.java)
    }

    companion object {
//        @JvmStatic
//        fun getWanService(): WanAndroidService {
//            return SingleInstance.mainRepository.service
//        }

        @JvmStatic
        fun getWanService() = SingleInstance.mainRepository.service
    }

    private class SingleInstance {
        companion object {
            val mainRepository = MainRepository()
        }
    }

    interface WanAndroidService {

        @FormUrlEncoded
        @POST("user/login")
        suspend fun loginForm(
            @Field("username") username: String,
            @Field("password") password: String
        ): ResponseData<MyUserBean>
    }

}


