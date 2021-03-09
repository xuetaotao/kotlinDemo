package com.jlpay.kotlindemo.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadActivity extends AppCompatActivity {

    private static final String TAG = ThreadActivity.class.getSimpleName();

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, ThreadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

//        thread();
//        runnable();
//        threadFactory();
//        executor();
//        callable();

        runSynchronized1Demo();
    }


    /*****************多线程的使用*****************************************************/
    /**
     * Thread
     */
    private static void thread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
//                super.run();
                Log.e(TAG, "thread() started!");
            }
        };
        thread.start();
    }


    /**
     * Runnable
     */
    private static void runnable() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "runnable() started!");
            }
        };
        new Thread(runnable).start();
    }

    /**
     * ThreadFactory
     */
    private static void threadFactory() {
        ThreadFactory threadFactory = new ThreadFactory() {
            AtomicInteger atomicInteger = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Thread-" + atomicInteger.incrementAndGet());//++count
            }
        };

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "threadFactory() started!");
            }
        };

        Thread thread = threadFactory.newThread(runnable);
        thread.start();
        Thread thread1 = threadFactory.newThread(runnable);
        thread1.start();
    }

    /**
     * Executor和线程池
     * 常用：newCachedThreadPool()
     */
    private static void executor() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "executor() started!");
            }
        };

        Executor executor = Executors.newCachedThreadPool();
        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);
    }

    /**
     * Callable和 Future
     */
    private static void callable() {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "Done!";
            }
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(callable);
        try {
            String result = future.get();//这里主线程会阻塞，等到子线程返回结果
            Log.e(TAG, "callable() started!");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**********************************************************************/


    /*****************线程同步与线程安全*****************************************************/

    private void runSynchronized1Demo() {
        new Synchronized1Demo().runTest();
    }

    /**
     * 内部静态类不需要有指向外部类的引用。但非静态内部类需要持有对外部类的引用。非静态内部类能够访问外部类的静态和非静态成员。
     * 静态类不能访问外部类的非静态成员。他只能访问外部类的静态成员。一个非静态内部类不能脱离外部类实体被创建，
     * 一个非静态内部类可以访问外部类的数据和方法，因为他就在外部类里面
     */
    static class Synchronized1Demo implements ThreadDemp {

        private volatile boolean running = true;

        private void stop() {
            running = false;
        }

        //不需要外部类的引用，可以直接调用
        private static void hhh() {
            Log.e("hhh", "hhh");
        }

        @Override
        public void runTest() {
            new Thread() {
                @Override
                public void run() {
                    while (running) {

                    }
                }
            }.start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop();
        }
    }


    /**********************************************************************/
}
