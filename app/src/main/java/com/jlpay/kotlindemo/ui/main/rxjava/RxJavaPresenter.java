package com.jlpay.kotlindemo.ui.main.rxjava;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxJavaPresenter implements RxJavaContract.Presenter {

    private final String TAG = RxJavaPresenter.class.getSimpleName();
    private Context mContext;
    private RxJavaContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        unDisposable();
    }

    public RxJavaPresenter(Context context, RxJavaContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void netRequest() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(((BaseActivity) mContext).getActivityLifecycleProvider())//方式一RxLifeCycle：可以绑定Activity生命周期：不推荐
//                .compose(mView.getActivityLifecycleProvider())//方式一RxLifeCycle：可以绑定Activity生命周期  TODO 考虑一下怎么把这行通用代码移出来；另外就是这行代码必须得放在subscribe之前
//                .compose(CommonTransformer.getLifeTransformer((BaseActivity) mContext))//方式一RxLifeCycle：可以绑定Activity生命周期，不推荐
//                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(mView.getLifecycleOwner())))//使用AutoDispose来绑定生命周期一
//                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(mView.getLifecycleOwner(), Lifecycle.Event.ON_DESTROY)))//使用AutoDispose来绑定生命周期二
                .doOnSubscribe(new Consumer<Disposable>() {//方式二：doOnSubscribe是事件被订阅之前(也就是事件源发起之前)会调用的方法，这个方法一般用于修改、添加或者删除事件源的数据流
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //doOnSubscribe：Observable 每发送 onSubscribe() 之前都会回调这个方法
                        Log.e(TAG, "doOnSubscribe");
                        addDisposable(disposable);
                    }
                })
                .subscribe(new Observer<Long>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
//                        addDisposable(d);//方式二：订阅后取消
                        Log.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e(TAG, Thread.currentThread().getName());
                        Log.e(TAG, "currentTime：" + aLong);
                        if (aLong >= 10) {
                            disposable.dispose();
                            Log.e(TAG, "计时完毕");
                            mView.onNetRequest();
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
}
