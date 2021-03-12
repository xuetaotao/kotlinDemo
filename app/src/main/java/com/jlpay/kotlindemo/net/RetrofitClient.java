package com.jlpay.kotlindemo.net;

import android.util.Log;

import com.jlpay.kotlindemo.bean.WxArticleBean;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final int DEFAULT_TIME = 30;
    public static final String BASE_URL = "https://www.wanandroid.com/";
    private static final String TAG = "WanAndroid-----------";

    private WanAndroidService service;

    private RetrofitClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS)
                .addInterceptor(new LogInterceptor())
//                .addInterceptor(new SecurityInterceptor())
                .build();

        //1.创建Retrofit对象，采用建造者模式
        //测试api采用鸿洋的玩Android的api，https://www.wanandroid.com/blog/show/2
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())// 支持Gson解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava2
                .client(okHttpClient)
                .build();

        service = retrofit.create(WanAndroidService.class);

    }

    public static RetrofitClient get() {
        return SingleFacade.retrofitClient;
    }

    private static class SingleFacade {
        private static RetrofitClient retrofitClient = new RetrofitClient();
    }

    public void getWanWxarticle() {
        service.getWxarticle().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e(TAG, response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }


    public Observable<WxArticleBean> getWxarticle2() {
        return service.getWxarticle2()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
//                .compose(RxLifecycle.bindUntilEvent(BehaviorSubject.create(), ActivityEvent.DESTROY));//在Activity onDestroy时解除订阅，防止内存泄漏
    }
}
