package com.jlpay.kotlindemo.android_study.ui;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

/**
 * View.java类中只处理事件：View.dispatchTouchEvent()--->只有事件的处理逻辑
 */
public class ViewEventDispatchView extends androidx.appcompat.widget.AppCompatButton {

    private String TAG = ViewEventDispatchView.class.getSimpleName();

    public ViewEventDispatchView(Context context) {
        this(context, null);
    }

    public ViewEventDispatchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewEventDispatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //View中事件传递流程:
    //第一步 只有事件的处理逻辑
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //true, 当前的dispatchTouchEvent消费，停止传递
        //false, 父View的onTouchEvent消费
        Log.e(TAG, "子View--->dispatchTouchEvent：" + event.getAction());
        //内部拦截法（一定要想办法让子view拿到事件）步骤一
        //如果是ACTION_DOWN事件，请求父容器不要拦截我，但是还需要父容器的onInterceptTouchEvent对该事件做不拦截处理
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        //该方法可以让父容器的onInterceptTouchEvent()方法直接不执行，从而不让父容器拦截
//            getParent().requestDisallowInterceptTouchEvent(true);
//        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            getParent().requestDisallowInterceptTouchEvent(false);
//        }
        return super.dispatchTouchEvent(event);
    }

    //第二步
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果onTouchEvent()方法返回true，则表示当前View会消费该事件，事件传递终止。
        //如果返回false，则会回到当前View的parent View中并调用它的onTouchEvent()，
        // 如果onTouchEvent()返回true，则表示当前ViewGroup消费事件，终止事件传递，否则继续向外传递，直到传递到Activity的onTouchEvent()。
        Log.e(TAG, "子View--->onTouchEvent: " + event.getAction());
        //获取手指按下的位置
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "子View--->onTouchEvent: MotionEvent.ACTION_DOWN=" + MotionEvent.ACTION_DOWN);
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "子View--->onTouchEvent: MotionEvent.ACTION_UP=" + MotionEvent.ACTION_UP);
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "子View--->onTouchEvent: MotionEvent.ACTION_CANCEL=" + MotionEvent.ACTION_CANCEL);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "子View--->onTouchEvent: MotionEvent.ACTION_MOVE=" + MotionEvent.ACTION_MOVE);
                break;
        }
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
