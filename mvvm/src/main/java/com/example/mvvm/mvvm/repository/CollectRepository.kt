package com.example.mvvm.mvvm.repository

import com.example.mvvm.httpUtils.BaseListResponseBody
import com.example.mvvm.httpUtils.CollectionArticle
import com.example.mvvm.httpUtils.ResponseData
import com.example.mvvm.httpUtils.RetrofitClient

class CollectRepository : CommonRepository() {

    suspend fun getCollectList(page: Int): ResponseData<BaseListResponseBody<CollectionArticle>> =
        request {
            RetrofitClient.service.getCollectList(page)
        }
}