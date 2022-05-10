package com.jlpay.kotlindemo.android_study.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

/**
 * View的绘制流程
 * <p>
 * 三个关键对象：
 * WindowManagerImpl：确定View属于哪个屏幕，哪个父窗口
 * WindowManagerGlobal：管理整个进程 所有的窗口信息（view,root,WindowManager.LayoutParams）
 * ViewRootImpl：WindowManagerGlobal的实际操作者，操作自己的窗口；
 * <p>
 * ViewRootImpl中的几个关键方法：
 * --> setView()
 * --> performTraversals()：绘制View，里面会执行三大方法
 * <p>
 * ViewRootImpl.performTraversals()方法中重要的几个操作
 * --> windowSizeMayChange |= measureHierarchy();//预测量
 * --> relayoutResult = relayoutWindow(params, viewVisibility, insetsPending);//布局窗口
 * --> performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);//控件树测量
 * --> performLayout(lp, mWidth, mHeight);//布局
 * --> performDraw();//绘制
 */
public class ViewDrawingProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drawing_process);

        WindowManager windowManager = getWindowManager();
    }


    /**
     * 自定义View
     */
    static class MyView extends View {

        public MyView(Context context) {
            super(context);
        }

        public MyView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            invalidate();
        }
    }

    /**
     * 自定义 ViewGroup
     */
    static class MyLinearLayout extends LinearLayout {

        private final String TAG = MyLinearLayout.class.getSimpleName();

        public MyLinearLayout(Context context) {
            super(context);
        }

        public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.e(TAG, "onDraw: ");//ViewGroup这里不会执行
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            Log.e(TAG, "dispatchDraw: ");//会执行
        }
    }
}