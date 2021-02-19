package com.jlpay.kotlindemo.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.jlpay.kotlindemo.R;

public class TestCustomerUi extends View {

    public TestCustomerUi(Context context) {
        super(context);
    }

    public TestCustomerUi(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestCustomerUi(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TestCustomerUi);
        int color = typedArray.getColor(R.styleable.TestCustomerUi_background_color, Color.WHITE);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
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
