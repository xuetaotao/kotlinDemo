package com.jlpay.kotlindemo.ui.base;

import android.os.Environment;

import com.jlpay.kotlindemo.ui.utils.AppUtils;

import java.io.File;

/**
 * 常量类
 */
public class Constants {

    public static final String AMAP_API_KEY = "f77e3c2ac742f9b003ef1ac533748e65";//高德地图API key
    public static final String BAIDU_MAP_API_KEY = "Ca3ngPa1TMn5S7ElnTcfN81rS0sISo4q";//百度地图API key
    public static final String TENCENT_MAP_API_KEY = "UFYBZ-ZUX6J-TWSFH-KMAIY-R7VBE-ANF5F";//腾讯地图API key

    public static final String BUGLY_APP_ID = "445be59d72";//腾讯Bugly APPID
    public static final String BUGLY_APP_KEY = "69458a34-fd2c-47d7-8286-69eef612476f";//腾讯Bugly APPKEY
    public static final int BUGLY_RELEASE_TAG = 194584;//BUGLY生产标签
    public static final int BUGLY_DEBUG_TAG = 194586;//BUGLY测试标签

    public static final String APP_RELEASE_SHA1 = "F7:1C:71:88:47:4C:70:5B:CE:5A:85:61:CD:28:D8:A3:83:36:2A:4D";//发布版安全码SHA1，命令行：keytool -v -list -keystore C:\AndroidProgram\kotlinDemo\kotlinDemoKey.jks，密钥库口令：fit1
//    public static final String APP_DEBUG_SHA1 = "12:52:5C:FA:1B:55:32:6C:29:6A:B1:AB:AF:A3:05:0E:A2:12:9D:D4";//发布版安全码SHA1，命令行：keytool -v -list -keystore C:\Users\xuetaotao\.android\debug.keystore，密钥库口令：android

    public static final String TAG = "KotlinDemo";

    //外部存储根目录
    @Deprecated
    public static final String external_storage_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    //Android/data/com.jlpay.kotlindemo/files目录
    public static final String external_files_dir = AppUtils.getContext().getExternalFilesDir(null) + File.separator;

    //Android/data/com.jlpay.kotlindemo/cache目录
    public static final String external_cache_dir = AppUtils.getContext().getExternalCacheDir() + File.separator;


    /*******APP外部私有目录下一些常用目录整理，并统一于{@link com.jlpay.kotlindemo.ui.utils.AppUtils#initFileDir()}初始化*****************/
    public static final String FILE_SAVE_DIR = Constants.external_files_dir + File.separator + "file" + File.separator;//文件保存路径
    public static final String IMAGE_SAVE_DIR = Constants.external_files_dir + File.separator + "image" + File.separator;//图片保存路径
    public static final String AUDIO_SAVE_DIR = Constants.external_files_dir + File.separator + "audio" + File.separator;//音频保存路径
    public static final String VIDEO_SAVE_DIR = Constants.external_files_dir + File.separator + "video" + File.separator;//视频保存路径

    public static final String IMAGE_SAVE_DIR_DESC = "/storage/emulated/0/Android/data/com.jlpay.kotlindemo/files/image";
}
