package com.jlpay.kotlindemo.ui.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface BaseView {

    <T> LifecycleTransformer<T> getActivityLifecycleProvider();

//    void showToast();
}
