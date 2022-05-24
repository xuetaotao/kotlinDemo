package com.jlpay.kotlindemo.library_study.rxjava;

import com.jlpay.kotlindemo.base.BasePresenter;
import com.jlpay.kotlindemo.base.BaseView;

public class RxJavaContract {

    interface View extends BaseView {

        void onNetRequest();
    }

    interface Presenter extends BasePresenter {

        void netRequest();
    }

}
