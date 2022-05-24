package com.jlpay.kotlindemo.utils;

import android.text.TextUtils;

/**
 * 处理各种数据格式转换，金额格式化等数据
 */
public class DataUtils {


    /****************************基本数据类型格式互转**********************************************************/

    /**
     * String 转 double
     *
     * @param stringNum 要转换的数字字符串
     * @return 若传入的是非数字字符串，则默认返回初始值 0.0
     */
    public static double stringToDouble(String stringNum) {
        double parseDouble = 0.0;
        if (TextUtils.isEmpty(stringNum)) {
            return parseDouble;
        }
        try {
            parseDouble = Double.parseDouble(stringNum);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return parseDouble;
    }


    /**
     * String 转 int
     *
     * @param stringNum 要转换的数字字符串
     * @return 若传入的是非数字字符串，则默认返回初始值 0
     */
    public static int stringToInt(String stringNum) {
        int parseInt = 0;
        if (TextUtils.isEmpty(stringNum)) {
            return parseInt;
        }
        try {
            parseInt = Integer.parseInt(stringNum);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return parseInt;
    }
}
