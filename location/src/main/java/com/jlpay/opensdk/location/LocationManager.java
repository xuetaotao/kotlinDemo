package com.jlpay.opensdk.location;

import android.annotation.SuppressLint;
import android.content.Context;

import com.jlpay.opensdk.location.bean.LocationData;
import com.jlpay.opensdk.location.bean.SearchOption;
import com.jlpay.opensdk.location.bean.SearchResult;
import com.jlpay.opensdk.location.impl.BaiduLocationImpl;
import com.jlpay.opensdk.location.impl.GaodeLocationImpl;
import com.jlpay.opensdk.location.impl.ILocation;
import com.jlpay.opensdk.location.impl.IPoiSearch;
import com.jlpay.opensdk.location.impl.TencentLocationImpl;
import com.jlpay.opensdk.location.listener.LocationListener;
import com.jlpay.opensdk.location.listener.SearchListener;
import com.jlpay.opensdk.location.utils.CheckUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class LocationManager implements ILocation, IPoiSearch {

    private final int MAX_ERROR_TIME = 3;//默认最大允许定位失败次数

    private Context mContext;
    private ILocation iLocation;
    private IPoiSearch iPoiSearch;
    private boolean baiduSdk = false;
    private boolean gaodeSdk = false;
    private boolean tencentSdk = false;//百度，高德，腾讯定位sdk的可用状态，默认不可用
    private boolean autoSwitch = false;//是否自动切换定位方式：百度/高德/腾讯
    private int locationErrorTime = 0;//定位失败次数
    private boolean cacheEnable;//内存存储，默认true
    private static LocationData mLocationData;//保存的定位数据
    private static LocationManager locationManager = null;

    private static LocationManager getInstance(Build build) {
        if (locationManager == null) {
            synchronized (LocationManager.class) {
                if (locationManager == null) {
                    locationManager = new LocationManager(build);
                }
            }
        }
        return locationManager;
    }

    private LocationManager(Build build) {
        mContext = build.context;
        iLocation = build.iLocation;
        iPoiSearch = build.iPoiSearch;
        baiduSdk = build.baiduSdk;
        gaodeSdk = build.gaodeSdk;
        tencentSdk = build.tencentSdk;
        autoSwitch = build.autoSwitch;
        cacheEnable = build.cacheEnable;
    }


    @SuppressLint("CheckResult")
    @Override
    public void startLocation(LocationListener listener) {
        Observable.just(0)
                //检查权限处理
                .compose(new ObservableTransformer<Integer, Boolean>() {
                    @Override
                    public ObservableSource<Boolean> apply(Observable<Integer> upstream) {
                        if (mContext == null) {
                            return Observable.error(new LocationError(LocationError.ERR_OTHER, "context不能为空"));
                        }
                        if (iLocation == null) {
                            return Observable.error(new LocationError(LocationError.ERR_OTHER, "定位代理获取失败"));
                        }
                        //这里取消了自动获取权限的配置，因为ApplicationContext的原因
                        return CheckUtil.checkPermission(mContext) ? Observable.just(true) :
                                Observable.error(new LocationError(LocationError.ERR_PERMISSION_NOT_GRANTED, "未获取到定位相关权限"));
                    }
                })
                //检查Gps开关是否ok
                .compose(new ObservableTransformer<Boolean, Boolean>() {
                    @Override
                    public ObservableSource<Boolean> apply(Observable<Boolean> upstream) {

                        return CheckUtil.isCheckGpsOpen(mContext) ? upstream :
                                Observable.error(new LocationError(LocationError.ERR_GPS_CLOSED, "Gps未开启"));
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<LocationData>>() {
                    @Override
                    public ObservableSource<LocationData> apply(Boolean aBoolean) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<LocationData>() {
                            @Override
                            public void subscribe(ObservableEmitter<LocationData> emitter) throws Exception {
                                iLocation.startLocation(new LocationListener() {
                                    @Override
                                    public void onLocation(LocationData locationData) {
                                        emitter.onNext(locationData);
                                    }

                                    @Override
                                    public void onFail(int errorCode, String errorMsg) {
                                        emitter.onError(new Exception(errorMsg));
                                    }
                                });
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LocationData>() {
                    @Override
                    public void accept(LocationData locationData) throws Exception {
                        saveLocationData(locationData);
                        locationErrorTime = 0;
                        if (listener != null) {
                            listener.onLocation(locationData);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        locationErrorTime++;
                        autoSwitchLocationType();
                        LocationError locationError;
                        if (throwable instanceof LocationError) {
                            locationError = (LocationError) throwable;
                        } else {
                            locationError = new LocationError(LocationError.ERR_OTHER, throwable.getMessage());
                        }
                        if (listener != null) {
                            listener.onFail(locationError.errorCode, locationError.errorMsg);
                        }
                    }
                });
    }


    @Override
    public void stop() {
        if (iLocation != null) {
            iLocation.stop();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void startSearch(SearchOption searchData, SearchListener listener) {
        Observable.just(0)
                .flatMap(new Function<Integer, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Integer integer) throws Exception {
                        if (iPoiSearch == null) {
                            return Observable.error(new LocationError(LocationError.ERR_OTHER, "兴趣点代理获取失败"));
                        }
                        if (searchData == null) {
                            return Observable.error(new LocationError(LocationError.ERR_OTHER, "搜索数据不能为空"));
                        }
                        return Observable.just(0);
                    }
                })
                .flatMap(new Function<Object, ObservableSource<SearchResult>>() {
                    @Override
                    public ObservableSource<SearchResult> apply(Object o) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<SearchResult>() {
                            @Override
                            public void subscribe(ObservableEmitter<SearchResult> emitter) throws Exception {
                                iPoiSearch.startSearch(searchData, new SearchListener() {
                                    @Override
                                    public void onSearch(SearchResult searchDataList) {
                                        emitter.onNext(searchDataList);
                                    }

                                    @Override
                                    public void onFail(int errorCode, String errorMsg) {
                                        emitter.onError(new Exception(errorMsg));
                                    }
                                });
                            }
                        });
                    }
                })
                .subscribe(new Consumer<SearchResult>() {
                    @Override
                    public void accept(SearchResult searchResult) throws Exception {
                        if (listener != null) {
                            listener.onSearch(searchResult);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (listener != null) {
                            listener.onFail(LocationError.ERR_OTHER, throwable.getMessage());
                        }
                    }
                });
    }

    /**
     * 保存数据
     */
    private void saveLocationData(LocationData locationData) {
        if (cacheEnable) {
            mLocationData = locationData;
        }
    }

    /**
     * 获取缓存定位数据
     */
    public static LocationData getLocationData() {
        return mLocationData;
    }

    public void switchLocationType(LocationType locationType) {
        switch (locationType) {
            case GAODE:
                if (gaodeSdk) {
                    this.iLocation = GaodeLocationImpl.getInstance(mContext);
                } else if (tencentSdk) {
                    this.iLocation = TencentLocationImpl.getInstance(mContext);
                } else if (baiduSdk) {
                    this.iLocation = BaiduLocationImpl.getInstance(mContext);
                }
                break;
            case TENCENT:
                if (tencentSdk) {
                    this.iLocation = TencentLocationImpl.getInstance(mContext);
                } else if (baiduSdk) {
                    this.iLocation = BaiduLocationImpl.getInstance(mContext);
                } else if (gaodeSdk) {
                    this.iLocation = GaodeLocationImpl.getInstance(mContext);
                }
                break;
            case BAIDU:
            default:
                if (baiduSdk) {
                    this.iLocation = BaiduLocationImpl.getInstance(mContext);
                }
                break;
        }
    }

    /**
     * 自动切换定位方式：百度/高德/腾讯
     * 默认自动切换条件为：连续三次定位失败
     */
    private void autoSwitchLocationType() {
        if (!autoSwitch) {
            return;
        }
        if (locationErrorTime < MAX_ERROR_TIME) {
            return;
        }
        if (iLocation == null) {
            return;
        }
        if (iLocation instanceof BaiduLocationImpl) {
            switchLocationType(LocationType.GAODE);
        } else if (iLocation instanceof GaodeLocationImpl) {
            switchLocationType(LocationType.TENCENT);
        } else {
            switchLocationType(LocationType.BAIDU);
        }

        locationErrorTime = 0;
    }

    public static Build with(Context context) {
        return new Build(context);
    }


    public static class Build {
        private Context context;
        private ILocation iLocation;
        private IPoiSearch iPoiSearch;
        private LocationListener locationListener;
        private int locationTypeNum = 0;//可使用的定位方式数量
        private boolean baiduSdk = false;
        private boolean gaodeSdk = false;
        private boolean tencentSdk = false;//百度，高德，腾讯定位sdk的可用状态，默认不可用
        private boolean autoSwitch = true;//是否自动切换定位方式：百度/高德/腾讯，默认是
        private boolean cacheEnable = true;//是否允许内存存储


        private Build(Context context) {
            this.context = context.getApplicationContext();
            baiduSdk = checkBaiduLocationSdk();
            gaodeSdk = checkGaodeLocationSdk();
            tencentSdk = checkTencentLocationSdk();
            if (baiduSdk) {
                this.iLocation = BaiduLocationImpl.getInstance(context);
                this.iPoiSearch = BaiduLocationImpl.getInstance(context);
                locationTypeNum += 1;
            }
            if (gaodeSdk) {
                locationTypeNum += 1;
            }
            if (tencentSdk) {
                locationTypeNum += 1;
            }
        }

        private boolean checkGaodeLocationSdk() {
            try {
                Class<?> clazz = Class.forName("com.amap.api.location.AMapLocationClient");
            } catch (ClassNotFoundException e) {
                return false;
            }
            return true;
        }

        private boolean checkTencentLocationSdk() {
            try {
                Class<?> clazz = Class.forName("com.tencent.map.geolocation.TencentLocationManager");
            } catch (ClassNotFoundException e) {
                return false;
            }
            return true;
        }

        private boolean checkBaiduLocationSdk() {
            try {
                //百度的定位和POI搜索在同一个jar包中，故这里没有再做POI的类发射检查；高德腾讯不在
                Class<?> clazz = Class.forName("com.baidu.location.LocationClient");
            } catch (ClassNotFoundException e) {
                return false;
            }
            return true;
        }

        public Build cacheEnable(boolean enable) {
            this.cacheEnable = enable;
            return this;
        }

        public Build defaultLocationType(LocationType locationType) {
            if (locationType == LocationType.BAIDU && baiduSdk) {
                this.iLocation = BaiduLocationImpl.getInstance(context);
            } else if (locationType == LocationType.GAODE && gaodeSdk) {
                this.iLocation = GaodeLocationImpl.getInstance(context);
            } else if (locationType == LocationType.TENCENT && tencentSdk) {
                this.iLocation = TencentLocationImpl.getInstance(context);
            }
            return this;
        }

        public Build autoSwitchLocation(boolean autoSwitch) {
            if (locationTypeNum > 1) {
                this.autoSwitch = autoSwitch;
            } else {
                this.autoSwitch = false;
            }
            return this;
        }

        public Build locationListener(LocationListener listener) {
            this.locationListener = listener;
            return this;
        }

        private LocationManager build() {
            return getInstance(this);
        }

        public void startLocation() {
            if ((!baiduSdk) && (!gaodeSdk) && (!tencentSdk)) {//检查是否有定位sdk
                if (locationListener != null) {
                    locationListener.onFail(LocationError.ERR_LOCATION_SDK, LocationError.ERR_LOCATION_SDK_DESC);
                }
                return;
            }
            build().startLocation(locationListener);
        }

        public void startSearch(SearchOption searchData, SearchListener listener) {
            if (!baiduSdk) {//检查是否有百度POI搜索sdk(和百度定位同一个jar包)
                if (listener != null) {
                    listener.onFail(LocationError.ERR_POI_SDK, LocationError.ERR_POI_SDK_DESC);
                }
                return;
            }
            build().startSearch(searchData, listener);
        }
    }

}
