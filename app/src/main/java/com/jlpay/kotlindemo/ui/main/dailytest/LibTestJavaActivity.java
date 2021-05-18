package com.jlpay.kotlindemo.ui.main.dailytest;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.ui.base.BaseActivity;
import com.jlpay.kotlindemo.ui.utils.PermissionUtils;
import com.jlpay.opensdk.location.LocationManager;
import com.jlpay.opensdk.location.bean.LocationData;
import com.jlpay.opensdk.location.impl.LocationType;
import com.jlpay.opensdk.location.listener.LocationListener;

public class LibTestJavaActivity extends BaseActivity {

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, LibTestJavaActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void initView() {
        TextView tv_result = findViewById(R.id.tv_result);

        PermissionUtils.getLocationPermission(this, new PermissionUtils.PermissionListener() {
            @Override
            public void allow() {
                LocationManager
                        .with(LibTestJavaActivity.this)
                        .defaultLocationType(LocationType.TENCENT)
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

    }
}
