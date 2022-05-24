package com.jlpay.kotlindemo.library_study.rxjava;

public class RxAutoDisposeContract {

    interface View extends BaseNewView {
        void onNetRequest(long aLong);
    }

    interface Presenter extends IPresenter {
        void netRequest();

        void anotherNetRequest();
    }
}
