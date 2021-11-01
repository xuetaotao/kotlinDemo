package com.example.mvvm.mvvm.repository

import com.example.mvvm.httpUtils.ArticleResponseBody
import com.example.mvvm.httpUtils.ResponseData
import com.example.mvvm.httpUtils.RetrofitClient

class GroupRepository : CommonRepository() {

    suspend fun getGroupList(page: Int): ResponseData<ArticleResponseBody> = request {
        RetrofitClient.service.getGroupList(page)
    }
}