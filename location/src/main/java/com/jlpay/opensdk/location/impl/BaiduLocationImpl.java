package com.jlpay.opensdk.location.impl;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.jlpay.opensdk.location.LocationError;
import com.jlpay.opensdk.location.bean.LocationData;
import com.jlpay.opensdk.location.bean.SearchData;
import com.jlpay.opensdk.location.bean.SearchOption;
import com.jlpay.opensdk.location.bean.SearchResult;
import com.jlpay.opensdk.location.listener.LocationListener;
import com.jlpay.opensdk.location.listener.SearchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 百度地图实现类
 */
public class BaiduLocationImpl extends BDAbstractLocationListener implements ILocation, IPoiSearch {

    private LocationClient mLocationClient;
    private LocationListener locationListener;
    private Context mContext;
    private Timer timer;

    private static BaiduLocationImpl INSTANCE;

    public static BaiduLocationImpl getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BaiduLocationImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BaiduLocationImpl(context);
                }
            }
        }
        return INSTANCE;
    }


    private BaiduLocationImpl(Context context) {
        initMap(context);
    }

    private void initMap(Context context) {
        mContext = context.getApplicationContext();
        SDKInitializer.initialize(mContext);//百度搜索定位初始化
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
        option.setIsNeedAddress(true);//需要获取地址
        int span = 1000;
        option.setScanSpan(span);//设置发起定位请求的间隔时间为1000ms
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setOpenGps(true);//
        mLocationClient = new LocationClient(context.getApplicationContext(), option);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(this);

    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (locationListener == null) {
            return;
        }
        if (!isLocationValid(bdLocation)) {
            locationListener.onFail(LocationError.ERR_OTHER, LocationError.ERR_OTHER_DESC);
            stop();
            return;
        }
        locationListener.onLocation(changeLocation(bdLocation));
        stop();
    }


    @Override
    public void startLocation(LocationListener listener) {

        this.locationListener = listener;

        //计算超时时间
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (locationListener != null) {
                    synchronized (BaiduLocationImpl.class) {
                        if (locationListener != null) {
                            locationListener.onFail(1, "定位超时");
                            locationListener = null;
                        }
                    }
                }
                stop();
            }
        }, TIMEOUT_DEFAULT * 1000);

        if (mLocationClient == null) {
            initMap(mContext);
        }

        mLocationClient.start();
    }


    @Override
    public void stop() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        if (locationListener != null) {
            locationListener = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void startSearch(SearchOption searchData, SearchListener listener) {
        if (searchData == null) {
            if (listener != null) {
                listener.onFail(LocationError.ERR_POI_SEARCH_DATA_NULL, LocationError.ERR_POI_SEARCH_DATA_NULL_DESC);
            }
            return;
        }
        if (TextUtils.isEmpty(searchData.getCity())) {
            if (listener != null) {
                listener.onFail(LocationError.ERR_POI_SEARCH_CITY_NULL, LocationError.ERR_POI_SEARCH_CITY_NULL_DESC);
            }
            return;
        }
        if (TextUtils.isEmpty(searchData.getKeyword())) {
            if (listener != null) {
                listener.onFail(LocationError.ERR_POI_SEARCH_KEYWORD_NULL, LocationError.ERR_POI_SEARCH_KEYWORD_NULL_DESC);
            }
            return;
        }

        PoiSearch poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null || poiResult.getAllPoi() == null || poiResult.getAllPoi().size() == 0) {
                    listener.onFail(LocationError.ERR_POI_SEARCH_FAILED, LocationError.ERR_POI_SEARCH_FAILED_DESC);
                    return;
                }
                List<PoiInfo> allPoi = poiResult.getAllPoi();
                List<SearchData> searchResults = new ArrayList<>();
                for (PoiInfo poiInfo : allPoi) {
                    SearchData result = new SearchData();
                    result.setName(poiInfo.getName());
                    result.setAddress(poiInfo.getAddress());
                    result.setLatitude(poiInfo.getLocation().latitude);
                    result.setLongitude(poiInfo.getLocation().longitude);
                    searchResults.add(result);
                }
                SearchResult result = new SearchResult();
                result.setTotal(poiResult.getTotalPoiNum());
                result.setCurrentPageNum(poiResult.getCurrentPageNum());
                result.setPageSize(poiResult.getCurrentPageCapacity());
                result.setResults(searchResults);
                if (listener != null) {
                    listener.onSearch(result);
                }

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }

            //废弃
            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });

        poiSearch.searchInCity(
                new PoiCitySearchOption()
                        .city(searchData.getCity())
                        .keyword(searchData.getKeyword())
                        .pageNum(searchData.getPageNum())
                        .pageCapacity(searchData.getPageSize()));


    }


    /**
     * 定位是否是有效定位
     */
    private boolean isLocationValid(BDLocation location) {

        if (location == null) {
            return false;
        }

        if (location.getLocType() == BDLocation.TypeServerError) {
            return false;
        }

        if (Math.abs(location.getLatitude()) < 1) {
            return false;
        }

        if (Math.abs(location.getLongitude()) < 1) {
            return false;
        }

        if (TextUtils.isEmpty(location.getCity())) {
            return false;
        }
        return true;
    }


    /**
     * 统一转换JL LocationData
     *
     * @param location
     * @return
     */
    private LocationData changeLocation(BDLocation location) {

        if (location == null) {
            return null;
        }
        LocationData jLlocation = new LocationData();
        jLlocation.setLatitude(location.getLatitude());
        jLlocation.setLongitude(location.getLongitude());
        jLlocation.setAddress(location.getAddrStr());
        jLlocation.setCity(location.getCity());
        jLlocation.setProvince(location.getProvince());
        jLlocation.setArea(location.getDistrict());
        jLlocation.setStreet(location.getStreet());
        jLlocation.setAreaCode(location.getAdCode());
        jLlocation.setCityCode(getCityCode(location));
        jLlocation.setTime(location.getTime());

        return jLlocation;
    }


    /**
     * 获取定位信息
     *
     * @param location
     * @return
     */
    private String getCityCode(BDLocation location) {
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
