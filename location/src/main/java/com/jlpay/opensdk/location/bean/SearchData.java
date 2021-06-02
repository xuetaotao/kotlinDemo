package com.jlpay.opensdk.location.bean;

/**
 * 兴趣点搜索
 */
public class SearchData {


    private String name;//搜索结果名

    private String address;//结果详细地址

    private double latitude;//当前结果纬度

    private double longitude;//当前结果经度


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
