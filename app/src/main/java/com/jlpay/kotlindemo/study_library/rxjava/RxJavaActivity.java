package com.jlpay.kotlindemo.study_library.rxjava;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.base.BaseMvpActivity;
import com.jlpay.kotlindemo.bean.BResponse;
import com.jlpay.kotlindemo.bean.WxArticleBean;
import com.jlpay.kotlindemo.net.RetrofitClient;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * 测试 RxLifeCycle
 * 那 ButterKnife 是 JakeWharton 写的 views 注入框架，配合 mvp 后让代码更简洁
 * 如果 activity 里的 view 都释放了，很容易出现空指针。特别像使用了 view 注入框架的项目中。因为 view 注入框架中会对 view 在 activity 生命周期中进行注册和注销操作。
 * 一般会在 onCreate 中注册，在 onDestory 中注销，这样在 onDestory 之后访问 view 就肯定会出现空指针
 * 以下摘自：https://github.com/trello/RxLifecycle
 * <p>
 * RxLifecycle does not actually unsubscribe the sequence. Instead it terminates the sequence. The way in which it does so varies based on the type:
 * RxLifecycle实际上并没有取消订阅该序列。相反，它根据不同的类型采取不同的终止方式
 * <p>
 * Observable, Flowable and Maybe - emits onCompleted()
 * Single and Completable - emits onError(CancellationException)
 * If a sequence requires the Subscription.unsubscribe() behavior, then it is suggested that you manually handle the Subscription yourself and call unsubscribe() when appropriate.
 * 如果一个序列需要取消订阅，建议您手动处理订阅，并在适当的时候调用unsubscribe()
 * <p>
 * 上面这种方式与使用CompositeDisposable不一样，调用dispose()会将两根管道切断, 从而导致下游收不到事件，并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件.
 */
public class RxJavaActivity extends BaseMvpActivity<RxJavaContract.Presenter> implements RxJavaContract.View {

    //    private final String TAG = RxJavaActivity.class.getSimpleName();
    private final String TAG = "RxJava2--------------";

    private TextView tv_rxjava2;
    //    private RxJavaPresenter presenter;
    private ImageView imageView;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, RxJavaActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_rxjava2);

    }

    @Override
    public int getResourceId() {
        return R.layout.activity_rxjava2;
    }

    @NotNull
    @Override
    public RxJavaContract.Presenter createPresenter() {
        return new RxJavaPresenter(this, this);
    }

    @Override
    public void initView() {
        tv_rxjava2 = findViewById(R.id.tv_rxjava2);
//        presenter = new RxJavaPresenter(this, this);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    public void initData() {
//        basicUse();
//        mapUse();
//        getWanWxarticle();
//        getWanWxarticle2();
//        intervalUse();
//        presenter.netRequest();
//        mPresenter.netRequest();
//        publishSubjectTest();
    }

    public void onClickTest(View view) {
        justDemo();
    }

    @Override
    public void onNetRequest() {
        Log.e(TAG, "返回RxJavaActivity层");
        tv_rxjava2.setText("返回RxJavaActivity层");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy了");
        tv_rxjava2 = null;
    }

//    @Override
//    public <T> LifecycleTransformer<T> getActivityLifecycleProvider() {
//        return bindToLifecycle();//可以绑定Activity生命周期
////        return bindUntilEvent(ActivityEvent.DESTROY);//可以绑定Activity生命周期
//    }

    @SuppressLint("AutoDispose")
    private void getWanWxarticle2() {
        RetrofitClient.get()
                .getWxarticle2()
                .delay(5, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
//                .compose(bindToLifecycle())
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
     * just操作符
     * 通过直接传入N个参数来批量发送事件(最多九个参数)
     * 全部事件发送完毕后会回调onComplete方法
     */
    @SuppressLint("AutoDispose")
    private void justDemo() {
        String path = "https://scpic.chinaz.net/files/default/imgs/2022-09-26/c2ddb884ecac35cb.jpg";
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        Observable
                //第2步
                .just(path)
                //第3步
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        URL url = new URL(path);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setConnectTimeout(5000);
                        int responseCode = httpURLConnection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            InputStream inputStream = httpURLConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            return bitmap;
                        }
                        return null;
                    }
                })
                //subscribeOn() 指定的就是发射事件的线程，observerOn 指定的就是订阅者接收事件的线程。
                //多次指定发射事件的线程只有第一次指定的有效，也就是说多次调用 subscribeOn() 只有第一次的有效，其余的会被忽略
                //但多次指定订阅者接收线程是可以的，也就是说每调用一次 observerOn()，下游的线程就会切换一次
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .compose(ObservableTransformerDemo())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //订阅开始。第一步
                        progressDialog[0] = new ProgressDialog(RxJavaActivity.this);
                        progressDialog[0].setTitle("下载中");
                        progressDialog[0].show();
                    }

                    //第4步
                    @Override
                    public void onNext(Bitmap s) {
                        //拿到事件
                        imageView.setImageBitmap(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    //第5步
                    @Override
                    public void onComplete() {
                        if (progressDialog[0] != null) {
                            progressDialog[0].dismiss();
                        }
                    }
                });
    }

    /**
     * ObservableTransformer学习
     */
    private <T> ObservableTransformer<T, T> ObservableTransformerDemo() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                //subscribeOn() 指定的就是发射事件的线程，observerOn 指定的就是订阅者接收事件的线程。
                //多次指定发射事件的线程只有第一次指定的有效，也就是说多次调用 subscribeOn() 只有第一次的有效，其余的会被忽略
                //但多次指定订阅者接收线程是可以的，也就是说每调用一次 observerOn()，下游的线程就会切换一次
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * Consumer的使用
     */
    @SuppressLint("AutoDispose")
    private void consumerDemo() {
        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(2000);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        String message = throwable.getMessage();
                    }
                });
    }

    /**
     * flatMap的使用
     * 将事件拦截然后转成被观察者再次发送
     * 可以让网络请求按同步顺序先后请求，即拿到第一个请求的结果后再去请求第二个接口
     * <p>
     * doOnNext的作用
     * Observable 每发送 onNext() 之前都会先回调这个方法。
     * 应用场景：请求APi注册-->更新UI-->请求APi登录-->更新UI
     */
    @SuppressLint("AutoDispose")
    private void flatMapDemo() {
        Disposable disposable = Observable.just(1)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                    }
                })
                //只给下面切换异步线程
                .observeOn(Schedulers.io())
                .flatMap(new Function<Integer, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Integer integer) throws Exception {
                        List<Integer> list = new ArrayList<>();
                        list.add(integer);
                        list.add(integer + 1);
                        list.add(integer + 2);
                        //直接发送一个 List 集合数据给观察者
                        return Observable.fromIterable(list);

//                        return Observable.create(new ObservableOnSubscribe<String>() {
//                            @Override
//                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                                emitter.onNext("this is" + integer);
//                            }
//                        });
                    }
                })
                .flatMap(new Function<Integer, ObservableSource<WxArticleBean>>() {
                    @Override
                    public ObservableSource<WxArticleBean> apply(Integer s) throws Exception {
                        return RetrofitClient.get().getWxarticle2();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WxArticleBean>() {
                    @Override
                    public void accept(WxArticleBean integer) throws Exception {

                    }
                });
    }

    /**
     * RxJava的Hook
     * 把这个方法放到rxJava执行之前就可以hook了
     */
    private void rxJavaHook() {
        RxJavaPlugins.setOnObservableAssembly(new Function<Observable, Observable>() {
            @Override
            public Observable apply(Observable observable) throws Exception {
                Log.e(TAG, "apply: 这是所有RxJava的Hook：" + observable);
                return observable;//如果返回null，那么所有的操作符就不能使用了
            }
        });
    }


    /**
     * zip 操作符
     * 可以将多个发射器发送的事件对应发送顺序组合成一个然后统一一次接收事件, 遵守两两合并的原则.
     * 如果存在异步情况, 将会等待需要合并的两个事件同时执行完毕后再发送给观察者;
     * <p>
     * 获取当前登录合伙人的分润规则
     */
//    public static void getMyProfitRule(@NonNull CallBackListener listener) {
//        //查询卡类型
//        Observable<CardTypeBean> cardObservable = getCardObservable();
//        //查询机具类型
//        Observable<PhysnTypeBean> physnTypeObservable = getPhysnTypeObservable();
//        //查询分润规则类型
//        Observable<RuleTypeBean> ruleTypeObservable = getRuleTypeObservable();
//        //查询自身分润规则
//        Observable<FenlunBean> fenlunObservable = getFenlunObservable();
//
//        Disposable disposable = Observable.zip(cardObservable, physnTypeObservable, ruleTypeObservable, fenlunObservable,
//                new Function4<CardTypeBean, PhysnTypeBean, RuleTypeBean, FenlunBean, MyProfitListBean>() {
//                    @Override
//                    public MyProfitListBean apply(CardTypeBean cardTypebean, PhysnTypeBean physnTypeBean, RuleTypeBean ruleTypeBean, FenlunBean fenlunBean) throws Exception {
//                        return getMyProfitListBean(cardTypebean, physnTypeBean, ruleTypeBean, fenlunBean);
//                    }
//                })
//                .subscribe(new Consumer<MyProfitListBean>() {
//                    @Override
//                    public void accept(MyProfitListBean myProfitListBean) throws Exception {
//                        listener.callBackSuccess(myProfitListBean.getMyProfitBeanList());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        listener.callBackFail(throwableHandle(throwable).message);
//                    }
//                });
//    }


    /**
     * interval 操作符
     * interval 会切换线程，使用的时候更新UI要注意切换回主线程
     * 创建一个每隔给定的时间间隔发射一个递增整数的Observable
     * 每隔一段时间就会发送一个事件，这个事件是从0开始，不断增1的数字
     * 可以用来设置定时器
     * <p>
     * Disposable的使用：避免内存泄漏问题
     */
    private void intervalUse() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(RxLifecycle.bindUntilEvent(BehaviorSubject.create(), ActivityEvent.DESTROY))//不可以实现绑定Activity生命周期，因为BehaviorSubject压根没有发射事件
//                .compose(bindUntilEvent(ActivityEvent.DESTROY))//可以绑定Activity生命周期
//                .compose(bindToLifecycle())//可以绑定Activity生命周期
//                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))//使用AutoDispose来绑定生命周期一
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))//使用AutoDispose来绑定生命周期二
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
     * 手动添加管理RxJava内存泄漏
     */
    static class CommonComposite {

        private CompositeDisposable mCompositeDisposable;

        public void addDisposable(Disposable disposable) {
            if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
                mCompositeDisposable = new CompositeDisposable();
            }
            mCompositeDisposable.add(disposable);
        }

        //在Activity销毁的时候调用
        public void unDisposable() {
            if (mCompositeDisposable != null) {
                mCompositeDisposable.dispose();
            }
        }
    }

    /**
     * PublishSubject  与普通的Subject不同，在订阅时并不立即触发订阅事件，而是允许我们在任意时刻手动调用onNext(),onError(),onCompleted来触发事件。
     * 可以看到PublishSubject与普通的Subject最大的不同就是其可以先订阅事件，然后在某一时刻手动调用方法来触发事件
     * https://blog.csdn.net/qq1026291832/article/details/51006746
     */
    @SuppressLint("AutoDispose")
    private void publishSubjectTest() {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        publishSubject.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e("TAG", "收到了：" + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        publishSubject.onNext(1);
        publishSubject.onNext(2);
        publishSubject.onNext(3);
    }


    /**
     * map 操作符
     * map 可以将被观察者发送的数据类型转变成其他的类型
     */
    @SuppressLint("AutoDispose")
    private void mapUse() {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        emitter.onNext("我花两万块买了个包");
                    }
                })
                .map(new Function<String, BResponse>() {
                    @Override
                    public BResponse apply(String s) throws Exception {
                        return new BResponse(s, "00");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BResponse>() {
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
    @SuppressLint("AutoDispose")
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
