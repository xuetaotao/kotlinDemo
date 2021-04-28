package com.jlpay.kotlindemo.ui.main.rxjava;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jlpay.kotlindemo.R;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * 目的：解决RxJava使用中的内存泄漏问题
 * 例如，当使用RxJava订阅并执行耗时任务后，当Activity被finish时，如果耗时任务还未完成，没有及时取消订阅，就会导致Activity无法被回收，从而引发内存泄漏。
 * 为了解决这个问题，就产生了RxLifecycle，让RxJava变得有生命周期感知，使得其能及时取消订阅，避免出现内存泄漏问题。
 * 参考：https://juejin.cn/post/6844903617124630535#heading-56
 * https://juejin.cn/post/6844903634963185672#heading-13
 * https://juejin.cn/post/6844903614775820296
 */
public class RxLifecycleActivity extends RxAppCompatActivity {

    public final String TAG = RxLifecycleActivity.class.getSimpleName();

    private TextView textview;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, RxLifecycleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "=======" + "onCreate()" + "=========");
        setContentView(R.layout.activity_rxlifecycle);
        textview = findViewById(R.id.textview);

//        test();//实例1，使用 bindUntilEvent
        test2();//实例2，使用 bindToLifecycle()

//        takeUntilTest();
//        takeUntilTest2();
//        takeUntilTest3();
//        filterTest();
//        takeTest();
//        skipTest();
//        combineLatestTest();
//        intervalRangeTest();
//        zipTest();
//        onErrorReturnTest();
    }


    @SuppressLint("AutoDispose")
    private void test2() {
        //interval()：每隔一段时间就会发送一个事件，这个事件是从0开始，不断增1的数字
        Observable.interval(1, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        //doOnDispose：当调用 Disposable 的 dispose() 之后回调该方法
                        Log.e(TAG, "=======" + "doOnDispose：" + "=========");
                        Log.e(TAG, "=======" + "doOnDispose，Thread：" + Thread.currentThread().getName() + "=========");
                    }
                })
                .compose(bindToLifecycle())//在某个生命周期进行绑定，在对应的生命周期进行订阅解除。如在onResume()里调用test2()进行绑定订阅，则在onPause()进行解除订阅，生命周期是两两对应的。
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

    /**
     * compose一般情况下可以配合Transformer使用，以实现将一种类型的Observable转换成另一种类型的Observable，保证调用的链式结构
     */
    @SuppressLint("AutoDispose")
    private void test() {
        //interval()：每隔一段时间就会发送一个事件，这个事件是从0开始，不断增1的数字
        Observable.interval(1, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        //doOnDispose：当调用 Disposable 的 dispose() 之后回调该方法
                        Log.e(TAG, "=======" + "doOnDispose：" + "=========");
                        Log.e(TAG, "=======" + "doOnDispose，Thread：" + Thread.currentThread().getName() + "=========");
                    }
                })
                .compose(bindUntilEvent(ActivityEvent.DESTROY))//指定在生命周期onDestory()时，取消订阅
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

    /**
     * takeUntil()操作符：可以设置条件，当事件满足此条件时，下一次的事件就不会被发送了。
     */
    @SuppressLint("AutoDispose")
    private void takeUntilTest() {
        Disposable disposable = Observable.just(1, 3, 5, 4, 2, 8)
                .takeUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 4;// 返回true时，就停止发送事件。当第一个发送的数据满足>3时（这个与filter不同），就停止发送Observable的数据
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "=======" + "accept：" + integer + "=========");
                    }
                });
    }

    @SuppressLint("AutoDispose")
    private void takeUntilTest2() {
        // 1. 每1s发送1个数据 = 从0开始，递增1，即0、1、2、3
        Observable.interval(1, TimeUnit.SECONDS)
                // 2. 通过takeUntil的Predicate传入判断条件
                .takeUntil(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return aLong > 3;
                        // 返回true时，就停止发送事件
                        // 当发送的数据满足>3时，就停止发送Observable的数据
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "=======" + "onSubscribe：" + "=========");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e(TAG, "=======" + "onNext：" + aLong + "=========");
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
    private void takeUntilTest3() {
        // （原始）第1个Observable：每隔1s发送1个数据 = 从0开始，每次递增1
        Observable.interval(1, TimeUnit.SECONDS)
                //当第 5s 时，第2个 Observable 开始发送数据，于是（原始）第1个 Observable 停止发送数据
                .takeUntil(Observable.timer(5, TimeUnit.SECONDS))//timer()操作符：当到指定时间后就会发送一个 0L 的值给观察者
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "=======" + "onSubscribe：" + "=========");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e(TAG, "=======" + "onNext：" + aLong + "=========");
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

    /**
     * 过滤操作符：filter()
     * 通过一定逻辑来过滤被观察者发送的事件，如果返回 true 则会发送事件，否则不会发送
     */
    @SuppressLint("AutoDispose")
    private void filterTest() {
        Observable.just(1, 5, 3, 8, 6, 4)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 5;//返回 true 则会发送事件，只有小于5的事件才会发送，即1,3,4
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "=======" + "onSubscribe：" + "=========");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "=======" + "onNext：" + integer + "=========");
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

    /**
     * take()操作符
     * 控制观察者接收的事件的数量
     * take(int)用一个整数n作为一个参数，只发射前面的n项
     */
    @SuppressLint("AutoDispose")
    private void takeTest() {
        Observable.just(1, 5, 3, 6, 4)
                .take(3)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "=======" + "onSubscribe：" + "=========");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "=======" + "onNext：" + integer + "=========");
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

    /**
     * skip()操作符
     * 跳过正序某些事件，count 代表跳过事件的数量
     * skip(int)忽略Observable发射的前n项数据
     */
    @SuppressLint("AutoDispose")
    private void skipTest() {
        Observable.just(1, 5, 3, 2)
                .skip(3)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "=======" + "onSubscribe：" + "=========");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "=======" + "onNext：" + integer + "=========");
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

    /**
     * combineLatest() 操作符
     * combineLatest操作符可以将2~9个Observable发射的数据组装起来然后再发射出来。不过还有两个前提：
     * 1.所有的Observable都发射过数据。
     * 2.满足上面条件的时候任何一个Observable发射一个数据，就将所有Observable最新发射的数据按照提供的函数组装起来发射出去。
     * <p>
     * combineLatest() 的作用与 zip() 类似，但是 combineLatest() 发送事件的序列是与发送的时间线有关的，当 combineLatest() 中所有的 Observable 都发送了事件，
     * 只要其中有一个 Observable 发送事件，这个事件就会和其他 Observable 最近发送的事件结合起来发送，这样可能还是比较抽象，看看以下例子代码：
     * <p>
     * Observable A 会每隔1秒就发送一次事件，Observable B 会隔2秒发送一次事件
     * 分析结果可以知道，当发送 A1 事件之后，因为 B 并没有发送任何事件，所以根本不会发生结合。当 B 发送了 B1 事件之后，就会与 A 最近发送的事件 A2 结合成 A2B1，
     * 这样只有后面一有被观察者发送事件，这个事件就会与其他被观察者最近发送的事件结合起来了
     */
    @SuppressLint("AutoDispose")
    private void combineLatestTest() {
        Observable.combineLatest(
                Observable.intervalRange(1, 4, 1, 1, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                String s1 = "A" + aLong;
                                Log.e(TAG, "=======" + "A发送的事件：" + s1 + "=========");
                                return s1;
                            }
                        }),
                Observable.intervalRange(1, 5, 2, 2, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                String s2 = "B" + aLong;
                                Log.e(TAG, "=======" + "B发送的事件：" + s2 + "=========");
                                return s2;
                            }
                        }),
                new BiFunction<String, String, String>() {
                    @Override
                    public String apply(String s, String s2) throws Exception {
                        return s + s2;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "=======" + "onSubscribe：" + "=========");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "=======" + "onNext：" + s + "=========");
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

    /**
     * zip()操作符
     * 会将多个被观察者合并，根据各个被观察者发送事件的顺序一个个结合起来，最终发送的事件数量会与源 Observable 中最少事件的数量一样
     * 下面代码中有两个 Observable，第一个发送事件的数量为4个，第二个发送事件的数量为5个。
     * 可以发现最终接收到的事件数量是4，那么为什么第二个 Observable 没有发送第6个事件呢？因为在这之前第一个 Observable 已经发送了
     * onComplete 事件，所以第二个 Observable 不会再发送事件。
     */
    @SuppressLint("AutoDispose")
    private void zipTest() {
        Observable.zip(
                Observable.intervalRange(1, 4, 1, 1, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                String s1 = "A" + aLong;
                                Log.e(TAG, "=======" + "A发送的事件：" + s1 + "=========");
                                return s1;
                            }
                        }),
                Observable.intervalRange(1, 5, 2, 2, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                String s2 = "B" + aLong;
                                Log.e(TAG, "=======" + "B发送的事件：" + s2 + "=========");
                                return s2;
                            }
                        }),
                new BiFunction<String, String, String>() {
                    @Override
                    public String apply(String s, String s2) throws Exception {
                        return s + s2;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "=======" + "onSubscribe：" + "=========");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "=======" + "onNext：" + s + "=========");
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

    /**
     * intervalRange()操作符
     * 可以指定发送事件的开始值和数量，其他与 interval() 的功能一样
     * <p>
     * 从时间就可以看出每隔1秒就会发出一次数字递增1的事件。这里说下 intervalRange() 第三个方法的 initialDelay 参数，
     * 这个参数的意思就是 onSubscribe 回调之后，再次回调 onNext 的间隔时间。
     * 从结果可以看出收到5次 onNext 事件，并且是从 2 开始的。
     */
    @SuppressLint("AutoDispose")
    private void intervalRangeTest() {
        Observable.intervalRange(2, 5, 3, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "=======" + "onSubscribe：" + "=========");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e(TAG, "=======" + "onNext：" + aLong + "=========");
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

    /**
     * onErrorReturn()操作符
     * 当接受到一个 onError() 事件之后回调，返回的值会回调 onNext() 方法，并正常结束该事件序列。
     */
    @SuppressLint("AutoDispose")
    private void onErrorReturnTest() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        emitter.onError(new NullPointerException());
                    }
                })
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) throws Exception {
                        Log.e(TAG, "=======" + "onErrorReturn：" + throwable + "=========");
                        return 404;
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "=======" + "onSubscribe：" + "=========");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "=======" + "onNext：" + integer + "=========");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "=======" + "onError：" + e + "=========");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "=======" + "onComplete：" + "=========");
                    }
                });
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
}
