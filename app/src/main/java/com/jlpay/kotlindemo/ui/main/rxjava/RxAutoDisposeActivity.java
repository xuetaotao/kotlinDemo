package com.jlpay.kotlindemo.ui.main.rxjava;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.net.BaseObserver;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class RxAutoDisposeActivity extends AppCompatActivity {

    public final String TAG = RxAutoDisposeActivity.class.getSimpleName();

    private TextView textview;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, RxAutoDisposeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "=======" + "onCreate()" + "=========");
        setContentView(R.layout.activity_rx_auto_dispose);
        textview = findViewById(R.id.textview);

        test2();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "=======" + "onStart()" + "=========");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "=======" + "onResume()" + "=========");
//        test();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "=======" + "onPause()" + "=========");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "=======" + "onStop()" + "=========");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "=======" + "onDestroy()" + "=========");
    }

    private void test() {
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
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))//方式一，自动绑定，在哪里注册，那么就在它的对立时期解除绑定
                .subscribe(new Observer<Long>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "=======" + "onSubscribe：" + "=========");
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e(TAG, "=======" + "onNext：" + aLong + "=========");
                        Log.e(TAG, "=======" + "onNext，Thread：" + Thread.currentThread().getName() + "=========");
                        textview.setText(String.valueOf(aLong));
                        if (aLong >= 10) {
                            disposable.dispose();
                            Log.e(TAG, "=======" + "dispose" + "=========");//这里会落后于doOnDispose执行
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "=======" + "onError：" + "=========");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "=======" + "onComplete：" + "=========");
                    }
                });
    }


    @SuppressLint("AutoDispose")
    private void test2() {
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
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))//方式二：指定解除绑定周期
                .subscribe(new BaseObserver<Long>() {
                    @Override
                    public void onSuccess(Long data) {
                        Log.e(TAG, "=======" + "onSuccess：" + data + "=========");
                        textview.setText(String.valueOf(data));
                    }

                    @Override
                    public void onError(String msg, String code) {
                        textview.setText("onError");
                    }
                });
    }
}
