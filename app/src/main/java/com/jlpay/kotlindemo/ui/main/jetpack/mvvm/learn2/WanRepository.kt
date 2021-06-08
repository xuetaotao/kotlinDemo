package com.jlpay.kotlindemo.ui.main.jetpack.mvvm.learn2

import com.jlpay.kotlindemo.net.LogInterceptor
import com.jlpay.kotlindemo.net.UrlConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Kotlin 协程
 * 是什么：线程框架，就是launch里面的代码
 */
class WanRepository private constructor() {

    val DEFAULT_TIME = 30

    private var service: WanService

    companion object {
        @JvmStatic
        fun getWanRepository(): WanRepository.WanService {
            return SingleFacade.wanRepository.service
        }
    }

    init {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(LogInterceptor())
            .writeTimeout(DEFAULT_TIME.toLong(), TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIME.toLong(), TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIME.toLong(), TimeUnit.SECONDS)
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(UrlConfig.WAN_ANDROID_BASE_URL)
            .build()
        service = retrofit.create(WanService::class.java)
    }


    /**
     * suspend 挂起函数
     * suspend关键字只起到了标志这个函数是一个耗时操作，必须放在协程中执行的作用，而withContext方法则进行了线程的切换工作。
     * 挂起操作靠的是挂起函数里的实际代码，而不是关键字
     *
     * 协程中的代码自动地切换到其他线程之后又自动地切换回了主线程！ 顺序编写保证了逻辑上的直观性，协程的自动线程切换又保证了代码的非阻塞性。
     * 挂起函数必须在协程或者其他挂起函数中被调用，也就是挂起函数必须直接或者间接地在协程中执行。
     *
     * 那为什么协程中的代码没有在主线程中执行呢？而且执行完毕为什么还会自动地切回主线程呢？
     * 协程的挂起可以理解为协程中的代码离开协程所在线程的过程，不是这个协程停下来了而是协程所在的线程从这行代码开始不再运行在这个线程了，线程和协程分2波走了
     * 协程的恢复可以理解为协程中的代码重新进入协程所在线程的过程。协程就是通过的这个挂起恢复机制进行线程的切换。
     */
    interface WanService {
        @FormUrlEncoded
        @POST("user/login")
        suspend fun loginForm(
            @Field("username") username: String,
            @Field("password") password: String
        ): BaseResponse<UserBean>

        @POST("user/login")
        suspend fun loginQuery(
            @Query("username") username: String,
            @Query("password") password: String
        ): BaseResponse<UserBean>
    }

    private class SingleFacade {
        companion object {
            val wanRepository: WanRepository = WanRepository()
        }
    }
}