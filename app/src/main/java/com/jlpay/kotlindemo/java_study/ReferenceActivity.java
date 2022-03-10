package com.jlpay.kotlindemo.java_study;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * 强/软/弱/虚 四大引用学习
 */
public class ReferenceActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
     */
    private void WeakReferenceDemo() {
        WeakReference<Integer> weakReference = new WeakReference<Integer>(3);
    }

    /**
     * 虚引用
     * 虚引用是最弱的一种引用关系，如果一个对象仅持有虚引用，那么它就和没有任何引用一样，它随时可能会被回收
     */
    private void PhantomReferenceDemo() {
        ReferenceQueue<Integer> referenceQueue = new ReferenceQueue<Integer>();
        PhantomReference<Integer> phantomReference = new PhantomReference<>(3, referenceQueue);
    }

    /**
     * 软引用
     * 发生 GC 后不一定回收
     */
    private void SoftReferenceDemo() {
        SoftReference<Integer> softReference = new SoftReference<Integer>(3);
    }

    /**
     * 强引用
     * 只要有一个引用（栈）指向其（堆）上的实例，那堆上的实例就不会被回收
     */
    private void StrongReferenceDemo() {
        Object obj = new Object();
        Log.e(TAG, "StrongReferenceDemo: " + "this is a StrongReference");
    }
}
