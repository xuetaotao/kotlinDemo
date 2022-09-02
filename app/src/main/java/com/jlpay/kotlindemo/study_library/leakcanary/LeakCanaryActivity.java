package com.jlpay.kotlindemo.study_library.leakcanary;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
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

                //手动触发GC，GC耗资源，耗时，卡顿，会影响启动，影响App性能
                Runtime.getRuntime().gc();

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
     * 技术点三：ContentProvider中启动LeakCanary
     * Application.attachBaseContext()-->ContentProvider.onCreate()-->Application.onCreate()
     */
}
