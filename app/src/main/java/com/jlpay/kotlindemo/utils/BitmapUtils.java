package com.jlpay.kotlindemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.base.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.jlpay.kotlindemo.base.Constants.IMAGE_SAVE_DIR;

public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();

    /**
     * 图片压缩监听接口
     */
    public interface ImageCompressListener {
        void onSuccess(File file);

        void onError(String msg);
    }


    /**
     * 图片压缩
     *
     * @param context
     * @param imgPath  图片本地路径
     * @param listener
     */
    public static void imageCompress(Context context, String imgPath, ImageCompressListener listener) {

//        sizeCompress(imgPath, System.currentTimeMillis() + "_sizeCompress", 1500, 1500, listener);//压缩尺寸标准是后台老王给的
//        qualityCompress(imgPath, System.currentTimeMillis() + "_qualityCompress", 300, listener);//质量压缩
//        commonCompress(imgPath, System.currentTimeMillis() + "_commonCompress", 1500, 1500, 300, listener);//通用压缩
        lubanCompress(context, imgPath, listener);
    }


    /**
     * 图片压缩，适配 Android Q
     *
     * @param context
     * @param uri      图片uri
     * @param listener
     */
    public static void imageCompress(Context context, Uri uri, ImageCompressListener listener) {

//        sizeCompress(uri, System.currentTimeMillis() + "_sizeCompress", 1500, 1500, listener);//压缩尺寸标准是后台老王给的
//        qualityCompress(uri, System.currentTimeMillis() + "_qualityCompress", 300, listener);//质量压缩
        commonCompress(uri, System.currentTimeMillis() + "_commonCompress", 1500, 1500, 300, listener);//通用压缩
//        lubanCompress(context, uri, listener);//LuBan.load(Uri uri)方法有问题，只能通过图片文件路径来load，
    }


    /**
     * 图片压缩-Luban压缩
     * 注：LuBan.load(Uri uri)方法有问题，只能通过图片文件路径来load，
     * 因此适配 Android Q时，对于共享目录下的图片文件无法使用Uri进行压缩，暂时只能通过复制到APP私有目录下再进行压缩(APP外部私有目录可以通过路径访问)，然后压缩完后删除原图
     * 自己写的基于Android原生方法压缩的不需要复制这一步
     *
     * @param context
     * @param imgPath  图片本地路径
     * @param listener
     */
    public static void lubanCompress(Context context, @NonNull String imgPath, ImageCompressListener listener) {

        FileUtils.makeDirs(IMAGE_SAVE_DIR);
        File originFile = new File(imgPath);
        if (!originFile.exists()) {
            listener.onError(context.getResources().getString(R.string.file_not_exist));
            return;
        }
        Luban.with(context)
                .load(originFile)
                .ignoreBy(1024)
                .setTargetDir(IMAGE_SAVE_DIR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        //小于1024KB的文件不会压缩，若BasePicturesActivity里考虑使用复制到APP外部私有目录下再压缩的策略，且还没有些页面也调用了图片压缩，可以放开这里
                        //删除的时候再加一层限制：判断是不是APP私有目录下的图片
                        String originFileAbsolutePath = originFile.getAbsolutePath();
                        File originFile = new File(originFileAbsolutePath);
                        String originFileParent = originFile.getParent();
                        if (Constants.IMAGE_SAVE_DIR_DESC.equals(originFileParent) && !file.getAbsolutePath().equals(originFileAbsolutePath)) {
                            boolean delete = originFile.delete();
                            Log.e(TAG, "APP私有目录下相册复制过来的照片原图删除结果：" + delete);
                        }
                        listener.onSuccess(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e.getMessage());
                    }
                })
                .launch();
    }


    /**
     * 图片压缩-Android原生，Android Q 后仅限APP私有目录下文件使用
     * 先进行尺寸压缩，加载到内存中后看照片是否有旋转角度，进行旋转，再进行质量压缩，最后保存本地
     *
     * @param imgPath          图片路径
     * @param compressFileName 压缩后的文件名，不用带目录和后缀(如.png或者.jpg)
     * @param reqWidth         要求压缩后的图片宽度
     * @param reqHeight        要求压缩后的图片高度
     * @param targetSize       要求压缩后的图片大小，单位：KB
     * @param listener         压缩结果监听
     */
    public static void commonCompress(@NonNull String imgPath, @NonNull String compressFileName, @NonNull int reqWidth, @NonNull int reqHeight,
                                      @NonNull int targetSize, @NonNull ImageCompressListener listener) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        options.inJustDecodeBounds = false;

        int sampleSize = 1;
        while ((width / sampleSize > reqWidth) || (height / sampleSize > reqHeight)) {
            sampleSize *= 2;
        }
        options.inSampleSize = sampleSize;
        try {
            bitmap = BitmapFactory.decodeFile(imgPath, options);
        } catch (OutOfMemoryError e) {
            listener.onError("The bitmap has out of memory：" + e.getMessage());
            return;
        }
        //角度旋转
        bitmap = rotatePicByDegree(bitmap, getPictureDegree(imgPath));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        int quality = 90;
        while (byteArrayOutputStream.toByteArray().length / 1024 > targetSize && quality >= 0) {
            byteArrayOutputStream.reset();//重置
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            quality -= 10;
        }

        String child = compressFileName + ".jpg";
        File file = new File(IMAGE_SAVE_DIR, child);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byteArrayOutputStream.writeTo(outputStream);
//            outputStream.write(byteArrayOutputStream.toByteArray());//或者这样
            outputStream.flush();
            outputStream.close();
            byteArrayOutputStream.close();
            Log.e("Bitmap.Size.Compressed：", file.length() / 1024 + "KB");
            listener.onSuccess(file);

        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
        bitmapRecycle(bitmap);
    }


    /**
     * 图片压缩-Android原生，适配 Android Q，且不需要复制到APP私有目录这一步
     * 先进行尺寸压缩，加载到内存中后看照片是否有旋转角度，进行旋转，再进行质量压缩，最后保存本地
     *
     * @param uri              图片uri
     * @param compressFileName 压缩后的文件名，不用带目录和后缀(如.png或者.jpg)
     * @param reqWidth         要求压缩后的图片宽度
     * @param reqHeight        要求压缩后的图片高度
     * @param targetSize       要求压缩后的图片大小，单位：KB
     * @param listener         压缩结果监听
     */
    public static void commonCompress(@NonNull Uri uri, @NonNull String compressFileName, @NonNull int reqWidth, @NonNull int reqHeight,
                                      @NonNull int targetSize, @NonNull ImageCompressListener listener) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imgStream = MediaStoreUtils.getImgStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(imgStream);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        options.inJustDecodeBounds = false;

        int sampleSize = 1;
        while ((width / sampleSize > reqWidth) || (height / sampleSize > reqHeight)) {
            sampleSize *= 2;
        }
        options.inSampleSize = sampleSize;
        try {
            bitmap = BitmapFactory.decodeStream(imgStream, null, options);
        } catch (OutOfMemoryError e) {
            listener.onError("The bitmap has out of memory：" + e.getMessage());
            return;
        }
        //角度旋转
        bitmap = rotatePicByDegree(bitmap, getPictureDegree(imgStream));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        int quality = 90;
        while (byteArrayOutputStream.toByteArray().length / 1024 > targetSize && quality >= 0) {
            byteArrayOutputStream.reset();//重置
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            quality -= 10;
        }

        String child = compressFileName + ".jpg";
        File file = new File(IMAGE_SAVE_DIR, child);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byteArrayOutputStream.writeTo(outputStream);
//            outputStream.write(byteArrayOutputStream.toByteArray());//或者这样
            outputStream.flush();
            outputStream.close();
            byteArrayOutputStream.close();
            Log.e("Bitmap.Size.Compressed：", file.length() / 1024 + "KB");
            listener.onSuccess(file);

        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
        bitmapRecycle(bitmap);
    }


    /**
     * 图片压缩-Android原生只尺寸压缩
     * 但是实际上{@link #bitmapToFile 方法中还是有调用compress的质量压缩，只是取了最高quality:100}
     * 一般通用完整流程：先进行尺寸压缩，然后在进行质量压缩，然后看照片是否有旋转角度，如果有，rotate一下，最后返回处理后的照片路径
     *
     * @param imgPath          图片路径
     * @param compressFileName 压缩后的文件名，不用带目录和后缀(如.png或者.jpg)
     * @param reqWidth         要求压缩后的图片宽度
     * @param reqHeight        要求压缩后的图片高度
     * @param listener         压缩结果监听
     */
    public static void sizeCompress(String imgPath, String compressFileName, int reqWidth, int reqHeight, ImageCompressListener listener) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//开始读入图片
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);//此时返回bitmap为空
        int width = options.outWidth;
        int height = options.outHeight;
        options.inJustDecodeBounds = false;

        //计算采样率
        int sampleSize = 1;//压缩比例
        while ((width / sampleSize > reqWidth) || (height / sampleSize > reqHeight)) {
            sampleSize *= 2;
        }
        options.inSampleSize = sampleSize;

        try {
            bitmap = BitmapFactory.decodeFile(imgPath, options);//加载采样率压缩后的图片到内存
        } catch (OutOfMemoryError e) {
            listener.onError("The bitmap has out of memory：" + e.getMessage());
            return;
        }
        bitmap = rotatePicByDegree(bitmap, getPictureDegree(imgPath));//将图片旋转为正常角度

        //保存压缩后的图片到本地
        FileUtils.makeDirs(IMAGE_SAVE_DIR);
        String child = compressFileName + ".jpg";
        if (bitmapToFile(bitmap, child)) {
            listener.onSuccess(new File(IMAGE_SAVE_DIR, child));
        } else {
            listener.onError("image_compress_failed");
        }
        bitmapRecycle(bitmap);
    }


    /**
     * 图片压缩-Android原生只尺寸压缩，适配 Android Q
     * 但是实际上{@link #bitmapToFile 方法中还是有调用compress的质量压缩，只是取了最高quality:100}
     * 一般通用完整流程：先进行尺寸压缩，然后在进行质量压缩，然后看照片是否有旋转角度，如果有，rotate一下，最后返回处理后的照片路径
     *
     * @param uri              图片uri
     * @param compressFileName 压缩后的文件名，不用带目录和后缀(如.png或者.jpg)
     * @param reqWidth         要求压缩后的图片宽度
     * @param reqHeight        要求压缩后的图片高度
     * @param listener         压缩结果监听
     */
    public static void sizeCompress(@NonNull Uri uri, String compressFileName, int reqWidth, int reqHeight, ImageCompressListener listener) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//开始读入图片
        InputStream imgStream = MediaStoreUtils.getImgStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(imgStream, null, options);//此时返回bitmap为空
        int width = options.outWidth;
        int height = options.outHeight;
        options.inJustDecodeBounds = false;

        //计算采样率
        int sampleSize = 1;//压缩比例
        while ((width / sampleSize > reqWidth) || (height / sampleSize > reqHeight)) {
            sampleSize *= 2;
        }
        options.inSampleSize = sampleSize;

        try {
            bitmap = BitmapFactory.decodeStream(imgStream, null, options);//加载采样率压缩后的图片到内存
        } catch (OutOfMemoryError e) {
            listener.onError("The bitmap has out of memory：" + e.getMessage());
            return;
        }
        bitmap = rotatePicByDegree(bitmap, getPictureDegree(imgStream));//将图片旋转为正常角度

        //保存压缩后的图片到本地
        FileUtils.makeDirs(IMAGE_SAVE_DIR);
        String child = compressFileName + ".jpg";
        if (bitmapToFile(bitmap, child)) {
            listener.onSuccess(new File(IMAGE_SAVE_DIR, child));
        } else {
            listener.onError("image_compress_failed");
        }
        bitmapRecycle(bitmap);
    }


    /**
     * 图片压缩-Android原生只质量压缩
     *
     * @param imgPath          图片路径
     * @param compressFileName 压缩后的文件名，不用带目录和后缀(如.png或者.jpg)
     * @param targetSize       要求压缩后的图片大小，单位：KB
     * @param listener         压缩结果监听
     */
    public static void qualityCompress(String imgPath, String compressFileName, int targetSize, ImageCompressListener listener) {
        Bitmap bitmap = null;
        try {
            //避免图片加载占用过大内存导致崩溃
            bitmap = BitmapFactory.decodeFile(imgPath);
            bitmap = rotatePicByDegree(bitmap, getPictureDegree(imgPath));
        } catch (OutOfMemoryError e) {
            listener.onError("The bitmap has out of memory：" + e.getMessage());
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        int quality = 90;
        //这里也可以设置为quality >= 0，这样可以压到最小，但是也不一定就能满足targetSize
        while (byteArrayOutputStream.toByteArray().length / 1024 > targetSize && quality >= 0) {
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            Log.d("Bitmap.Size.Comp.Str：", byteArrayOutputStream.toByteArray().length / 1024 + "KB" + "\t" + "quality = " + quality);
            quality -= 10;
        }

        FileUtils.makeDirs(IMAGE_SAVE_DIR);
        String child = compressFileName + ".jpg";
        File file = new File(IMAGE_SAVE_DIR, child);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byteArrayOutputStream.writeTo(fileOutputStream);
//            fileOutputStream.write(byteArrayOutputStream.toByteArray());//或者這樣
            fileOutputStream.flush();
            fileOutputStream.close();
            byteArrayOutputStream.close();
            Log.e("Bitmap.Size.Compressed：", file.length() / 1024 + "KB");
            listener.onSuccess(file);

        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
        bitmapRecycle(bitmap);
    }


    /**
     * 图片压缩-Android原生只质量压缩， 适配 Android Q
     *
     * @param uri              图片uri
     * @param compressFileName 压缩后的文件名，不用带目录和后缀(如.png或者.jpg)
     * @param targetSize       要求压缩后的图片大小，单位：KB
     * @param listener         压缩结果监听
     */
    public static void qualityCompress(@NonNull Uri uri, String compressFileName, int targetSize, ImageCompressListener listener) {
        Bitmap bitmap = null;
        InputStream imgStream = MediaStoreUtils.getImgStream(uri);
        try {
            //避免图片加载占用过大内存导致崩溃
            bitmap = BitmapFactory.decodeStream(imgStream);
            bitmap = rotatePicByDegree(bitmap, getPictureDegree(imgStream));
        } catch (OutOfMemoryError e) {
            listener.onError("The bitmap has out of memory：" + e.getMessage());
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        int quality = 90;
        //这里也可以设置为quality >= 0，这样可以压到最小，但是也不一定就能满足targetSize
        while (byteArrayOutputStream.toByteArray().length / 1024 > targetSize && quality >= 0) {
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            Log.d("Bitmap.Size.Comp.Str：", byteArrayOutputStream.toByteArray().length / 1024 + "KB" + "\t" + "quality = " + quality);
            quality -= 10;
        }

        FileUtils.makeDirs(IMAGE_SAVE_DIR);
        String child = compressFileName + ".jpg";
        File file = new File(IMAGE_SAVE_DIR, child);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byteArrayOutputStream.writeTo(fileOutputStream);
//            fileOutputStream.write(byteArrayOutputStream.toByteArray());//或者這樣
            fileOutputStream.flush();
            fileOutputStream.close();
            byteArrayOutputStream.close();
            Log.e("Bitmap.Size.Compressed：", file.length() / 1024 + "KB");
            listener.onSuccess(file);

        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
        bitmapRecycle(bitmap);
    }


    public static byte[] qualityCompress(Bitmap bitmap, int targetSize) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] result;

        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        while (byteArrayOutputStream.toByteArray().length / 1024 > targetSize && quality >= 0) {
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            Log.d("Bitmap.Size.Comp.Str：", byteArrayOutputStream.toByteArray().length / 1024 + "KB" + "\t" + "quality = " + quality);
            quality -= 10;
        }
        result = byteArrayOutputStream.toByteArray();
        return result;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bitmap 待旋转图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotatePicByDegree(Bitmap bitmap, int degree) {
        Bitmap rotateBitmap = null;
        //根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        if (rotateBitmap == null) {
            rotateBitmap = bitmap;
        }
        if (bitmap != rotateBitmap) {
            bitmap.recycle();
            bitmap = null;
        }
        return rotateBitmap;
    }


    /**
     * 获取图片的旋转角度
     * 手机拍照的图片，本地查看正常的照片，传到服务器发现照片旋转了90°或者270°，这是因为有些手机摄像头的参数原因，拍出来的照片是自带旋转角度的
     *
     * @param imgPath 本地图片路径
     * @return 图片旋转角度
     */
    public static int getPictureDegree(String imgPath) {
        int degree = 0;
        try {
            //获取图片的exif信息，exif是照片的一些头部信息，如拍照时间，相机品牌，型号，色彩编码，旋转角度等
            ExifInterface exifInterface = new ExifInterface(imgPath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }


    /**
     * 获取图片的旋转角度，适配 Android Q
     * 手机拍照的图片，本地查看正常的照片，传到服务器发现照片旋转了90°或者270°，这是因为有些手机摄像头的参数原因，拍出来的照片是自带旋转角度的
     *
     * @param inputStream 输入流
     * @return 图片旋转角度
     */
    public static int getPictureDegree(@NonNull InputStream inputStream) {
        int degree = 0;
        try {
            //获取图片的exif信息，exif是照片的一些头部信息，如拍照时间，相机品牌，型号，色彩编码，旋转角度等
            ExifInterface exifInterface = new ExifInterface(inputStream);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }

    /**
     * bitmap格式转文件
     * 存在质量压缩
     *
     * @param bitmap   待转换bitmap
     * @param fileName 本地文件名，默认不带后缀，需要调用者自己加
     * @return true 转换成功
     */
    public static boolean bitmapToFile(Bitmap bitmap, String fileName) {
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        boolean convertResult = false;
        FileUtils.makeDirs(IMAGE_SAVE_DIR);
        File file = new File(IMAGE_SAVE_DIR, fileName);
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            convertResult = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Log.e("Bitmap.Size.Compressed：", file.length() / 1024 + "KB");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return convertResult;
    }

    /**
     * 文件转Bitmap
     *
     * @param filePath 文件路径
     * @return Bitmap
     */
    public static Bitmap fileToBitmap(@NonNull String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(filePath);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * Bitmap内存回收
     *
     * @param bitmaps 准备回收内存的Bitmaps
     */
    public static void bitmapRecycle(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bitmap : bitmaps) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }


    /**
     * 根据图片资源的Uri获取输入流
     *
     * @param context
     * @param uri     Android图片资源的Uri
     * @return 图片的输入流
     */
//    private InputStream getImageStream(Context context, Uri uri) {
//        InputStream inputStream = null;
//        try {
//            inputStream = context.getApplicationContext().getContentResolver().openInputStream(uri);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return inputStream;
//    }
}
