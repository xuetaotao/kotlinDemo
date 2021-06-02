-keep class com.baidu.** {*;}
-keep class mapsdkvi.com.** {*;}
-dontwarn com.baidu.**

-keep class com.jlpay.opensdk.location.bean.** {*;}
-keep class com.jlpay.opensdk.location.impl.** {*;}
-keep class com.jlpay.opensdk.location.listener.** {*;}
-keep class com.jlpay.opensdk.location.LocationManager
-keep class com.jlpay.opensdk.location.LocationError


#高德地图定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.loc.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}


#腾讯地图定位
-keepclassmembers class ** {
    public void on*Event(...);
}

-keep class c.t.**{*;}
-keep class com.tencent.map.geolocation.**{*;}

-dontwarn  org.eclipse.jdt.annotation.**
-dontwarn  c.t.**