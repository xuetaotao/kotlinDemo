package com.jlpay.kotlindemo.study_java;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 线程相关概念：
 * 死锁：
 * 通俗理解：
 * 1、多个操作者（M>=2）争夺多个资源（N>=2），N<=M
 * 2、争夺资源的顺序不对
 * 3、拿到资源不放手
 * 学术理解：1、互斥条件  2、请求保持  3、不剥夺  4、环路等待
 * <p>
 * CAS: Compare and Swap
 * 问题：1、ABA问题  2、忘了  3、忘了
 * <p>
 * 悲观锁：synchronized
 * 乐观锁：CAS
 * <p>
 * AtomicInteger：
 * 1、乐观锁，性能较强，利用CPU自身的特性保证原子性，即CPU的指令集封装compare and swap两个操作为一个指令来保证原子性  2、适合读多写少模式
 */
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

        //多线程的使用
//        thread();
//        runnable();
//        threadFactory();
//        executor();
//        callable();

        //线程间通信
//        new ThreadInteractionDemo().runTest();

        //Fork-Join
//        forkJoinDemo();

        //CyclicBarrier
//        CyclicBarrierDemo();

//        SemaphoreDemo();

//        ExchangerDemo();

//        AbstractQueuedSynchronizerDemo();
    }

    /**
     * AQS 学习
     * 可以用来自定义锁
     */
    private void AbstractQueuedSynchronizerDemo() {
        AbstractQueuedSynchronizer aqs = new AbstractQueuedSynchronizer() {

        };
    }

    /**
     * 用处不大，仅用于两个线程交换数据
     * TODO
     */
    private void ExchangerDemo() {
        Exchanger<String> exchanger = new Exchanger<>();
    }

    /**
     * Semaphore 信号灯
     * 主要作用：流控
     * TODO
     */
    private void SemaphoreDemo() {
        Semaphore semaphore = new Semaphore(3);
    }

    /**
     * CyclicBarrier
     * 主要作用：有点像RxJava的 zip操作符，就是几个线程并发执行，然后等全部执行完拿到结果后，在执行后面的操作
     * TODO
     */
    private void CyclicBarrierDemo() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    /**
     * Random 学习
     */
    private void randomDemo() {
        Random random = new Random();
        int i = random.nextInt(100);//通过Random对象获取,[0, 100)之间的int整数
    }

    /**
     * Fork-Join 学习
     * 重要的类：
     * ForkJoinTask  RecursiveAction  RecursiveTask ForkJoinPool
     * TODO
     */
    private void forkJoinDemo() {
        //------------------一些测试代码-------------------------//

        RecursiveAction recursiveAction = new RecursiveAction() {
            @Override
            protected void compute() {
                //没有返回值
            }
        };
        RecursiveTask<String> recursiveTask = new RecursiveTask<String>() {
            @Override
            protected String compute() {
                //有返回值
                return null;
            }
        };

        //-------------------下面才是使用-------------------------//
        //new 出池的实例
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //下面的代码暂时先不敲了，用到再学习

    }

    /**
     * CountDownLatch 的学习
     * https://blog.csdn.net/hbtj_1216/article/details/109655995
     * 主要作用：当某个线程，需要等待其他线程执行完毕后再去执行，就可以使用 CountDownLatch
     * 注：1、等待线程可以有多个；2、子线程数目不一定和CountDownLatch的扣减数一致；3、子线程countDown()执行后仍然可以继续运行；
     * <p>
     * CountDownLatch 提供了一些方法：
     * await()：使当前线程进入同步队列进行等待，直到latch的值被减到0或者当前线程被中断，当前线程就会被唤醒。
     * countDown()：使latch的值减1，如果减到了0，则会唤醒所有等待在这个latch上的线程。
     * getCount()：获得latch的数值。
     */
    public void countDownLatchClick(View view) {
        countDownLatchDemo();
    }

    private static CountDownLatch countDownLatch;

    private void countDownLatchDemo() {
        countDownLatch = new CountDownLatch(6);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Log.e(TAG, "Thread: " + Thread.currentThread() + "\n" + Thread.currentThread().getId() + "\t" + "start step1 work");
                    countDownLatch.countDown();
                    Log.e(TAG, "Thread: " + Thread.currentThread() + "\n" + Thread.currentThread().getId() + "\t" + "begin step2 work");
                    Thread.sleep(1000);
                    Log.e(TAG, "Thread: " + Thread.currentThread() + "\n" + Thread.currentThread().getId() + "\t" + "start step2 work");
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new BusinessThread()).start();
        for (int i = 0; i < 4; i++) {
            new Thread(new InitThread()).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //BusinessThread和MainThread都有await等待，它们两被唤醒执行的先后顺序是随机的
        Log.e(TAG, "countDownLatchDemo: " + Thread.currentThread() + "\n" + Thread.currentThread().getId() + "\t" + "do main work");
    }

    //初始化线程
    private static class InitThread implements Runnable {
        @Override
        public void run() {
            Log.e(TAG, "InitThread: " + Thread.currentThread() + "\n" + Thread.currentThread().getId() + "\t" + "ready init work");
            countDownLatch.countDown();
            for (int i = 0; i < 1; i++) {
                Log.e(TAG, "InitThread: " + Thread.currentThread() + "\n" + Thread.currentThread().getId() + "\t" + "continue do its work");
            }
        }
    }

    //业务线程 等待latch的计数器为0时完成
    private static class BusinessThread implements Runnable {
        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 1; i++) {
                Log.e(TAG, "BusinessThread: " + Thread.currentThread() + "\n" + Thread.currentThread().getId() + "\t" + "do business work");
            }
        }
    }

    /**
     * 1.ThreadLocal是一个线程内部的存储类，可以在指定线程内存储数据，数据存储以后，只有指定线程可以得到存储数据。
     * 2.ThreadLocal提供了线程内存储变量的能力，这些变量不同之处在于每一个线程读取的变量是对应的互相独立的。
     * 通过get和set方法就可以得到当前线程对应的值。
     * 3.ThreadLocal的静态内部类ThreadLocalMap为每个Thread都维护了一个数组table，ThreadLocal确定了一个数组下标，
     * 而这个下标就是value存储的对应位置。
     * 4.每个线程持有一个ThreadLocalMap对象,每一个新的线程Thread都会实例化一个ThreadLocalMap并赋值给成员
     * 变量threadLocals，使用时若已经存在threadLocals则直接使用已经存在的对象。
     * 5.对于某一ThreadLocal来讲，他的索引值i是确定的，在不同线程之间访问时访问的是不同的table数组的同一位置
     * 即都为table[i]，只不过这个不同线程之间的table是独立的
     * 6.对于同一线程的不同ThreadLocal来讲，这些ThreadLocal实例共享一个table数组，然后每个ThreadLocal实例
     * 在table中的索引i是不同的。
     * 7.ThreadLocal是通过每个线程单独一份存储空间，牺牲空间来解决冲突，并且相比于Synchronized，ThreadLocal
     * 具有线程隔离的效果，只有在线程内才能获取到对应的值，线程外则不能访问到想要的值。
     * 8.应用场景：当某些数据是以线程为作用域并且不同线程具有不同的数据副本的时候，就可以考虑采用ThreadLocal
     */
    private int countNum = 0;

    public void threadLocalClick(View view) {
        ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
            @Nullable
            @Override
            protected Integer initialValue() {
                return -1;
            }
        };
        if (countNum != 0) {
            threadLocal.set(countNum++);
        } else {
            countNum++;
        }
        Log.e(TAG, "threadLocalDemo的值是:\t" + threadLocal.get() + "\n" +
                "当前线程是：\t" + Thread.currentThread().getName());
        Toast.makeText(this, "threadLocalDemo的值是:\t" + threadLocal.get() + "\n" +
                "当前线程是：\t" + Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();

        //新开一个子线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "threadLocalDemo的值是1-->:\t" + threadLocal.get() + "\n" +
                        "当前线程是1-->：\t" + Thread.currentThread().getName());
            }
        });
        thread.start();


//        threadLocal.remove();//将当前 threadLocal 所指向的 value 一起从内存中清除，避免内存泄露
        //另外，threadLocal 也存在线程不安全的问题，例子暂时略过
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
//        设置为守护线程，守护线程中的finally里的程序不一定起作用，
//        守护线程可以自动结束生命周期（当除守护线程之外的所有线程都结束的时候），常用的守护线程如GC
//        thread.setDaemon(true);
        Thread thread1 = threadFactory.newThread(runnable);
        thread1.start();
    }

    /**
     * Executor和线程池
     * 常用：newCachedThreadPool()
     * <p>
     * 彻底搞懂Java线程池的工作原理
     * https://mp.weixin.qq.com/s/tyh_kDZyLu7SDiDZppCx5Q
     * Java中的线程池有哪些
     * https://baijiahao.baidu.com/s?id=1710203886182271419&wfr=spider&for=pc
     */
    private static void executor() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "executor() started!");
            }
        };

        //创建一个定长线程池,corePoolSize = maximumPoolSize = 传入值
        //每当提交一个任务就创建一个工作线程，当线程 处于空闲状态时，它们并不会被回收，除非线程池被关闭了，
        // 如果工作线程数量达到线程池初始的最大数，则将提交的任务存入到池队列(没有大小限制)中。
        // 由于newFixedThreadPool只有核心线程并且这些核心线程不会被回收，这样它更加快速底相应外界的请求
        ExecutorService executorService1 = Executors.newFixedThreadPool(3);

        //创建一个定长线程池,corePoolSize = 传入值,主要用于执行定时任务和具有固定周期的重复任务
        ExecutorService executorService2 = Executors.newScheduledThreadPool(3);

        //创建一个单线程化的线程池
        //内部只有一个核心线程，以无界队列方式来执行该线程，这使得这些任务之间不需要处理线程同步的问题
        ExecutorService executorService3 = Executors.newSingleThreadExecutor();

        //创建一个可缓存线程池程,适合执行大量的耗时较少的任务，当整个线程池都处于闲置状态时，线程池中的线程都会超时被停止
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(runnable);// 使用线程池执行一个任务,execute 提交的线程任务不关心返回值
        executor.execute(runnable);
        executor.execute(runnable);
//        executor.shutdown();// 关闭线程池,会阻止新任务提交，但不影响已提交的任务
//        executor.shutdownNow();// 关闭线程池，阻止新任务提交，并且中断当前正在运行的线程(不一定能中断，除非自己处理了，因为Java的线程是协作式的)
    }

    /**
     * ThreadPoolExecutor
     * 构造方法中各个参数的意义：
     * ThreadPoolExecutor(
     * int corePoolSize,  核心线程数
     * int maximumPoolSize, 最大线程数
     * long keepAliveTime,空闲线程的存活时间
     * TimeUnit unit,时间单位
     * BlockingQueue<Runnable> workQueue,阻塞队列，用来存放暂时来不及处理的任务；当核心线程占用完后就开始往这里放，这里放满
     * 了，会再开线程一直到最大线程数
     * ThreadFactory threadFactory,在线程创建的时候做一点微调的工作，如命名等
     * RejectedExecutionHandler handler),拒绝策略:丢弃最老的任务，直接抛出异常（默认），让调用者线程自己去执行任务，丢弃最新提交的任务
     */
    private void threadPoolExecutorDemo() {
        //获取CPU核心数
        int processors = Runtime.getRuntime().availableProcessors();

        ThreadFactory sThreadFactory = new ThreadFactory() {

            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "MySyncTask #" + mCount.getAndIncrement());
            }
        };

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                3, 10, 60, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), sThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * Callable和 Future
     * Callable:相对于Runnable，可以有返回值
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
        Future<String> future = executorService.submit(callable);//submit提交的线程任务关心返回值
        try {
            String result = future.get();//这里主线程会阻塞，等到子线程返回结果
            Log.e(TAG, "callable() started!");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * FutureTask 本身就是一个 Runnable
     */
    private void futureTaskDemo() {
        //1.FutureTask 作为Runnable，交给线程Thread来执行
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hhh";
            }
        };
        FutureTask<String> futureTask = new FutureTask<String>(callable);
        new Thread(futureTask).start();

        //2.FutureTask 作为 Future，拿到执行结果
        FutureTask<String> futureTask2 = new FutureTask<String>(new Runnable() {
            @Override
            public void run() {

            }
        }, "hhh");
        try {
            String result = futureTask2.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**********************************************************************/


    /*****************线程同步与线程安全*****************************************************/

    public void synchronizedClick(View view) {
        runSynchronized1Demo();
//        runSynchronized2Demo();
//        runSynchronized3Demo();
    }


    private void runSynchronized1Demo() {
        new Synchronized1Demo().runTest();
    }

    /**
     * 内部静态类不需要有指向外部类的引用。但非静态内部类需要持有对外部类的引用。非静态内部类能够访问外部类的静态和非静态成员。
     * 静态类不能访问外部类的非静态成员。他只能访问外部类的静态成员。一个非静态内部类不能脱离外部类实体被创建，
     * 一个非静态内部类可以访问外部类的数据和方法，因为他就在外部类里面
     * <p>
     * 保证加了 volatile 关键字的字段的操作具有可见性（同步性），以及对 long 和 double 的操作的原子性，因此 volatile 可以看做是简化版的
     * synchronized； volatile 只对基本类型（byte、char、short、int、long、float、double、boolean）的赋值操作和对象的
     * 引用赋值操作有效，你要修改 User.name 是不能保证同步的； volatile 依然解决不了 ++原子性的问题
     * <p>
     * volatile 只保证可见性，适用场景是：一写多读的场景
     */
    static class Synchronized1Demo implements ThreadDemo {

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

    static class Synchronized2Demo implements ThreadDemo {

        private int x = 0;

        //如果这个地方使用 x 来作为锁的话就会出问题，因为 x++ 方法会 new 出新的对象 x，造成锁的对象发生变化，进而导致并发
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

    static class Synchronized3Demo implements ThreadDemo {

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

        //如果下面的方法使用这个monitor3作为类锁，那么是可以和上面的方法并行的，因为这两个是不同的类，即不同的类锁
        private static Object monitor3 = new Object();
        //该方法与上面方法的锁一样，都是类锁，类锁本质上应该说是Class对象锁，因为每个类的Class对象只有一个，所以说是类锁
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
     * volatile 用来防止CPU将指令重排序
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

    /**
     * Lock 学习
     * Lock 可以尝试拿锁，而且拿到了锁可以释放锁，从而避免死锁问题
     */
    static class ReadWriteLockDemo implements ThreadDemo {

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

    /************************线程间通信**********************************************/

    static class ThreadInteractionDemo implements ThreadDemo {
        @Override
        public void runTest() {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 1_000_000; i++) {
//                        if (Thread.interrupted()) {//会把中断标志位重置为 false，故而只能判断一次
                        if (isInterrupted()) {//检查线程中断状态，一般在耗时操作前来结束线程，可以判断多次
                            Log.e(TAG, "线程终止");
                            return;
                        }
                        Log.e(TAG, "线程运行中：" + i + "");

                        try {
                            Thread.sleep(10000);//也不会释放锁
                        } catch (InterruptedException e) {
                            //当一个线程想要在线程睡眠的时候调用interrupt()结束当前线程，会触发这个异常，
                            // 中断状态不会置为true(因为这里会被重置为false)，只能在这里完成需要完成的工作
                            //如果线程没有中断，可以在catch里面做完资源释放以后,再调用interrupt()方法一次
                            //处于死锁状态的线程，不会理会中断
                            Log.e(TAG, "线程Exception中终止了");
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            thread.stop();//强制关闭线程，可能导致线程占用资源无法正常释放，不可预测，可能会崩溃
            thread.interrupt();//中断线程（实际上只是改了下线程的中断标志位），不是立即的，也不是强制的，需要线程自己去支持
        }
    }

    /**
     * 线程间通信
     * wait、NotifyAll、Join的使用
     * 1)wait()和notify()/notifyAll() 都需要放在同步代码块里,且它们两必须是同一个 monitor
     * wait:调用后会释放自己的锁，开始等待
     * notify()/notifyAll():调用后会唤醒其他等待的锁，但是不会释放锁
     * join():线程1插到主线程之前，通过join方法可以让线程之间的运行变为串行运行。被插入的线程必须等待插入线程执行完成才能继续执行。
     */
    public void waitNotifyAllJoinClick(View view) {
        new WaitDemo().runTest();
    }


    static class WaitDemo implements ThreadDemo {

        private String shareString;

        private synchronized void initString() {
            shareString = "rengwuxian";
            notifyAll();//会唤醒其他等待的锁，但是不会释放锁
            Log.e(TAG, "initString: notifyAll");
            Log.e(TAG, "initString: notifyAll2了");
        }

        private synchronized void printString() {
            //收到notify()/notifyAll()后，会重新竞争锁，然后继续执行后面的代码（从原来等待的地方），判断条件
            while (shareString == null) {
                try {
                    Log.e(TAG, "printString: 开始wait");
                    //调用wait方法后会释放它持有的锁
                    wait();//wait()和notify()/notifyAll() 都需要放在同步代码块里，且它们两必须是同一个 monitor，wait会释放自己的锁
                    Log.e(TAG, "printString: wait结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.e(TAG, "shareString is：" + shareString);
        }

        @Override
        public void runTest() {
            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        Log.e(TAG, "thread1: 睡醒了");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    yield();//暂时让出自己的时间片给同优先级的线程 ,让出CPU的执行期，但是不会释放已经拿到的锁
                    Log.e(TAG, "thread1: printString");
                    printString();
                }
            };
            thread1.start();
//            thread1.setPriority(6);//设置线程优先级，默认5，范围1～10，10优先级最大，但是能否发挥作用由操作系统决定

            Thread thread2 = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        Log.e(TAG, "thread2: 睡醒了");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "thread2: initString");
                    initString();
                }
            };
            thread2.start();
            try {
                thread1.join();//线程1插到主线程之前，通过join方法可以让线程之间的运行变为串行运行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "主线程完了");
        }
    }

    /**
     * sleep
     */
    public void sleepClick(View view) {
        try {
            Thread.sleep(3000);//也不会释放锁
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**********************************************************************/

    /*************************Android的多线程*********************************************/

    static class CustomerThread extends Thread {
        private CustomerLooper looper = new CustomerLooper();

        @Override
        public void run() {
            looper.loop();
        }

        static class CustomerLooper {
            private Runnable task;
            private AtomicBoolean quit = new AtomicBoolean(false);

            synchronized void setTask(Runnable task) {
                this.task = task;
            }

            void quit() {
                quit.set(true);
            }

            void loop() {
                while (!quit.get()) {
                    synchronized (this) {
                        if (task != null) {
                            task.run();
                            task = null;
                        }
                    }
                }
            }
        }
    }


    /**********************************************************************/
}
