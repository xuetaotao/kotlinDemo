package com.jlpay.kotlindemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.regex.Pattern;

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
     * 沉浸式状态栏
     * （Android关于沉浸式状态栏总结：https://juejin.cn/post/6844903490402123789#heading-0）
     * 下面代码摘自合伙人app，注意要放到setContentView()方法之前，不过好像放在后面也没事
     */
    public static void setStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            // Translucent status bar
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (null != activity.getActionBar()) {
            activity.getActionBar().hide();
        }
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

    /**
     * 判断字符串是否为数字（包括小数和正整数）
     *
     * @param str 要判断的字符串
     * @return true 是
     */
    private boolean isNumerEx(String str) {
//        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+"); //正负小数类型
        Pattern pattern = Pattern.compile("[0-9]+.?[0-9]+");//正小数
        Pattern pattern1 = Pattern.compile("[0-9]*");//正整数
        if (pattern.matcher(str).matches() || pattern1.matcher(str).matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置EditText是否可编辑
     *
     * @param edtInvoiceCount
     * @param editable
     */
    private void setEdieTextEditable(EditText edtInvoiceCount, boolean editable) {
        edtInvoiceCount.setFocusable(editable);
        edtInvoiceCount.setFocusableInTouchMode(editable);
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
