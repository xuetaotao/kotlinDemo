package com.jlpay.opensdk.location.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import androidx.core.content.ContextCompat;

/**
 * 检查硬
 */
public class CheckUtil {

    /**
     * GPS是否开启
     *
     * @return
     */
    public static boolean isCheckGpsOpen(Context context) {
        if (context == null) {
            return false;
        }
        boolean isOpen = false;

        LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
            isOpen = true;
        } else if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            isOpen = true;
        }
        return isOpen;
    }

    /**
     * 检查是否获取到定位权限
     *
     * @param mContext
     * @return
     */
    public static boolean checkPermission(Context mContext) {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

}
