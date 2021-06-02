package com.jlpay.opensdk.location.listener;

import com.jlpay.opensdk.location.bean.SearchResult;

/**
 * 兴趣点搜索回调结果
 */
public interface SearchListener {

    void onSearch(SearchResult searchDataList);

    void onFail(int errorCode, String errorMsg);
}
