package com.jlpay.kotlindemo.study_library.httpdns;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BiliBili_API_HOST = "https://app.bilibili.com/";

    @GET("/x/splash?plat=0&width=1080&height=1920")
    Flowable<SplashPicRes> getSplashPic();

    @GET("banner/json")
    Call<Bean> getBanner();
}
