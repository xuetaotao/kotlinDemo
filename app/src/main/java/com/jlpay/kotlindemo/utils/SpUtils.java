package com.jlpay.kotlindemo.utils;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;


public class SpUtils {

    //TODO 可以考虑在内存中加一个二级缓存机制，避免频繁从SP文件中读取性能低下
    public static Context context = AppUtils.getContext();


    /**
     * SP加密存储，Key 和 Value 均加密
     * 将所有类型数据都转为 String 后加密
     */
    private static void encryptPut(@NonNull String key, @NonNull Object value) {
        String valueStr = null;

        if (value instanceof String) {
            valueStr = (String) value;
        } else if (value instanceof Boolean) {
            valueStr = Boolean.toString((Boolean) value);
        } else if (value instanceof Integer) {
            valueStr = Integer.toString((Integer) value);
        } else if (value instanceof Float) {
            valueStr = Float.toString((Float) value);
        } else if (value instanceof Long) {
            valueStr = Long.toString((Long) value);
        }

        byte[] encryptKey = AESUtils.encryptByAES(AESUtils.AES_KEY.getBytes(), key.getBytes());
        String base64KeyStr = RSAUtils.byteToBase64Str(encryptKey);
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences settings = context.getSharedPreferences(context.getPackageName() + "_preferences", MODE_PRIVATE);

        if (valueStr != null && !TextUtils.isEmpty(valueStr)) {
            byte[] encryptValue = AESUtils.encryptByAES(AESUtils.AES_KEY.getBytes(), valueStr.getBytes());
            String base64ValueStr = RSAUtils.byteToBase64Str(encryptValue);
            settings.edit().putString(base64KeyStr, base64ValueStr).apply();

        } else {
            settings.edit().putString(base64KeyStr, "").apply();
        }
    }


    /**
     * SP解密
     */
    private static Object decryptGet(@NonNull String key, @NonNull Object defaultvalue) {
        String valueStr = null;
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences settings = context.getSharedPreferences(context.getPackageName() + "_preferences", MODE_PRIVATE);
        byte[] encryptKey = AESUtils.encryptByAES(AESUtils.AES_KEY.getBytes(), key.getBytes());
        String base64KeyStr = RSAUtils.byteToBase64Str(encryptKey);

        if (defaultvalue instanceof String) {
            valueStr = (String) defaultvalue;
            String settingsString = settings.getString(base64KeyStr, valueStr);
            if (settingsString != null && !TextUtils.isEmpty(settingsString) && !settingsString.equals(valueStr)) {
                byte[] bytes = RSAUtils.base64StrToByte(settingsString);
                byte[] decryptByte = AESUtils.decryptByAES(AESUtils.AES_KEY.getBytes(), bytes);
                String decryptStr = new String(decryptByte).trim();
                return decryptStr;
            }
            return valueStr;
        } else if (defaultvalue instanceof Boolean) {
            valueStr = Boolean.toString((Boolean) defaultvalue);
            String settingsString = settings.getString(base64KeyStr, valueStr);
            if (settingsString != null && !TextUtils.isEmpty(settingsString) && !settingsString.equals(valueStr)) {
                byte[] bytes = RSAUtils.base64StrToByte(settingsString);
                byte[] decryptByte = AESUtils.decryptByAES(AESUtils.AES_KEY.getBytes(), bytes);
                String decryptStr = new String(decryptByte).trim();
                return Boolean.valueOf(decryptStr);
            }
            return (boolean) defaultvalue;

        } else if (defaultvalue instanceof Integer) {
            valueStr = Integer.toString((Integer) defaultvalue);
            String settingsString = settings.getString(base64KeyStr, valueStr);
            if (settingsString != null && !TextUtils.isEmpty(settingsString) && !settingsString.equals(valueStr)) {
                byte[] bytes = RSAUtils.base64StrToByte(settingsString);
                byte[] decryptByte = AESUtils.decryptByAES(AESUtils.AES_KEY.getBytes(), bytes);
                String decryptStr = new String(decryptByte).trim();
                return Integer.valueOf(decryptStr);
            }
            return (int) defaultvalue;

        } else if (defaultvalue instanceof Float) {
            valueStr = Float.toString((Float) defaultvalue);
            String settingsString = settings.getString(base64KeyStr, valueStr);
            if (settingsString != null && !TextUtils.isEmpty(settingsString) && !settingsString.equals(valueStr)) {
                byte[] bytes = RSAUtils.base64StrToByte(settingsString);
                byte[] decryptByte = AESUtils.decryptByAES(AESUtils.AES_KEY.getBytes(), bytes);
                String decryptStr = new String(decryptByte).trim();
                return Float.valueOf(decryptStr);
            }
            return (float) defaultvalue;

        } else if (defaultvalue instanceof Long) {
            valueStr = Long.toString((Long) defaultvalue);
            String settingsString = settings.getString(base64KeyStr, valueStr);
            if (settingsString != null && !TextUtils.isEmpty(settingsString) && !settingsString.equals(valueStr)) {
                byte[] bytes = RSAUtils.base64StrToByte(settingsString);
                byte[] decryptByte = AESUtils.decryptByAES(AESUtils.AES_KEY.getBytes(), bytes);
                String decryptStr = new String(decryptByte).trim();
                return Long.valueOf(decryptStr);
            }
            return (long) defaultvalue;
        }
        return null;
    }


    public static String getPrefString(String key, final String defaultValue) {
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        return settings.getString(key, defaultValue);

        return (String) decryptGet(key, defaultValue);
    }

    public static void setPrefString(final String key, final String value) {
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        settings.edit().putString(key, value).apply();

        encryptPut(key, value);
    }

    public static boolean getPrefBoolean(final String key, final boolean defaultValue) {
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        return settings.getBoolean(key, defaultValue);

        return (boolean) decryptGet(key, defaultValue);
    }

    public static boolean hasKey(final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    public static void setPrefBoolean(final String key, final boolean value) {
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        settings.edit().putBoolean(key, value).apply();

        encryptPut(key, value);
    }

    public static void setPrefInt(final String key, final int value) {
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        settings.edit().putInt(key, value).apply();

        encryptPut(key, value);
    }

    public static int getPrefInt(final String key, final int defaultValue) {
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        return settings.getInt(key, defaultValue);

        return (int) decryptGet(key, defaultValue);
    }

    public static void setPrefFloat(final String key, final float value) {
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        settings.edit().putFloat(key, value).apply();

        encryptPut(key, value);
    }

    public static float getPrefFloat(final String key, final float defaultValue) {
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        return settings.getFloat(key, defaultValue);

        return (float) decryptGet(key, defaultValue);
    }

    public static void setSettingLong(final String key, final long value) {
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        settings.edit().putLong(key, value).apply();

        encryptPut(key, value);
    }

    public static void setPrefObj(String key, final Object defaultValue) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = gson.toJson(defaultValue);
        settings.edit().putString(key, json).apply();
    }

    public static <T> T getPrefObj(final String key, final Class<T> clazz) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String json = settings.getString(key, null);
        if (!TextUtils.isEmpty(json)) {
            return new Gson().fromJson(json, clazz);
        }
        return null;
    }


    public static long getPrefLong(final String key, final long defaultValue) {
//        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        return settings.getLong(key, defaultValue);

        return (long) decryptGet(key, defaultValue);
    }

    public static void clearPreference(final SharedPreferences p) {
        final SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.apply();
    }

}
