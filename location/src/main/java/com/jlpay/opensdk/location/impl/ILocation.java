package com.jlpay.opensdk.location.impl;

import com.jlpay.opensdk.location.listener.LocationListener;

/**
 * 定位接口
 */
public interface ILocation {

    public static final int TIMEOUT_DEFAULT = 10;//默认定位超时时间，10s

    /**
     * 开始定位
     *
     * @return
     */
    void startLocation(LocationListener listener);


    /**
     * 停止定位
     */
    void stop();


    /**
     * 三种定位方式
     */
    public enum LocationType {
        BAIDU, GAODE, TENCENT
    }
}
