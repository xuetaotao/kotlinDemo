package com.jlpay.kotlindemo.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
     * 设置APP字体大小为标准字体，不随系统字体大小改变
     */
    public static void initFontScale(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1.0f;//0.85:小号  1:标准  1.25:大号  1.4:巨无霸
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        Log.d("DisplayMetrics ", "======" + resources.getDisplayMetrics().toString() + "\t,fontScale:" + configuration.fontScale);
    }

    /**
     * 解析url地址中的某个参数的值
     *
     * @param url 地址
     */
    public static String parseUrlParam(String url, String paramKey) {
        String paramValue = null;
        String urlParams = url.substring(url.indexOf("?") + 1);
        String[] splitParams = urlParams.split("&");
        for (String splitParam : splitParams) {
            String[] splitParamKeyValue = splitParam.split("=");
            if (splitParamKeyValue.length == 2 && TextUtils.equals(splitParamKeyValue[0], paramKey)) {
                paramValue = splitParamKeyValue[1];
                break;
            }
        }
        return paramValue;
    }

    // 两次点击按钮之间的点击间隔不能少于400毫秒
    private static final int MIN_CLICK_DELAY_TIME = 400;
    private static long lastClickTime;

    /**
     * 防止按钮快速重复点击
     *
     * @return true:是快速点击 flase:不是
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * 隐藏软键盘
     *
     * @param activity activity
     * @param view     触发隐藏的view
     */
    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null && manager.isActive()) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
