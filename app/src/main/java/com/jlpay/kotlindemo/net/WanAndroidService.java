package com.jlpay.kotlindemo.net;

import com.jlpay.kotlindemo.bean.WxArticleBean;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WanAndroidService {

    //基本使用：获取公众号列表
    //baseUrl+接口中配置的地址组成真正的请求地址
    @GET("wxarticle/chapters/json")
    Call<ResponseBody> getWxarticle();

    @GET("wxarticle/chapters/json")
    Observable<WxArticleBean> getWxarticle2();

    @GET("wxarticle/chapters/json")
    Single<WxArticleBean> getWxarticle3();
}
