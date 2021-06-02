package com.jlpay.opensdk.location;

import androidx.annotation.Nullable;

public class LocationError extends Exception {


    public static final int ERR_GPS_CLOSED = 2;//Gps未开启
    public static final int ERR_PERMISSION_NOT_GRANTED = 3;//未获取到定位相关权限
    public static final int ERR_OTHER = 4;
    public static final String ERR_OTHER_DESC = "定位失败";
    public static final int ERR_TIMEOUT = 5;
    public static final String ERR_TIMEOUT_DESC = "定位超时";
    public static final int ERR_POI_SEARCH_DATA_NULL = 6;
    public static final String ERR_POI_SEARCH_DATA_NULL_DESC = "输入搜索信息不能为空";
    public static final int ERR_POI_SEARCH_CITY_NULL = 7;
    public static final String ERR_POI_SEARCH_CITY_NULL_DESC = "城市信息不能为空";
    public static final int ERR_POI_SEARCH_KEYWORD_NULL = 8;
    public static final String ERR_POI_SEARCH_KEYWORD_NULL_DESC = "keyword信息不能为空";
    public static final int ERR_POI_SEARCH_PAGESIZE = 9;
    public static final String ERR_POI_SEARCH_PAGESIZE_DESC = "每页查询结果数目不能为0";
    public static final int ERR_POI_SEARCH_FAILED = 10;
    public static final String ERR_POI_SEARCH_FAILED_DESC = "未搜索到数据";
    public static final int ERR_LOCATION_SDK = 11;
    public static final String ERR_LOCATION_SDK_DESC = "缺少定位sdk";
    public static final int ERR_POI_SDK = 12;
    public static final String ERR_POI_SDK_DESC = "缺少百度POI搜索sdk";


    int errorCode;
    String errorMsg;


    public LocationError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    @Nullable
    @Override
    public String getMessage() {
        return errorMsg;
    }
}
