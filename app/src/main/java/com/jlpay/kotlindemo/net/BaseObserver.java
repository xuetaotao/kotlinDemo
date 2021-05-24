package com.jlpay.kotlindemo.net;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

    private String TAG = BaseObserver.class.getSimpleName();

    @Override
    public void onSubscribe(Disposable d) {
        Log.d(TAG, "=======" + "onSubscribe：" + "=========");
        Log.d(TAG, "=======" + "onSubscribe，Thread：" + Thread.currentThread().getName() + "=========");
    }

    @Override
    public void onNext(T t) {
        Log.d(TAG, "=======" + "onNext：" + "=========");
        Log.d(TAG, "=======" + "onNext，Thread：" + Thread.currentThread().getName() + "=========");
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "=======" + "onError：" + "=========");
        Log.d(TAG, "=======" + "onError，Thread：" + Thread.currentThread().getName() + "=========");
        //TODO 待完善，错误处理类：ExceptionHandle
        onError(e.getMessage(), "01");
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "=======" + "onComplete：" + "=========");
        Log.d(TAG, "=======" + "onComplete，Thread：" + Thread.currentThread().getName() + "=========");
    }

    public abstract void onSuccess(T data);

    public abstract void onError(String msg, String code);
}
