package com.jlpay.opensdk.location.listener;

import com.jlpay.opensdk.location.bean.LocationData;


/**
 * 定位结果回调
 */
public interface LocationListener {

    void onLocation(LocationData locationData);

    void onFail(int errorCode, String errorMsg);
}
