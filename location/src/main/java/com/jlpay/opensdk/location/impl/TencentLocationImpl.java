package com.jlpay.opensdk.location.impl;

import android.content.Context;
import android.text.TextUtils;

import com.jlpay.opensdk.location.LocationError;
import com.jlpay.opensdk.location.bean.LocationData;
import com.jlpay.opensdk.location.listener.LocationListener;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TencentLocationImpl implements ILocation, TencentLocationListener {

    //腾讯地图开放平台、定位接口文档、POI搜索文档地址
    //https://lbs.qq.com/mobile/androidLocationSDK/androidGeoGuide/androidGeoOverview
    //https://lbs.qq.com/geosdk/doc/
    //https://lbs.qq.com/AndroidDocs/doc_3d/index.html

    public static TencentLocationImpl INSTANCE;
    private Context mContext;
    private TencentLocationManager locationManager;
    private TencentLocationRequest request;
    private LocationListener locationListener;
    private Timer timer;

    public TencentLocationImpl(Context context) {
        initMap(context);
    }

    public static TencentLocationImpl getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TencentLocationImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TencentLocationImpl(context);
                }
            }
        }
        return INSTANCE;
    }

    private void initMap(Context context) {
        mContext = context.getApplicationContext();
        locationManager = TencentLocationManager.getInstance(mContext);
        request = TencentLocationRequest.create();
        request.setInterval(1000);//自定义定位间隔，时间单位为毫秒，不得小于1000毫秒:
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);//用户获取的位置信息的详细程度
        request.setAllowGPS(true);//是否允许使用GPS
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
                    synchronized (TencentLocationImpl.class) {
                        if (locationListener != null) {
                            locationListener.onFail(1, "定位超时");
                            locationListener = null;
                        }
                    }
                }
                stop();
            }
        }, TIMEOUT_DEFAULT * 1000);
        if (locationManager == null || request == null) {
            initMap(mContext);
        }
        locationManager.requestLocationUpdates(request, this);//发起连续定位请求
    }

    /**
     * 接收定位结果
     *
     * @param tencentLocation 新的位置, *可能*来自缓存. 定位失败时 location 无效或者为 null
     * @param errorCode       错误码, 仅当错误码为 TencentLocation.ERROR_OK 时表示定位成功, 为其他值时表示定位失败
     * @param reason          错误描述, 简要描述错误原因
     */
    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int errorCode, String reason) {
        if (locationListener == null) {
            return;
        }
        if (!isLocationValid(tencentLocation)) {
            locationListener.onFail(LocationError.ERR_OTHER, LocationError.ERR_OTHER_DESC);
            return;
        }
        if (errorCode != TencentLocation.ERROR_OK) {
            locationListener.onFail(LocationError.ERR_OTHER, reason);
            return;
        }

        LocationData locationData = new LocationData();
        locationData.setLatitude(tencentLocation.getLatitude());
        locationData.setLongitude(tencentLocation.getLongitude());
        locationData.setAddress(tencentLocation.getAddress());
        locationData.setProvince(tencentLocation.getProvince());
        locationData.setCity(tencentLocation.getCity());
        locationData.setArea(tencentLocation.getDistrict());
        locationData.setStreet(tencentLocation.getStreet());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(tencentLocation.getTime());
        locationData.setTime(simpleDateFormat.format(date));
        locationData.setAreaCode(tencentLocation.getCityCode());
        locationData.setCityCode(getCityCode(tencentLocation));
        locationListener.onLocation(locationData);

        stop();
    }


    /**
     * 定位是否是有效定位
     */
    private boolean isLocationValid(TencentLocation tencentLocation) {
        if (tencentLocation == null) {
            return false;
        }
        if (Math.abs(tencentLocation.getLatitude()) < 1) {
            return false;
        }
        if (Math.abs(tencentLocation.getLongitude()) < 1) {
            return false;
        }
        if (TextUtils.isEmpty(tencentLocation.getCity())) {
            return false;
        }
        return true;
    }

    /**
     * 接收GPS、WiFi、Cell状态码
     *
     * @param name   设备名, GPS, WIFI, CELL 中的某个
     * @param status 状态码, STATUS_ENABLED, STATUS_DISABLED, STATUS_UNKNOWN, STATUS_GPS_AVAILABLE, STATUS_GPS_UNAVAILABLE, STATUS_DENIED中的某个 在使用status之前,请先按照name进行区分
     * @param desc   状态描述
     */
    @Override
    public void onStatusUpdate(String name, int status, String desc) {

    }

    @Override
    public void stop() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager = null;
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
    private String getCityCode(TencentLocation location) {
        if (location != null) {
            String adCode = location.getCityCode();
            if (!TextUtils.isEmpty(adCode) && adCode.length() > 4) {
                String cityCode = adCode.substring(0, 4);
                return cityCode;
            }
        }
        return "";
    }
}
