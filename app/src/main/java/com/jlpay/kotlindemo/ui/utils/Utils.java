package com.jlpay.kotlindemo.ui.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;

public class Utils {

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
