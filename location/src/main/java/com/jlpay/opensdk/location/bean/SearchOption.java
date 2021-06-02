package com.jlpay.opensdk.location.bean;

/**
 * 搜索请求数据
 */
public class SearchOption {

    private String city;//城市名
    private String keyword;//关键词
    private int pageNum;//查询页码
    private int pageSize = 10;//每页最多返回多少条POI数据


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keywords) {
        this.keyword = keywords;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
