package com.jlpay.kotlindemo.study_android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * ViewGroup.java类中只分发事件：   ViewGroup.dispatchTouchEvent()--->实现了分发流程的逻辑
 * <p>
 * 与事件分发相关联的三个方法分别为dispatchTouchEvent，onInterceptTouchEvent， onTouchEvent
 */
public class ViewEventDispatchViewGroup extends LinearLayout {

    private String TAG = ViewEventDispatchViewGroup.class.getSimpleName();

    public ViewEventDispatchViewGroup(Context context) {
        this(context, null);
    }

    public ViewEventDispatchViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewEventDispatchViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //ViewGroup中的事件传递流程
    //第一步 实现了事件分发的逻辑
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //在ViewGroup的dispatchTouchEvent()方法中首先会调用onInterceptTouchEvent()判断是否需要拦截当前事件
        //true, 当前的dispatchTouchEvent消费，停止传递
        //false, 父View的onTouchEvent消费
        Log.e(TAG, "父容器--->dispatchTouchEvent: " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    //第二步
    //外部拦截法一般只需要在父容器处理,根据业务需求返回true或者false
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //返回true表示当前ViewGroup拦截当前事件，不在向下传递，并调用onTouchEvent()方法
        //返回false时，交给子view的dispatchTouchEvent()
        //内部拦截法步骤二
        //ACTION_DOWN不能拦截，否则子View的requestDisallowInterceptTouchEvent()方法无效
//        if (ev.getAction()==MotionEvent.ACTION_DOWN){
//            super.onInterceptTouchEvent(ev);
//            return false;
//        }
        Log.e(TAG, "父容器--->onInterceptTouchEvent: " + ev.getAction());
        return super.onInterceptTouchEvent(ev);
    }

    //第三步
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果onTouchEvent()返回true表示当前ViewGroup消费当前事件，终止事件传递
        //如果onTouchEvent()返回false时，则调用当前ViewGroup的parent View的onTouchEvent()方法
        //同理如果当前ViewGroup的parent View的onTouchEvent()方法放回true，则表示该view消费当前事件，并且终止事件传递，
        //反之如果当前ViewGroup的parent View的onTouchEvent()方法放回false，则继续向上传递事件
        Log.e(TAG, "父容器--->onTouchEvent: " + event.getAction());
        return super.onTouchEvent(event);
    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//
//    }
}
