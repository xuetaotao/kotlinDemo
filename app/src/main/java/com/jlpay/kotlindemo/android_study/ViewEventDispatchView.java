package com.jlpay.kotlindemo.android_study;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ViewEventDispatchView extends View {

    private String TAG = ViewEventDispatchView.class.getSimpleName();

    public ViewEventDispatchView(Context context) {
        super(context);
    }

    public ViewEventDispatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewEventDispatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewEventDispatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //View中事件传递流程:
    //第一步 只有事件的处理逻辑
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //true, 当前的dispatchTouchEvent消费，停止传递
        //false, 父View的onTouchEvent消费
        Log.e(TAG, "onInterceptTouchEvent: " + event.getAction());
        return super.dispatchTouchEvent(event);
    }

    //第二步
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果onTouchEvent()方法返回true，则表示当前View会消费该事件，事件传递终止。
        //如果返回false，则会回到当前View的parent View中并调用它的onTouchEvent()，
        // 如果onTouchEvent()返回true，则表示当前ViewGroup消费事件，终止事件传递，否则继续向外传递，直到传递到Activity的onTouchEvent()。
        Log.e(TAG, "onInterceptTouchEvent: " + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
