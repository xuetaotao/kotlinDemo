package com.jlpay.kotlindemo.ui.base;

import androidx.lifecycle.LifecycleOwner;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface BaseView {

    <T> LifecycleTransformer<T> getActivityLifecycleProvider();

    LifecycleOwner getLifecycleOwner();

//    void showToast();
}
