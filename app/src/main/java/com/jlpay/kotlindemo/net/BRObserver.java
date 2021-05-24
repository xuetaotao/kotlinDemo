package com.jlpay.kotlindemo.net;

import android.util.Log;

import com.google.gson.Gson;
import com.jlpay.kotlindemo.bean.BResponse;

import org.json.JSONException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public abstract class BRObserver<T extends BResponse> implements Observer<ResponseBody> {
    private String TAG = BRObserver.class.getSimpleName();

    @Override
    public void onSubscribe(Disposable d) {
        Log.e(TAG, "=======" + "onSubscribe：" + "=========");
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        Log.e(TAG, "=======" + "onNext：" + "=========");
        try {
            String responseBodyStr = responseBody.string();
            BResponse bResponse = new Gson().fromJson(responseBodyStr, BResponse.class);
            if (bResponse == null) {
                throw new JSONException("解析异常");
            }
            if (bResponse.isSuccess()) {
                Type type = getType();
                T data = new Gson().fromJson(responseBodyStr, type);
                if (data == null) {
                    throw new RuntimeException("数据解析异常");
                }
                onSuccess(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "=======" + "onError：" + "=========");
        //TODO 待完善，错误处理类：ExceptionHandle
        onError(e.getMessage(), "01");
    }

    @Override
    public void onComplete() {
        Log.e(TAG, "=======" + "onComplete：" + "=========");
    }

    public abstract void onSuccess(T data);

    public abstract void onError(String msg, String code);

    public Type getType() {
        Type genericSuperclass = getClass().getGenericSuperclass();//获取当前类带泛型参数的父类，返回值为：BaseDao<Employee, String>
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("须传入指定类型");
        }
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();//Type的子接口：ParameterizedType 的 getActualTypeArguments()获取泛型参数的数组
            if (actualTypeArguments[0] instanceof Class) {
                return actualTypeArguments[0];
            }
        }
        return null;
    }
}
