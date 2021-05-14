package com.jlpay.kotlindemo.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;

/**
 * 存放一些日常使用小方法的工具类
 */
public class Utils {

    /**
     * 启动Activity
     *
     * @param packageContext 上下文
     * @param clss           要启动的
     */
    public static void launchActivity(Context packageContext, Class<?> clss) {
        Intent intent = new Intent(packageContext, clss);
        packageContext.startActivity(intent);
    }


    /**
     * 自定义View绘制的时候参数都是px
     * dp转换为px
     */
    public static float dp2px(float value, View view) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, view.getResources().getDisplayMetrics());
    }

    public static float dp2px(float value, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static float dp2px(float value) {
        //Resources.getSystem()获取到的是系统的DPI的配置，但是获取不到软件自己的配置。这个可能会影响到软件自己的字体大小设置等(没测过)
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }

    public void test() {
        float[] aa = {2f, 3f, 4f};
        float[] bb = new float[]{2f, 3f, 4f};
        float[] cc = new float[5];
        cc[0] = 2f;
    }
}
