package com.jlpay.kotlindemo.ui.utils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.bean.PermissionBean;
import com.jlpay.kotlindemo.ui.base.BaseActivity;
import com.jlpay.kotlindemo.ui.widget.CustomDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PermissionUtils {

    /**
     * 当前APP需要动态获取的运行时权限
     */
    private static String[] partnerAppPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static Map<String, String> getPermissionNameMap() {
        Map<String, String> permissionNameMap = new HashMap<>();
        permissionNameMap.put(Manifest.permission.CAMERA, "相机");
        permissionNameMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储");
//        permissionNameMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, "存储");
        permissionNameMap.put(Manifest.permission.ACCESS_FINE_LOCATION, "定位");
//        permissionNameMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, "定位");
        return permissionNameMap;
    }

    private static Map<String, String> getPermissionDesMap() {
        Map<String, String> getPermissionDesMap = new HashMap<>();
        getPermissionDesMap.put(Manifest.permission.CAMERA, "照相设备将被用于扫码、拍照、身份证OCR识别等需要使用相机的功能");
        getPermissionDesMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "手机存储权限将被用于读写文件系统业务，如图片缓存、主要功能的访问与使用");
//        getPermissionDesMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, "手机存储权限将被用于读写文件系统业务，如图片缓存、主要功能的访问与使用");
        getPermissionDesMap.put(Manifest.permission.ACCESS_FINE_LOCATION, "APP需要获取您的位置信息，用于工单处理等需要位置信息的功能");
//        getPermissionDesMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, "APP需要获取您的位置信息，用于工单处理等需要位置信息的功能");
        return getPermissionDesMap;
    }


    /**
     * 获取运行时权限列表描述及相关授权情况
     */
    public static List<PermissionBean> getPermissionBeanList(BaseActivity baseActivity) {
        return getPermissionBeanList(baseActivity, partnerAppPermissions);
    }

    private static List<PermissionBean> getPermissionBeanList(BaseActivity baseActivity, String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            return null;
        }
        List<PermissionBean> permissionBeanList = new ArrayList<>();
        PermissionBean permissionBean = null;
        Map<String, String> permissionNameMap = getPermissionNameMap();
        Map<String, String> getPermissionDesMap = getPermissionDesMap();
        for (String permission : permissions) {
            permissionBean = new PermissionBean();
            permissionBean.setPermission(permission);
            permissionBean.setPermissionName(permissionNameMap.get(permission));
            permissionBean.setPermissionDes(getPermissionDesMap.get(permission));
            permissionBean.setGranted(checkPermissionBefore(baseActivity, permission));
            permissionBeanList.add(permissionBean);
            permissionBean = null;
        }
        return permissionBeanList;
    }


    /**
     * 获取相机权限
     * 请勿放在Activity的onResume()中，避免反复弹窗
     *
     * @param baseActivity 上下文环境
     * @param listener     权限获取结果监听
     */
    public static void getCameraPermission(BaseActivity baseActivity, PermissionListener listener) {
        int[] applyMsg = new int[]{R.string.camera_permission, R.string.camera_permission_message, R.string.camera_permission_apply};
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        getNeedPermission(baseActivity, listener, applyMsg, permissions);
    }


    /**
     * 获取外部存储权限
     * 请勿放在Activity的onResume()中，避免反复弹窗
     *
     * @param baseActivity 上下文环境
     * @param listener     权限获取结果监听
     */
    public static void getStoragePermission(BaseActivity baseActivity, PermissionListener listener) {
        int[] applyMsg = new int[]{R.string.external_storage_permission, R.string.external_storage_permission_message, R.string.external_storage_permission_apply};
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        getNeedPermission(baseActivity, listener, applyMsg, permissions);
    }


    /**
     * 获取定位权限
     * 请勿放在Activity的onResume()中，避免反复弹窗
     *
     * @param baseActivity 上下文环境
     * @param listener     权限获取结果监听
     */
    public static void getLocationPermission(BaseActivity baseActivity, PermissionListener listener) {
        int[] applyMsg = new int[]{R.string.location_permission, R.string.location_permission_message, R.string.location_permission_apply};
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        getNeedPermission(baseActivity, listener, applyMsg, permissions);
    }


    /**
     * 需要动态获取的权限申请
     * 请勿放在Activity的onResume()中，避免反复弹窗
     *
     * @param baseActivity 上下文环境
     * @param listener     权限获取结果监听
     * @param applyMsg     依次为：权限，申请原因说明，申请描述
     * @param permissions  危险权限名称
     */
    private static void getNeedPermission(BaseActivity baseActivity, PermissionListener listener, int[] applyMsg, String... permissions) {
        if (applyMsg == null || applyMsg.length != 3) {
            return;
        }
        if (!checkPermissionBefore(baseActivity, permissions)) {
            requestPermissionDialog(baseActivity, applyMsg[0], applyMsg[1], applyMsg[2], listener, permissions);
        } else {
            listener.allow();
        }
    }


    /**
     * 权限检查
     */
    private static boolean checkPermissionBefore(BaseActivity baseActivity, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(baseActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 请求权限前的弹窗
     *
     * @param baseActivity    上下文环境
     * @param title           "APP"想访问您的*****
     * @param message         *****权限权限将被用于………的访问与使用
     * @param unauthorizedMsg 权限获取失败的弹窗提示语
     */
    private static void requestPermissionDialog(BaseActivity baseActivity, @StringRes int title, @StringRes int message, @StringRes int unauthorizedMsg,
                                                PermissionListener listener, String... permissions) {
        CustomDialog.Builder builder = new CustomDialog.Builder(baseActivity);
        builder.setTitle(title)
                .setTitleVisible(true)
                .setContent(message)
                .setPositiveButton("允许", (dialog, which) -> {
                    dialog.dismiss();
                    //请求权限
                    requestPermission(baseActivity, unauthorizedMsg, listener, permissions);
                })
                .setNegativeButton("不允许", (dialog, which) -> dialog.dismiss())
                .setCancelable(false).create().show();
    }


    /**
     * 权限获取结果监听
     */
    public interface PermissionListener {
        void allow();
    }


    /**
     * 权限获取
     */
    private static void requestPermission(final BaseActivity baseActivity, @StringRes final int unauthorizedMsg, final PermissionListener listener, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(baseActivity);
        Disposable disposable = rxPermissions.request(permissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //权限获取成功
                            listener.allow();
                        } else {
                            //权限获取失败，弹出授权提示框
                            unauthorizedPoint(baseActivity, unauthorizedMsg);
                        }
                    }
                });
    }


    /**
     * 未授权时，再次点击弹出的授权提示框
     *
     * @param baseActivity 上下文环境
     * @param message      因您未授予APP****权限，该功能无法使用，可在设置中修改
     */
    private static void unauthorizedPoint(BaseActivity baseActivity, @StringRes int message) {
        CustomDialog.Builder builder = new CustomDialog.Builder(baseActivity);
        builder.setTitle(R.string.permission_apply)
                .setTitleVisible(true)
                .setContent(message)
                .setPositiveButton("去设置", (dialog, which) -> {
                    dialog.dismiss();
                    //跳转权限设置界面
                    gotoSettings(baseActivity);
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setCancelable(false).create().show();
    }


    /**
     * 跳转权限设置界面
     *
     * @param baseActivity 上下文环境
     */
    public static void gotoSettings(BaseActivity baseActivity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + baseActivity.getPackageName()));
        baseActivity.startActivity(intent);
    }
}
