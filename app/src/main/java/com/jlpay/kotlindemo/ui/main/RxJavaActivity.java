package com.jlpay.kotlindemo.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.jlpay.kotlindemo.bean.BResponse;
import com.jlpay.kotlindemo.bean.WxArticleBean;
import com.jlpay.kotlindemo.net.RetrofitClient;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class RxJavaActivity extends RxAppCompatActivity {

    //    private final String TAG = RxJavaActivity.class.getSimpleName();
    private final String TAG = "RxJava2--------------";

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, RxJavaActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
//        basicUse();
//        mapUse();
//        getWanWxarticle();
        getWanWxarticle2();
    }


    private void getWanWxarticle2() {
        RetrofitClient.get()
                .getWxarticle2()
                .subscribe(new Observer<WxArticleBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WxArticleBean wxArticleBean) {
                        List<WxArticleBean.DataBean> dataBeanList = wxArticleBean.getData();
                        for (WxArticleBean.DataBean dataBean : dataBeanList) {
                            Log.e(TAG, dataBean.getName());
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
     */
    private void basicUse() {
        //1.创建被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //把这个消息发送出去
                emitter.onNext("我花两万块买了个包");
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s + "哈哈哈";
            }
        });
        //2.创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

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
        //3.订阅，以便观察者能收到消息。这里是被观察者订阅观察者，不是一般的逻辑，主要是框架涉及考虑把
        observable.subscribe(observer);
    }
}
