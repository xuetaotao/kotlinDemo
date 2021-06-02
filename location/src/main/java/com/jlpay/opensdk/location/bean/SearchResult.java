package com.jlpay.opensdk.location.bean;

import java.util.List;

/**
 * 兴趣点搜索结果
 */
public class SearchResult {

    public long total;

    public long currentPageNum;

    public long pageSize;

    public List<SearchData> results;


    public void setResults(List<SearchData> results) {
        this.results = results;
    }

    public List<SearchData> getResults() {
        return results;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public long getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(long currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }
}
