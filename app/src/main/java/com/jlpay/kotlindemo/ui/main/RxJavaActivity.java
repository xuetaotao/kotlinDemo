package com.jlpay.kotlindemo.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.bean.BResponse;
import com.jlpay.kotlindemo.bean.WxArticleBean;
import com.jlpay.kotlindemo.net.RetrofitClient;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxJavaActivity extends RxAppCompatActivity {

    //    private final String TAG = RxJavaActivity.class.getSimpleName();
    private final String TAG = "RxJava2--------------";

    private TextView tv_rxjava2;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, RxJavaActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava2);

        initView();
        initData();
    }

    private void initView() {
        tv_rxjava2 = findViewById(R.id.tv_rxjava2);
    }

    private void initData() {
        basicUse();
//        mapUse();
//        getWanWxarticle();
//        getWanWxarticle2();
//        intervalUse();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    private void getWanWxarticle2() {
        RetrofitClient.get()
                .getWxarticle2()
                .delay(5, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WxArticleBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "Disposable");
                    }

                    @Override
                    public void onNext(WxArticleBean wxArticleBean) {
                        Log.e(TAG, Thread.currentThread().getName());
                        List<WxArticleBean.DataBean> dataBeanList = wxArticleBean.getData();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (WxArticleBean.DataBean dataBean : dataBeanList) {
                            Log.e(TAG, dataBean.getName());
                            Toast.makeText(RxJavaActivity.this, TAG + dataBean.getName(), Toast.LENGTH_SHORT).show();
//                            tv_rxjava2.setText(stringBuilder.append(dataBean.getName()));//Activity不可见了但是不一定销毁，所以这里不一定会崩溃
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void getWanWxarticle() {
        RetrofitClient.get().getWanWxarticle();
    }


    /**
     * interval 操作符
     * 创建一个每隔给定的时间间隔发射一个递增整数的Observable
     * 可以用来设置定时器
     */
    private void intervalUse() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(bindUntilEvent(ActivityEvent.STOP))
                .compose(bindToLifecycle())
                .subscribe(new Observer<Long>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        Log.e(TAG, "Disposable");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e(TAG, Thread.currentThread().getName());
                        Log.e(TAG, "currentTime：" + aLong);
                        if (aLong >= 60) {
                            disposable.dispose();
                            Log.e(TAG, "计时完毕");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                });
    }


    /**
     * map 操作符
     */
    private void mapUse() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("我花两万块买了个包");
            }
        }).map(new Function<String, BResponse>() {
            @Override
            public BResponse apply(String s) throws Exception {
                return new BResponse(s, "00");
            }
        }).subscribe(new Observer<BResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BResponse bResponse) {
                Log.e(TAG, bResponse.getMsg());
                Log.e(TAG, "收到了，败家娘们儿");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * Rxjava2 基本使用
     * delay 操作符：延时接收 Observable 发送的消息 (使用时好像会切换线程，不了解，慎用)
     */
    private void basicUse() {
        //1.创建被观察者
        Observable<String> observable = Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        //把这个消息发送出去
                        emitter.onNext("我花两万块买了个包");
                        Log.e(TAG, "emitter消息：" + "我花两万块买了个包");
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s + "哈哈哈";
                    }
                })
                .delay(2, TimeUnit.SECONDS);
        //2.创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "Disposable");
            }

            @Override
            public void onNext(String s) {
                //你收到买包的消息
                //收到的消息经过map转化过
                Log.e(TAG, s);
                //你暗骂的话
                Log.e(TAG, "收到了，败家娘们儿");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observer.onNext("onNext");
        //3.订阅，以便观察者能收到消息。这里是被观察者订阅观察者，不是一般的逻辑，主要是框架涉及考虑把
        observable.subscribe(observer);
    }
}
