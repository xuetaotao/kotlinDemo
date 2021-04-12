package com.jlpay.kotlindemo.ui.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * 封装一些关于APP安全的方法
 */
public class SafetyUtils {

    public static final String TAG = SafetyUtils.class.getSimpleName();

    /**
     * APP程序签名校验
     *
     * @param context     上下文环境
     * @param validateMd5 用来做校验比对的正确的md5
     * @return true:校验通过
     */
    public static boolean validateApkSign(Context context, @NonNull String validateMd5) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;//得到签名
            Signature signature = signatures[0];
            byte[] byteArray = signature.toByteArray();
            String md5 = RSAUtils.md5(byteArray);
            return validateMd5.equals(md5);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 检查应用是否属于debug模式
     *
     * @param context 上下文环境
     * @return true:是 debug模式
     */
    public static boolean isDebuggable(Context context) {
        return 0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE);
    }


    /**
     * 判断APP是否运行于后台
     *
     * @param context 上下文环境
     * @return true 是，后台运行
     */
    public static boolean isAppRunBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : runningAppProcesses) {
            if (appProcessInfo.processName.equals(context.getPackageName())) {
                if (appProcessInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.d(TAG, context.getPackageName() + "\t后台" + appProcessInfo.processName);
                    return true;
                } else {
                    Log.d(TAG, context.getPackageName() + "\t前台" + appProcessInfo.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 获取栈顶Activity
     */
    public static String getStackTopActivity(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        String packageName = Objects.requireNonNull(activityManager.getRunningTasks(1).get(0).topActivity).getPackageName();//com.jlpay.kotlinDemo
//        String shortClassName = Objects.requireNonNull(activityManager.getRunningTasks(1).get(0).topActivity).getShortClassName();//.ui.start.LoginActivity
        String className = Objects.requireNonNull(activityManager.getRunningTasks(1).get(0).topActivity).getClassName();//com.jlpay.kotlinDemo.ui.start.LoginActivity
//        LogUtils.e("packageName：：" + packageName + "\n" + "shortClassName：：" + shortClassName + "\n" + "className：：" + className);
        return className;
    }


    /**
     * 禁止页面截屏
     * 注：Activity做出禁止行为后，该页面下的Dialog也会禁止截屏
     */
    public static void forbidScreenShots(Activity activity) {
        if (activity == null) {
            return;
        }
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }


    /**
     * 登陆账号脱敏
     *
     * @param account 登陆账号
     * @return 脱敏后的登陆账号
     */
    public static String accountEncrypt(String account) {
        if (TextUtils.isEmpty(account)) {
            return account;
        }
        if (account.length() >= 11) {
            return strEncrpty(account, 3, 4);
        } else {
            return strEncrpty(account);
        }
    }


    /**
     * 手机号脱敏
     *
     * @param mobilePhone 手机号码，如：13566156100
     * @return 空值：返回原字符串;     非手机号：返回原字符串；    手机号：返回脱敏后手机号码;
     */
    public static String mobilePhoneEncrypt(String mobilePhone) {
        if (TextUtils.isEmpty(mobilePhone) || mobilePhone.length() != 11) {
            return mobilePhone;
        }
        return strEncrpty(mobilePhone, 3, 4);
    }


    /**
     * 身份证号码脱敏
     *
     * @param idCardNum 身份证号码，如：
     * @return 返回脱敏后的身份证号码
     */
    public static String idCardNumEncrypt(String idCardNum) {
        if (TextUtils.isEmpty(idCardNum)) {
            return idCardNum;
        }
        return strEncrpty(idCardNum, 6, 4);
    }


    /**
     * 银行卡号脱敏
     *
     * @param bankCardNum 银行卡号
     * @return 返回脱敏后的银行卡号
     */
    public static String bankCardNumEncrypt(String bankCardNum) {
        if (TextUtils.isEmpty(bankCardNum) || bankCardNum.length() <= 4) {
            return bankCardNum;
        }
        return String.format("%s  ****  ****  %s", bankCardNum.substring(0, 4), bankCardNum.substring(bankCardNum.length() - 4));
    }


    /**
     * 银行卡号规范化
     * 去除开头结尾和中间的空格
     *
     * @param bankCardNum 银行卡号
     * @return 除开头结尾和中间的空格后的银行卡号
     */
    public static String bankCardNumFormat(String bankCardNum) {
        return bankCardNum.replace(" ", "");
    }


    /**
     * 普通字符串脱敏，APP专用规则
     *
     * @param str 待脱敏字符串
     * @return 已脱敏字符串
     */
    public static String strEncrpty(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            return strEncrpty(str, 0, 0);
        } else if (str.length() == 2) {
            return strEncrpty(str, 0, 1);
        } else if (str.length() == 3) {
            return strEncrpty(str, 0, 1);
        } else {
            return strEncrpty(str, 1, 1);
        }
    }


    /**
     * 普通字符串脱敏
     *
     * @param str          待脱敏字符串
     * @param startKeepNum 开头保留字符位数
     * @param endKeepNum   结尾保留字符位数
     * @return 已脱敏字符串
     */
    private static String strEncrpty(String str, int startKeepNum, int endKeepNum) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int strLength = str.length();
        if (startKeepNum >= strLength || startKeepNum < 0 || endKeepNum >= strLength || endKeepNum < 0) {
            return str;
        }
        if (startKeepNum + endKeepNum >= strLength) {
            return str;
        }
        char[] chars = str.toCharArray();
        for (int i = startKeepNum; i < strLength - endKeepNum; i++) {
            chars[i] = '*';
        }
        return new String(chars);
    }


    /**
     * 验证密码有效性
     * 密码由8-20位数字和字母组成
     *
     * @param password 待验证密码
     * @return false:有效，符合规则；    true:无效，不符合规则
     */
    public static boolean invalidPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return true;
        }
        //密码长度不够或过长 / 纯数字 / 纯字母 / 包含特殊字符 均不通过
        //^ 匹配一行的开头位置  (?![0-9]+$) 预测该位置后面不全是数字  (?![a-zA-Z]+$) 预测该位置后面不全是字母  [0-9A-Za-z] {8,16} 由8-16位数字或这字母组成   $ 匹配行结尾位置
        return !password.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$");
    }
}
