package com.jlpay.kotlindemo.android_study.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

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
     * 自定义View的步骤：
     * 1.attrs文件中自定义属性，以及xml中使用
     * 2.测量onMeasure---只需要测量自定义View自己（如果继承自类似TextView，测量工作就不需要自己做了）
     * 3.自定义View不需要 onLayout
     * 4.onDraw：绘制自己
     * 5.交互
     */
    static class MyView extends View {

        private GestureDetector gestureDetector;

        public MyView(Context context) {
            this(context, null);
        }

        public MyView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);
            int color = typedArray.getColor(R.styleable.MyView_background_color, Color.WHITE);
            typedArray.recycle();

            gestureDetector = new GestureDetector(context, new MyGesture());
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        /**
         * 在onMeasure调用之后  调用
         * 每次改变尺寸时也会调用
         */
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.save();//保存画布绘制前的样子
            invalidate();//刷新显示
            canvas.restore();//恢复画布初始保存的样子
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //将gestureDetector与onTouchEvent关联起来，进而处理自己实现的相关效果
            return gestureDetector.onTouchEvent(event);
        }
    }

    /**
     * 自定义ViewGroup的步骤：
     * 1.attrs文件中自定义属性，以及xml中使用
     * 2.测量onMeasure---先测量子View，再根据子View尺寸，计算自己的，保存尺寸给后面用
     * 3.布局onLayout---根据自己的规则确定 child 的位置
     * 4.绘制onDraw--正常不会调用，可以通过重写 dispatchDraw 替代（一般不会用）
     * 5.交互
     */
    static class MyViewGroup extends ViewGroup {

        private final String TAG = MyViewGroup.class.getSimpleName();

        public MyViewGroup(Context context) {
            this(context, null);
        }

        public MyViewGroup(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public MyViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        //先测量子View，再根据子View尺寸，计算自己的，保存尺寸给后面用
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            //获取限制的值，父容器对子View限制的大小和模式
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            //保存尺寸给后面用
//            setMeasuredDimension(1,2);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            //放置子View
//            layout();
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

    /**
     * 实现手势操作（如双击-->可以直接用这种方式，也可以通过onTouchEvent来处理）
     * 一般用哪个方法重写哪个就行，不需要全部重写，这里是为了注释
     */
    static class MyGesture extends GestureDetector.SimpleOnGestureListener {

        public MyGesture() {
            super();
        }

        //Up时触发，双击的时候只第二次抬起时触发
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        //用的多一些
        //长按    300ms
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        //用的多一些
        //类似 move事件
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        //用的多一些
        //抛掷，当手指抬起的时候触发
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        //用的多一些
        //按下延时触发  100ms --用来实现点击效果，如水波纹
        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        //用的多一些
        //按下   注意：直接返回true，否则onScroll中拦截不到事件
        @Override
        public boolean onDown(MotionEvent e) {
//            return super.onDown(e);
            return true;
        }

        //用的多一些
        //双击    第二次点击按下的时候触发
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        //双击第二次 down、move、up都会触发
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        //略复杂，单击按下时触发，双击时不触发
        //延时300ms触发TAP事件
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            return super.onContextClick(e);
        }
    }
}