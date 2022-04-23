package com.jlpay.kotlindemo.android_study;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

/**
 * View的事件传递机制
 * 责任链模式和Android事件分发: https://blog.csdn.net/ccj659/article/details/53940394
 * <p>
 * 与事件分发相关联的三个方法分别为dispatchTouchEvent，onInterceptTouchEvent， onTouchEvent
 */
public class ViewEventDispatchActivity extends AppCompatActivity {

    private Button button8;
    private String TAG = ViewEventDispatchActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_dispatch);

        initView();
    }

    /**
     * OnTouchListener-->onTouchEvent-->onClickListener
     * OnTouchListener和onTouchEvent执行了两次，是因为在DOWN和UP时两个方法都被调用，onClickListener则只在UP的时候调用
     */
    //Custom view `Button` has setOnTouchListener called on it but does not override performClick
    //onTouch should call View#performClick when a click is detected
    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        button8 = findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "initView: OnClickListener");
                Toast.makeText(ViewEventDispatchActivity.this, "setOnClickListener", Toast.LENGTH_SHORT).show();
            }
        });

        button8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //手指初次接触到屏幕时触发
                    Log.e(TAG, "onTouch: OnTouchListener:\tDOWN");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //手指离开屏幕时触发
                    Log.e(TAG, "onTouch: OnTouchListener:\tUP");
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //手指在屏幕上滑动时触发，会多次触发
                    Log.e(TAG, "onTouch: OnTouchListener:\tMOVE");
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    //事件被上层拦截时触发
                    Log.e(TAG, "onTouch: OnTouchListener:\tCANCEL");
                }
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: " + event.getAction());
        return super.onTouchEvent(event);
    }
}