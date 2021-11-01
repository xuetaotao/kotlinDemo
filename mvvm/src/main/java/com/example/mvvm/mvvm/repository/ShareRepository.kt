package com.example.mvvm.mvvm.repository

import com.example.mvvm.httpUtils.ResponseData
import com.example.mvvm.httpUtils.RetrofitClient
import com.example.mvvm.httpUtils.ShareResponseBody

class ShareRepository : CommonRepository() {

    suspend fun shareArticle(map: MutableMap<String, Any>): ResponseData<Any> = request {
        RetrofitClient.service.shareArticle(map)
    }

    suspend fun getShareList(page: Int): ResponseData<ShareResponseBody> = request {
        RetrofitClient.service.getShareList(page)
    }

    suspend fun deleteShareArticle(id: Int): ResponseData<Any> = request {
        RetrofitClient.service.deleteShareArticle(id)
    }
}