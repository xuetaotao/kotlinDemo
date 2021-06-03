package com.jlpay.kotlindemo.ui.main.rxjava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.net.BaseObserver;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * 参考资料整理：
 * Android官方架构组件:Lifecycle详解&原理分析 ：https://qingmei2.blog.csdn.net/article/details/79029657
 * Android架构中添加AutoDispose解决RxJava内存泄漏：https://blog.csdn.net/mq2553299/article/details/79418068
 */
public class RxAutoDisposeActivity extends BaseNewMvpActivity<RxAutoDisposeContract.Presenter> implements RxAutoDisposeContract.View {

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
        textview = findViewById(R.id.textview);

//        test2();
//        LifecycleScopes.resolveScopeFromLifecycle();//TODO RxAutoDispose 核心代码位置
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
        presenter.netRequest();
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

    @NotNull
    @Override
    public RxAutoDisposeContract.Presenter createPresenter() {
        return new RxAutoDisposePresenter(this, this);
    }

    @Override
    public void onNetRequest(long aLong) {
        textview.setText(String.valueOf(aLong));
    }

    @Override
    public int getResourceId() {
        return R.layout.activity_rx_auto_dispose;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

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
                //AutoDispose在被订阅时，获取到Activity当前的生命周期，并找到对应需要结束订阅的生命周期事件
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
//                .as(RxAutoDisposeUtils.bindDestroyLifecycle(this))
//                .as(bindLifecycle())
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


    static class RxAutoDisposeUtils {

        private RxAutoDisposeUtils() {
            throw new IllegalStateException("can't instance the RxAutoDisposeUtils");
        }

        public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner) {
            return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner));
        }

        public static <T> AutoDisposeConverter<T> bindLifecycle(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, event));
        }

        public static <T> AutoDisposeConverter<T> bindDestroyLifecycle(LifecycleOwner lifecycleOwner) {
            return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, Lifecycle.Event.ON_DESTROY));
        }
    }
}
