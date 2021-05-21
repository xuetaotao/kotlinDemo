package com.jlpay.kotlindemo.net;

import com.jlpay.kotlindemo.bean.WxArticleBean;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface WanAndroidService {

    //基本使用：获取公众号列表
    //baseUrl+接口中配置的地址组成真正的请求地址
    @GET("wxarticle/chapters/json")
    Call<ResponseBody> getWxarticle();

    @GET("wxarticle/chapters/json")
    Observable<WxArticleBean> getWxarticle2();

    @GET("wxarticle/chapters/json")
    Single<WxArticleBean> getWxarticle3();

    @GET
    Observable<ResponseBody> getImg(@Url String url);

    @GET
    Observable<ResponseBody> get(@Url String url);

    //表单编码，application/x-www-form-urlencoded
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> post(@Url String url, @FieldMap Map<String, String> params);

    @Multipart
    @POST
    Observable<ResponseBody> upload(@Url String url, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
