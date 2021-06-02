package com.jlpay.kotlindemo.net;

import com.tencent.bugly.crashreport.CrashReport;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * 可参考：
 * https://juejin.cn/post/6844903806283563021
 */
public class ExceptionHandle {

    /**
     * 捕获RxJava未能处理的异常并上报Bugly
     */
    public static void rxjavaExceptionCapture() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                CrashReport.postCatchedException(new Exception("未处理的捕获异常：RxJavaPlugins：\nStackTrace:" + throwable.getStackTrace()
                        + "\nMessage:" + throwable.getMessage()));
            }
        });
    }
}
