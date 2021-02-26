package com.jlpay.kotlindemo.ui.base;

import android.os.Environment;

import com.jlpay.kotlindemo.ui.utils.AppUtils;

import java.io.File;

/**
 * 常量类
 */
public class Constants {

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
