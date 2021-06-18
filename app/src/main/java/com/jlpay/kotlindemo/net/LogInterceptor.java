package com.jlpay.kotlindemo.net;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LogInterceptor implements Interceptor {

    public static final String TAG = "接收响应：";
    public static final String JSON_MEDIA_TYPE = "application/json;charset=UTF-8";//报文请求体的MediaType类型：JSON类型
    public static final String FORM_MEDIA_TYPE = "application/x-www-form-urlencoded";//报文请求体的MediaType类型：Form表单

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();

//        Request requestBuilder = new Request.Builder()
//                .header("Content-Type", request.body().contentType().toString())
//                .build();

        Response response = chain.proceed(request);
        //不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一个新的response给应用层处理
        ResponseBody peekBody = response.peekBody(1024 * 1024);
        String formatResponseBody = String.format("接收响应：%s %n 返回数据：\n%s\n 响应头：\n%s\n", response.request().url(), peekBody.string(), response.headers());
        Log.d(TAG, formatResponseBody);

        return response;
    }


}
