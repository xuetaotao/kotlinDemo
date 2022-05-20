package com.jlpay.kotlindemo.android_study.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

/**
 * TODO 待完善
 * Android动画分为：
 * 视图动画(View动画)：作用对象是视图View,分为补间动画和逐帧动画
 * 属性动画：作用对象是任意Java对象（不再局限视图View对象），可自定义各种动画效果（不再局限于4种基本变换）
 */
public class AnimatorActivity extends AppCompatActivity {

    private ImageView imageView2;
    private TextView tv_introduce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);

        tv_introduce = findViewById(R.id.tv_introduce);
        imageView2 = findViewById(R.id.imageView2);
        initView();
    }


    //写一些相关知识点方便背记
    public void initView() {
        StringBuilder sb = new StringBuilder();
        sb.append("Android动画分为：\n")
                .append("视图动画(View动画)：作用对象是视图View,分为补间动画和逐帧动画\n")
                .append("属性动画：作用对象是任意Java对象（不再局限视图View对象），可自定义各种动画效果（不再局限于4种基本变换）");
        tv_introduce.setText(sb);
    }

    public void animTest(View view) {
//        Toast.makeText(AnimatorActivity.this, "HHH", Toast.LENGTH_SHORT).show();
        relatedClass();
    }


    public void relatedClass() {
        //加载动画资源
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        imageView2.startAnimation(animation);
    }

    /**
     * 视图动画(View动画)：补间动画
     */
    public void tweenAnim() {

    }

    /**
     * 视图动画(View动画)：逐帧动画
     */
    public void frameAnim() {

    }

    /**
     * 属性动画
     * ObjectAnimator、ValueAnimator
     */
    private float currentScale;

    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
//        invalidate();//自定义View的话，值更改就要刷新UI
    }

    public void propertyAnim() {
        //参数意义：要对其属性进行动画处理的对象，要设置的属性，属性的取值范围，
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,
                "currentScale", 0, 100);
        //启动属性动画
        objectAnimator.start();
//        objectAnimator.reverse();

//        ValueAnimator valueAnimator = new ValueAnimator();
    }


}
