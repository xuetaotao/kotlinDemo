package com.jlpay.kotlindemo.ui.main.rxjava;

import com.jlpay.kotlindemo.ui.base.BasePresenter;
import com.jlpay.kotlindemo.ui.base.BaseView;

public class RxJavaContract {

    interface View extends BaseView {

        void onNetRequest();
    }

    interface Presenter extends BasePresenter {

        void netRequest();
    }

}
