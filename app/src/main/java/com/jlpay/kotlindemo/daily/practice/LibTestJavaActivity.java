package com.jlpay.kotlindemo.daily.practice;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.jlpay.imagepick.ImageCompress;
import com.jlpay.imagepick.ImagePicker;
import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.base.BaseActivity;
import com.jlpay.kotlindemo.daily.MainActivity;
import com.jlpay.kotlindemo.utils.PermissionUtils;
import com.jlpay.opensdk.location.LocationManager;
import com.jlpay.opensdk.location.bean.LocationData;
import com.jlpay.opensdk.location.impl.ILocation;
import com.jlpay.opensdk.location.listener.LocationListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import com.jlpay.features.imageload.ImagePicker;

public class LibTestJavaActivity extends BaseActivity {

    private TextView tv_result;
    private ImageView imageView;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, LibTestJavaActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void initView() {
        tv_result = findViewById(R.id.tv_result);
        imageView = findViewById(R.id.imageView);

        testClassLoader();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void testClassLoader() {
        ClassLoader classLoader2 = Application.class.getClassLoader();//BootClassLoader
        ClassLoader classLoader1 = AppCompatActivity.class.getClassLoader();//PathClassLoader
        ClassLoader classLoader = Activity.class.getClassLoader();//BootClassLoader
        ClassLoader classLoader3 = MainActivity.class.getClassLoader();//PathClassLoader
        ClassLoader classLoader4 = getClassLoader();//PathClassLoader
        Log.e("LibTestJavaActivity", classLoader2.toString() + "\n\n" + classLoader1.toString() + "\n\n" +
                classLoader.toString() + "\n\n" + classLoader3.toString() + "\n\n" + classLoader4.toString());
    }

    public void imagePicker(View view) {
        ImagePicker.with(LibTestJavaActivity.this)
                .imagePickerListener(new ImagePicker.ImagePickerListener() {
                    @Override
                    public void onSuccess(@NotNull String imagePath) {
                        Log.e("TAG", "复制到APP外部私有目录地址Lib:" + imagePath);
                        Glide.with(LibTestJavaActivity.this).load(imagePath).into(imageView);
                    }

                    @Override
                    public void onFailed(@NotNull String msg, @NotNull String code) {
                        Log.e("TAG", msg);
                        showToast(msg);
                    }
                })
                .crop(true)
                .compress(true)
                .compressType(ImageCompress.ImageCompressType.OriginCompress)
                .isCamera(false)
                .startPick();
    }


    public void testBlockCanary(View view) {
//        SystemClock.sleep(2000);
        ImagePicker.with(this)
                .imagePickerListener(new ImagePicker.ImagePickerListener() {
                    @Override
                    public void onSuccess(@NotNull String imagePath) {
                        Log.e("TAG", "复制到APP外部私有目录地址Lib:" + imagePath);
                        Glide.with(LibTestJavaActivity.this).load(imagePath).into(imageView);
                    }

                    @Override
                    public void onFailed(@NotNull String msg, @NotNull String code) {
                        Log.e("TAG", msg);
                        showToast(msg);
                    }
                })
                .crop(true)
                .compress(true)
                .compressType(ImageCompress.ImageCompressType.OriginCompress)
                .isCamera(true)
                .startPick();
    }


    public void locationTest(View view) {
        tv_result.setText("开始定位：");
        PermissionUtils.getLocationPermission(this, new PermissionUtils.PermissionListener() {
            @Override
            public void allow() {
                LocationManager
                        .with(LibTestJavaActivity.this)
                        .autoSwitchLocation(false)
                        .defaultLocationType(ILocation.LocationType.GAODE)
                        .locationListener(new LocationListener() {
                            @Override
                            public void onLocation(@org.jetbrains.annotations.Nullable LocationData locationData) {
                                tv_result.setText("定位成功：" + locationData.toString());
                            }

                            @Override
                            public void onFail(int i, @org.jetbrains.annotations.Nullable String s) {
                                tv_result.setText("定位失败：" + s);
                            }
                        }).startLocation();
            }
        });
    }


    @Override
    public int getResourceId() {
        return R.layout.activity_libtest_java;
    }

    @Override
    public void initData() {
//        checkNeedPermission(initPermissionList());
    }

    public void testPermission(View view) {
        checkNeedPermission(initPermissionList());
    }

    /**
     * 1.在AndroidManifest.xml中添加所需权限
     * 2.动态获取权限
     */
    private List<String> initPermissionList() {
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        return new ArrayList<>(Arrays.asList(permissions));
    }

    private void checkNeedPermission(List<String> permissionList) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        int REQUEST_PERMISSION_CODE = 0x1001;
        List<String> noPermissionList = new ArrayList<>();
        for (String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                noPermissionList.add(permission);
            }
        }
        if (noPermissionList.size() > 0) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
            // 如果设备规范禁止应用具有该权限，此方法也会返回 false
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, noPermissionList.toArray(new String[0]), REQUEST_PERMISSION_CODE);
        }
    }


    /**
     * 5.请求权限后回调的方法
     *
     * @param requestCode  是我们自己定义的权限请求码
     * @param permissions  是我们请求的权限名称数组
     * @param grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限
     *                     名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionsDenied = false;//是否有权限被拒绝
        if (requestCode == 0x1001) {
            if (grantResults != null && grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        hasPermissionsDenied = true;
                        break;
                    }
                }
            }
            if (hasPermissionsDenied) {
                Toast.makeText(this, "you need allow some permissions", Toast.LENGTH_SHORT).show();
            } else {
                //have get all permissions
            }
        }
    }
}
