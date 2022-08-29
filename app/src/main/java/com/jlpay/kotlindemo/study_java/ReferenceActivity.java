package com.jlpay.kotlindemo.study_java;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 强/软/弱/虚 四大引用学习
 */
public class ReferenceActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void referenceStudy(View view) {
//        Toast.makeText(this, "referenceStudy", Toast.LENGTH_SHORT).show();
        WeakReferenceDemo();
//        StrongReferenceDemo();
//        SoftReferenceDemo();
//        PhantomReferenceDemo();
    }


    /**
     * 弱引用
     * 弱引用的引用强度比软引用要更弱一些，无论内存是否足够，只要 JVM 开始进行垃圾回收，那些被弱引用关联的对象都会被回收
     * <p>
     * ReferenceQueue(引用队列):
     * 引用队列可以与软引用、弱引用以及虚引用一起配合使用，当垃圾回收器准备回收一个对象时，如果发现它还有引用，
     * 那么就会在回收对象之前，把这个引用加入到与之关联的引用队列中去。程序可以通过判断引用队列中是否已经加入了引用，
     * 来判断被引用的对象是否将要被垃圾回收，这样就可以在对象被回收之前采取一些必要的措施
     * TODO 有空研究一下ReferenceQueue的源码
     */
    private void WeakReferenceDemo() {
        String string = new String("hello");
        String string2 = new String("world");
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();
        WeakReference<String> weakReference = new WeakReference<>(string, referenceQueue);
        WeakReference<String> weakReference2 = new WeakReference<>(string2, referenceQueue);
//        Log.e(TAG, "WeakReferenceDemo: string==" + string);//string==hello
//        Log.e(TAG, "WeakReferenceDemo: string2==" + string);//string==hello
//        Log.e(TAG, "WeakReferenceDemo: weakReference==" + weakReference.get());//weakReference==hello
//        Log.e(TAG, "WeakReferenceDemo: weakReference2==" + weakReference.get());//weakReference==hello

        string = null;
        string2 = null;
//        Log.e(TAG, "WeakReferenceDemo2: string==" + string);//string==null
//        Log.e(TAG, "WeakReferenceDemo2: string2==" + string2);//string==null
        Log.e(TAG, "WeakReferenceDemo2: weakReference==" + weakReference.get());//weakReference==hello
        Log.e(TAG, "WeakReferenceDemo2: weakReference2==" + weakReference2.get());//weakReference==hello
//        Log.e(TAG, "WeakReferenceDemo2: referenceQueue==" + referenceQueue.poll());//null

        //System.gc()并不会每次都引起GC，Runtime.getRuntime().gc()会更有效，但是也不一定真正GC
        //这里经过多次测试System.gc()都没有触发GC回收，反倒是Runtime.getRuntime().gc()更好使
//        System.gc();
        Runtime.getRuntime().gc();
        Log.e(TAG, "WeakReferenceDemo: 调用GC回收");
//        Log.e(TAG, "WeakReferenceDemo3: string==" + string);//string==null
//        Log.e(TAG, "WeakReferenceDemo3: string2==" + string2);//string==null
        //System.gc()并不会每次都引起GC，Runtime.getRuntime().gc()会更有效，但是也不一定真正GC。故而有个促进GC的思路是可以在GC前申请大的byte内存（不能过大，否则会OOM）
        Log.e(TAG, "WeakReferenceDemo3: weakReference==" + weakReference.get());
        Log.e(TAG, "WeakReferenceDemo3: weakReference2==" + weakReference2.get());
        WeakReference<String> poll;
        while ((poll = (WeakReference<String>) referenceQueue.poll()) != null) {
            Log.e(TAG, "WeakReferenceDemo3: referenceQueue==" + poll);//weakReference回收后这里不是立马有值的，下面延时后才看到
        }

        try {
            //改成3000会报这个错，原因：referenceStudy()方法中的Toast
            //出错来源在：ViewRootImpl.setView
            //Unable to add window -- token android.os.BinderProxy@26502d1 is not valid; is your activity running?
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //还是优点问题，看日志只回收了weakReference的对象，原因不知，有空再查吧
        Log.e(TAG, "WeakReferenceDemo: 延时3秒后");
        Log.e(TAG, "WeakReferenceDemo4: weakReference==" + weakReference.get());//weakReference==hello
        Log.e(TAG, "WeakReferenceDemo4: weakReference2==" + weakReference2.get());//weakReference==hello
        while ((poll = (WeakReference<String>) referenceQueue.poll()) != null) {
            //null，如果被回收了这里就会返回值 referenceQueue==java.lang.ref.WeakReference@ef09df8
            //这个poll就是回收的WeakReference引用对象，而不是WeakReference.get()的对象。
            Log.e(TAG, "WeakReferenceDemo4: referenceQueue==" + poll);
        }
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.e(TAG, "finalize: " + Thread.currentThread().getName());
    }

    //这个测试方法没成型，看看思路就好
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void WeakReferenceDemo2() {
//        WeakHashMap<String, String> weakHashMap = new WeakHashMap<String, String>();
//        WeakReference<Integer> weakReference = new WeakReference<Integer>(3);
        final int _80M = 80 * 1024 * 1024;//最基本的单位是字节，1M=1024KB=1024Byte
        List<WeakReference> list = new ArrayList<>();
        ReferenceQueue referenceQueue = new ReferenceQueue();
        list.add(new WeakReference(new byte[_80M], referenceQueue));
        Log.e(TAG, "WeakReferenceDemo: \"Run gc\"");
        System.gc();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list.stream().forEach(r -> Log.e(TAG, "WeakReferenceDemo: r：" + r.get()));
//        list.stream().forEach(new Consumer<WeakReference>() {
//            @Override
//            public void accept(WeakReference weakReference) {
//                Log.e(TAG, "accept: " + weakReference.get());
//            }
//        });
        Log.e(TAG, "WeakReferenceDemo: poll1：" + referenceQueue.poll());
        Log.e(TAG, "WeakReferenceDemo: poll2：" + referenceQueue.poll());
    }

    /**
     * 软引用
     * 是用来描述一些有用但并不是必须的对象。对于软引用关联着的对象，只有在内存不足的时候JVM才会回收该对象，
     * 也就是说发生 GC 后不一定回收。这一点可以很好的用来解决OOM的问题。
     * 并且这个特性很适合用来实现缓存：比如网页缓存，图片缓存。
     */
    private void SoftReferenceDemo() {
        Log.e(TAG, "SoftReferenceDemo: " + "this is a SoftReferenceDemo");
        Object a = new Object();
        SoftReference<Object> softReference = new SoftReference<Object>(a);
        Log.e(TAG, "SoftReferenceDemo1: a==" + a);//a==java.lang.Object@26502d1
        Log.e(TAG, "SoftReferenceDemo1: softReference==" + softReference.get());//softReference==java.lang.Object@26502d1

        a = null;
        Runtime.getRuntime().gc();
        Log.e(TAG, "SoftReferenceDemo2: a==" + a);//a==null
        Log.e(TAG, "SoftReferenceDemo2: softReference==" + softReference.get());//softReference==java.lang.Object@26502d1

        try {
            //为了效果明显一些，这里可以申请更大的内存(不太好弄，总是OOM)或者
            //配置Xms和Xmm为5MB（设置堆的大小：内存大小-Xmx（最大）/-Xms（最小） 使用示例: -Xmx20m -Xms5m 说明： 当下Java应用最大可用内存为20M， 最小内存为5M。)
            //这样下面就会触发GC回收内存了
            //申请200M内存：Caused by: java.lang.OutOfMemoryError: Failed to allocate a 209715212 byte allocation with 4929226 free bytes and 184MB until OOM
            byte[] bytes = new byte[200 * 1024 * 1024];//申请200M内存
            Log.e(TAG, "SoftReferenceDemo3: bytes" + bytes);//bytes[B@6b8d86a
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //如果触发了GC回收内存，那么a和softReference.get()都会变为null
            Log.e(TAG, "SoftReferenceDemo3: a==" + a);//a==null
            Log.e(TAG, "SoftReferenceDemo3: softReference==" + softReference.get());//未被回收，softReference==java.lang.Object@26502d1
        }
    }

    /**
     * 强引用
     * 只要有一个引用（栈）指向其（堆）上的实例，那堆上的实例就不会被回收。
     * 如果一个对象具有强引用，那就类似于必不可缺的物品，不会被垃圾回收器回收。当内存空间不足，Java虚拟机宁愿抛出
     * OutOfMemoryError错误，使程序异常终止，也不回收这种对象。
     */
    private void StrongReferenceDemo() {
        Log.e(TAG, "StrongReferenceDemo: " + "this is a StrongReference");
        Object a = new Object();
        //b并不是被创建的和a相同的对象,b就是a,b只是获得a指向的对象的引用
        //这个可以理解为引用为指针，都在左侧；对象为堆内存，都在右侧；引用都是指向右侧的指针。
        Object b = a;
        Log.e(TAG, "StrongReferenceDemo1: b==" + b);//b==java.lang.Object@26502d1
        Log.e(TAG, "StrongReferenceDemo1: a==" + a);//a==java.lang.Object@26502d1

        a = null;
        Log.e(TAG, "StrongReferenceDemo2: b==" + b);//b==java.lang.Object@26502d1
        Log.e(TAG, "StrongReferenceDemo2: a==" + a);//a==null

        b = null;
        Log.e(TAG, "StrongReferenceDemo3: b==" + b);//b==null
        Log.e(TAG, "StrongReferenceDemo3: a==" + a);//a==null
    }

    /**
     * 虚引用
     * 虚引用是最弱的一种引用关系，如果一个对象仅持有虚引用，那么它就和没有任何引用一样，它随时可能会被回收
     * 虚引用主要用来跟踪对象被垃圾回收的活动
     */
    private void PhantomReferenceDemo() {
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<String>();
        PhantomReference<String> phantomReference = new PhantomReference<>("hello", referenceQueue);
        Log.e(TAG, "PhantomReferenceDemo: " + phantomReference.get());//null
    }

}
