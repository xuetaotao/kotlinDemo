package com.jlpay.kotlindemo.android_study;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * 与事件分发相关联的三个方法分别为dispatchTouchEvent，onInterceptTouchEvent， onTouchEvent
 */
public class ViewEventDispatchViewGroup extends ViewGroup {

    private String TAG = ViewEventDispatchViewGroup.class.getSimpleName();

    public ViewEventDispatchViewGroup(Context context) {
        super(context);
    }

    public ViewEventDispatchViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewEventDispatchViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewEventDispatchViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: " + ev.getAction());
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onInterceptTouchEvent: " + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
