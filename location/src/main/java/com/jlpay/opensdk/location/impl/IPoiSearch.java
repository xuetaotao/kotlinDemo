package com.jlpay.opensdk.location.impl;

import com.jlpay.opensdk.location.bean.SearchOption;
import com.jlpay.opensdk.location.listener.SearchListener;

public interface IPoiSearch {

    /**
     * 搜索兴趣点信息
     *
     * @param searchData 搜索内容
     * @param listener   回调结果
     */
    void startSearch(SearchOption searchData, SearchListener listener);
}
