package com.jlpay.opensdk.location.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jlpay.opensdk.location.LocationError;
import com.jlpay.opensdk.location.bean.LocationData;
import com.jlpay.opensdk.location.listener.LocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GaodeLocationImpl implements ILocation, AMapLocationListener {

    //高德地图开放平台、定位接口文档、POI搜索接口文档地址
    //https://lbs.amap.com/api/android-location-sdk/locationsummary
    //http://amappc.cn-hangzhou.oss-pub.aliyun-inc.com/lbs/static/unzip/Android_Location_Doc/index.html
    //http://a.amap.com/lbs/static/unzip/Android_Map_Doc/index.html

    private static GaodeLocationImpl INSTANCE;
    private Context mContext;
    private AMapLocationClient aMapLocationClient;
    private LocationListener locationListener;
    private Timer timer;

    private GaodeLocationImpl(Context context) {
        initMap(context);
    }

    public static GaodeLocationImpl getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GaodeLocationImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GaodeLocationImpl(context);
                }
            }
        }
        return INSTANCE;
    }

    private void initMap(Context context) {
        mContext = context.getApplicationContext();
        aMapLocationClient = new AMapLocationClient(mContext);
        aMapLocationClient.setLocationListener(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//设置高精度定位模式
        option.setNeedAddress(true);//设置是否返回地址信息（默认返回地址信息）
        option.setInterval(1000);//设置连续定位间隔,单位毫秒,默认为2000ms，最低1000ms
        option.setHttpTimeOut(10000);//设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒
        option.setLocationCacheEnable(true);//设置是否开启定位缓存机制，默认开启，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存，GPS定位结果不会被缓存
        aMapLocationClient.setLocationOption(option);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.e("GaoLocationChanged:", aMapLocation.toStr());
        if (locationListener == null) {
            return;
        }
        if (!isLocationValid(aMapLocation)) {
            locationListener.onFail(LocationError.ERR_OTHER, LocationError.ERR_OTHER_DESC);
            stop();
            return;
        }
        if (aMapLocation.getErrorCode() != AMapLocation.LOCATION_SUCCESS) {
            locationListener.onFail(aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
            stop();
            return;
        }

        LocationData locationData = new LocationData();
        locationData.setLatitude(aMapLocation.getLatitude());
        locationData.setLongitude(aMapLocation.getLongitude());
        locationData.setAddress(aMapLocation.getAddress());
        locationData.setProvince(aMapLocation.getProvince());
        locationData.setCity(aMapLocation.getCity());
        locationData.setArea(aMapLocation.getDistrict());
        locationData.setStreet(aMapLocation.getStreet());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(aMapLocation.getTime());
        locationData.setTime(simpleDateFormat.format(date));
        locationData.setAreaCode(aMapLocation.getAdCode());
        locationData.setCityCode(getCityCode(aMapLocation));
        locationListener.onLocation(locationData);

        stop();
    }


    /**
     * 定位是否是有效定位
     */
    private boolean isLocationValid(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return false;
        }
        if (Math.abs(aMapLocation.getLatitude()) < 1) {
            return false;
        }
        if (Math.abs(aMapLocation.getLongitude()) < 1) {
            return false;
        }
        if (TextUtils.isEmpty(aMapLocation.getCity())) {
            return false;
        }
        return true;
    }

    @Override
    public void startLocation(LocationListener listener) {
        this.locationListener = listener;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (locationListener != null) {
                    synchronized (GaodeLocationImpl.class) {
                        if (locationListener != null) {
                            locationListener.onFail(1, "定位超时");
                            locationListener = null;
                        }
                    }
                }
                stop();
            }
        }, TIMEOUT_DEFAULT * 1000);

        if (aMapLocationClient == null) {
            initMap(mContext);
        }
        aMapLocationClient.startLocation();
    }

    @Override
    public void stop() {
        if (aMapLocationClient != null) {
            aMapLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
//            aMapLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务，若要重新开启定位请重新New一个AMapLocationClient对象。
        }
        if (locationListener != null) {
            locationListener = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    /**
     * 获取定位信息
     *
     * @param location
     * @return
     */
    private String getCityCode(AMapLocation location) {
        if (location != null) {
            String adCode = location.getAdCode();
            if (!TextUtils.isEmpty(adCode) && adCode.length() > 4) {
                String cityCode = adCode.substring(0, 4);
                return cityCode;
            }
        }
        return "";
    }

}
