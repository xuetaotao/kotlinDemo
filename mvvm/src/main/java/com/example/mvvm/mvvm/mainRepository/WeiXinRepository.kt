package com.example.mvvm.mvvm.mainRepository

import com.example.mvvm.httpUtils.ResponseData
import com.example.mvvm.httpUtils.RetrofitClient
import com.example.mvvm.httpUtils.WXChapterBean
import com.example.mvvm.mvvm.repository.CommonRepository

class WeiXinRepository : CommonRepository() {

    suspend fun getWeiXin(): ResponseData<List<WXChapterBean>> = request {
        RetrofitClient.service.getWeiXin()
    }
}