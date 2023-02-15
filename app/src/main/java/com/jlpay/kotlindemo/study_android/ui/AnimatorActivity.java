package com.jlpay.kotlindemo.study_android.ui;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
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

    //动画，测试代码
    //属性动画的原理
    public void animationTest() {
        final float[] newTranslation = {0};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                newTranslation[0] += 5;
                //设置X轴偏移
                imageView2.setTranslationX(newTranslation[0]);
            }
        };
        for (int i = 1; i < 100; i++) {
            imageView2.postDelayed(runnable, i * 10);
        }
    }

    public void animTest(View view) {
//        Toast.makeText(AnimatorActivity.this, "HHH", Toast.LENGTH_SHORT).show();
        relatedClass();
    }


    public void relatedClass() {
        //加载动画资源
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        imageView2.startAnimation(animation);
        ViewPropertyAnimator animator = imageView2.animate();
        animator.translationX(500);
        //内插器/插值器
        //开始阶段加速，结束阶段减速（默认的速度模型）
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        //线性的速度模型
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        imageView2.animate().translationX(500)
                //设置时长，毫秒
                .setDuration(1000)
                //设置速度模型
                .setInterpolator(linearInterpolator)
                //监听器
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                    }
                })
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {

                    }
                })
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,
                "currentScale", 0, 100);
        objectAnimator.setEvaluator(new ArgbEvaluator());//颜色属性计算的Evaluator
        objectAnimator.start();
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
     * https://www.bilibili.com/video/BV1rx411x7oM/?spm_id_from=333.880.my_history.page.click&vd_source=8e9997b417b4b997fd66ceb00b62d13b
     */
    private float currentScale;

    //第三步：
    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
//        invalidate();//自定义View的话，值更改就要刷新UI
    }

    public void propertyAnim() {
        //第一步：
        //参数意义：要对其属性进行动画处理的对象，要设置的属性，属性的取值范围(填一个是目标值，填2个是起始值和目标值，更多的话中间就是转接点)，
        //如果只有一个目标值的话，currentScale属性还需要有个getCurrentScale方法来获取起始值
        //ofFloat或者ofInt指的是属性的类型，即currentScale的类型
        //这里就是要把currentScale的值从0变到100，具体修改的过程是调用setCurrentScale方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,
                "currentScale", 0, 100);
        //第二步：启动属性动画
        objectAnimator.start();
//        objectAnimator.reverse();

//        ValueAnimator valueAnimator = new ValueAnimator();
    }


}
