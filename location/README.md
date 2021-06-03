## 定位搜索

## Use case:

### 1.first add config in project build.gradle


    allprojects {
        repositories {
            //嘉联自定义组件库
            maven {
             url 'http://sharesdk.jlpay.com/repository/android-private/'       //生产
             credentials {
                username = 'public'
                password = 'P1ic223zk'
             }
               }
               maven {
                url 'http://sharesdk.jlpay.com/repository/android-private-snapshot/'       //测试
             credentials {
                    username = 'report'
                    password = 'R86c12893x'
             }
           }
        }
    }

### 2.add implemtion in module:

        implementation 'com.jlpay.opensdk:location:1.0.4'

        //腾讯定位，按需添加
        implementation 'com.tencent.map.geolocation:TencentLocationSdk-openplatform:7.2.6'

        //高德定位，按需添加
        implementation 'com.amap.api:location:5.1.0'


### 3.add manifestPlaceholders

    defaultConfig {
        manifestPlaceholders = [
                BAIDU_MAP_API_KEY : "xxxxxxx",
                AMAP_API_KEY  : "xxxxxxx",
                TENCENT_MAP_API_KEY: "xxxxxxx"
        ]
    }

### 4.startLocation

     //新版本
     //注：新版本默认支持百度地图定位，若需要指定默认为高德或腾讯地图，需要在第二步中添加依赖库，并更新第三步中的API_KEY
     LocationManager
             .with(this)
             //.autoSwitchLocation(false)//是否自动切换定位，默认为true
             //.defaultLocationType(ILocation.LocationType.GAODE)//设置默认定位方式，默认为百度
             .locationListener(new LocationListener() {
                  @Override
                  public void onLocation(LocationData locationData) {

                  }

                  @Override
                  public void onFail(int errorCode, String errorMsg) {

                  }
              }).starLocation();


