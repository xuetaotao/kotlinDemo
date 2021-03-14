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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

//        runSynchronized1Demo();
//        runSynchronized2Demo();
        runSynchronized3Demo();
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
     * <p>
     * 保证加了 volatile 关键字的字段的操作具有同步性，以及对 long 和 double 的操作的原子性，因此 volatile 可以看做是简化版的
     * synchronized； volatile 只对基本类型（byte、char、short、int、long、float、double、boolean）的赋值操作和对象的
     * 引用赋值操作有效，你要修改 User.name 是不能保证同步的； volatile 依然解决不了 ++原子性的问题
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
                        Log.e("hhh", "hhh");
                    }
                }
            }.start();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop();
        }
    }


    private void runSynchronized2Demo() {
        new Synchronized2Demo().runTest();
    }

    static class Synchronized2Demo implements ThreadDemp {

        private int x = 0;

        private synchronized void count() {
            x++;//非原子操作
        }

        @Override
        public void runTest() {
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 1_000_000; i++) {
                        count();
                    }
                    Log.e(TAG, "final x from 1:" + x);
                }
            }.start();
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 1_000_000; i++) {
                        count();
                    }
                    Log.e(TAG, "final x from 2:" + x);
                }
            }.start();
        }
    }


    private void runSynchronized3Demo() {
        new Synchronized3Demo().runTest();
    }

    static class Synchronized3Demo implements ThreadDemp {

        private int x = 0;
        private int y = 0;
        private String name;
        private final Object monitor1 = new Object();//使用最简单最小的对象来做锁
        private final Object monitor2 = new Object();

        //以下三个方法实现线程安全的方式：
        //1.通过在方法名后加上 synchronized 关键字，那么将为这三个方法加上同一个 monitor(监视器，即对象锁)

//        private synchronized void count(int newCount) {
//            x = newCount;
//            y = newCount;
//        }

        //该种形式与上面一样，都是加的对象锁
//        private void count(int newCount) {
//            synchronized (this) {
//                x = newCount;
//                y = newCount;
//            }
//        }

        private void count(int newCount) {
            synchronized (monitor1) {
                x = newCount;
                y = newCount;
            }
        }

//        private static void count(int newCount) {
//            synchronized (Synchronized3Demo.class) {
//                x = newCount;
//                y = newCount;
//            }
//        }

        //该方法与上面方法的锁一样，都是类锁
//        private synchronized static void count(int newCount) {
//            x = newCount;
//            y = newCount;
//        }

        private void minus(int delta) {
            synchronized (monitor1) {
                x -= delta;
                y -= delta;
            }
        }

        private void setName(String newName) {
            synchronized (monitor2) {
                name = newName;
            }
        }

        @Override
        public void runTest() {

        }
    }

    /**
     * 单例模式
     */
    static class SingleMan {
        private static volatile SingleMan INSTANCE;//避免构造方法还没调用完，就可以获取到对象了

        private SingleMan getInstance() {
            if (INSTANCE == null) {
                synchronized (SingleMan.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new SingleMan();
                    }
                }
            }
            return INSTANCE;
        }

        private SingleMan() {

        }
    }


    static class ReadWriteLockDemo implements ThreadDemp {

        private int x = 0;
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        Lock readLock = lock.readLock();
        Lock writeLock = lock.writeLock();

        private void count1() {
            lock1.lock();
            try {
                x++;
            } finally {
                lock1.unlock();
            }
        }

        private void count() {
            writeLock.lock();
            try {
                x++;
            } finally {
                writeLock.unlock();
            }
        }

        private void printHHH() {
            readLock.lock();
            try {
                Log.e(TAG, x + "");
            } finally {
                readLock.lock();
            }
        }

        @Override
        public void runTest() {

        }
    }

    /**********************************************************************/
}
