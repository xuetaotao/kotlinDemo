package com.jlpay.kotlindemo.ui.main.rxjava;

import android.content.Context;
import android.util.Log;

import com.jlpay.kotlindemo.net.BaseObserver;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class RxAutoDisposePresenter extends BaseNewPresenter implements RxAutoDisposeContract.Presenter {

    private RxAutoDisposeContract.View mView;
    private Context mContext;
    private String TAG = RxAutoDisposePresenter.class.getSimpleName();

    public RxAutoDisposePresenter(Context context, RxAutoDisposeContract.View view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void netRequest() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        //doOnDispose：当调用 Disposable 的 dispose() 之后回调该方法
                        Log.e(TAG, "=======" + "doOnDispose：" + "=========");
                        Log.e(TAG, "=======" + "doOnDispose，Thread：" + Thread.currentThread().getName() + "=========");
                    }
                })
                .as(bindLifecycle())//方式二：指定解除绑定周期
                .subscribe(new BaseObserver<Long>() {
                    @Override
                    public void onSuccess(Long data) {
                        Log.e(TAG, "=======" + "onSuccess：" + data + "=========");
                        mView.onNetRequest(data);
                    }

                    @Override
                    public void onError(String msg, String code) {
                        mView.onNetRequest(Long.parseLong(code));
                    }
                });
    }

    @Override
    public void anotherNetRequest() {

    }
}
