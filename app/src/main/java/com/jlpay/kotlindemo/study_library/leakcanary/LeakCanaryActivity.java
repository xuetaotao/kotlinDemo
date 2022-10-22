package com.jlpay.kotlindemo.study_library.leakcanary;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jlpay.kotlindemo.R;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import leakcanary.LeakCanary;

/**
 * LeakCanary
 */
public class LeakCanaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leakcanary);
    }

    public void leakCanaryStudy(View view) {
        Toast.makeText(this, "leakCanaryStudy", Toast.LENGTH_SHORT).show();
        LeakCanary leakCanary = LeakCanary.INSTANCE;
//        AppWatcherInstaller.onCreate();//入口函数
    }

    /**
     * 原理一
     * Activity和Fragment生命周期监控
     * 把这部分代码在Application里面调用就可以了。
     */
    public void watchActivityAndFragment(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                //注：这样做其实有个小问题，就是直接继承自Activity的activity就无法监测了。
                watchFragmentLifeCycle((AppCompatActivity) activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                //监控目标人群，这里的都是我们要实行监控的目标对象。
                //接下来要在这里锁定目标个体（也就是内存泄漏的对象，即没有被回收的），通过可达性分析算法的GCRoots
                //Activity Destroy回调监听，过5s后做个GC，通过WeakReference看它有没有销毁
                ReferenceQueue<Activity> referenceQueue = new ReferenceQueue<>();
                WeakReference<Activity> weakReference = new WeakReference<Activity>(activity, referenceQueue);

                //可以先申请一块大内存，促进GC
                //手动触发GC，GC耗资源，耗时，卡顿，会影响启动，影响App性能
                Runtime.getRuntime().gc();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Reference<Activity> poll;
                //这个等价于weakReference.get() == null
                while ((poll = (Reference<Activity>) referenceQueue.poll()) != null) {
                    Log.e("TAG", "onActivityDestroyed: 回收了" + poll);
                }
            }
        });
    }

    /**
     * 监控 Fragment 生命周期
     */
    void watchFragmentLifeCycle(AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.registerFragmentLifecycleCallbacks(new WatchFragmentLifecycleCallbacks(), true);
    }


    static class WatchFragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks {

        private final String TAG = WatchFragmentLifecycleCallbacks.class.getSimpleName();

        @Override
        public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
            Log.e(TAG, "onFragmentCreated: ");
        }

        @Override
        public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentViewDestroyed(fm, f);
            //监控目标人群，在这里监控Fragment的View是否释放
            Log.e(TAG, "onFragmentViewDestroyed: ");
        }

        @Override
        public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
            super.onFragmentDestroyed(fm, f);
            //监控目标人群，在这里监控Fragment是否释放
            Log.e(TAG, "onFragmentDestroyed: ");
        }
    }

    /**
     * 原理二
     * 四大基本引用，详见ReferenceActivity
     */
    public void weakReferenceStudy(Activity activity) throws InterruptedException {
        ReferenceQueue<Activity> referenceQueue = new ReferenceQueue<Activity>();
        WeakReference<Activity> weakReference = new WeakReference<Activity>(activity, referenceQueue);
        Activity myActivity = weakReference.get();
        //System.gc()并不会每次都引起GC，Runtime.getRuntime().gc()会更有效，但是也不一定真正GC
        //思路：可以在GC前申请大的byte内存，促进GC
//        System.gc();
        Runtime.getRuntime().gc();
        Thread.sleep(1000);
    }

    /**
     * 链接：https://juejin.cn/post/6872151038305140744
     * <p>
     * WeakHashMap最大的特点是其key对象被自动弱引用，可以被回收，利用这个特点，
     * 用其key监听Activity回收就能达到泄露监测的目的
     * <p>
     * 线上选择监测没必要实时，将其延后到APP进入后台的时候，在APP进入后台之后主动触发一次GC，然后延时10s，
     * 进行检查，之所以延时10s，是因为GC不是同步的，为了让GC操作能够顺利执行完，这里选择10s后检查。
     * 在检查前分配一个4M的大内存块，再次确保GC执行，之后就可以根据WeakHashMap的特性，
     * 查找有多少Activity还保留在其中，这些Activity就是泄露Activity
     * <p>
     * https://blog.csdn.net/qiuhao9527/article/details/80775524
     * WeakHashMap的键是“弱键”。在 WeakHashMap 中，当某个键不再正常使用时，会被从WeakHashMap中被自动移除。
     * WeakHashMap的key是“弱键”，即是WeakReference类型的；ReferenceQueue是一个队列，它会保存被GC回收的“弱键”。
     */
    public void weakHashMapDemo(Activity activity) {
        WeakHashMap<Activity, String> weakHashMap = new WeakHashMap<>();
        //放入map，进行监听
        weakHashMap.put(activity, activity.getClass().getSimpleName());
        Runtime.getRuntime().gc();
        //申请个稍微大的对象，促进GC
        byte[] leakHelpBytes = new byte[4 * 1024 * 1024];
        for (int i = 0; i < leakHelpBytes.length; i += 1024) {
            leakHelpBytes[i] = 1;
        }
        Runtime.getRuntime().gc();
        SystemClock.sleep(100);
        System.runFinalization();
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (Map.Entry<Activity, String> activityStringEntry : weakHashMap.entrySet()) {
            String name = activityStringEntry.getKey().getClass().getName();
            Integer value = hashMap.get(name);
            if (value == null) {
                hashMap.put(name, 1);
            } else {
                hashMap.put(name, value + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            //这里也可以搞个回调Listener回调出去
            System.out.println(entry.getKey() + entry.getValue());
        }
    }


    /**
     * 技术点三：ContentProvider中启动LeakCanary
     * Application.attachBaseContext()-->ContentProvider.onCreate()-->Application.onCreate()
     */
}
