package com.example.mvvm.mvvm.mainRepository

import com.example.mvvm.httpUtils.*
import com.example.mvvm.mvvm.repository.CommonRepository

class SystemRepository : CommonRepository() {

    suspend fun getKnowledgeTree(): ResponseData<List<KnowledgeTreeBody>> = request {
        RetrofitClient.service.getKnowledgeTree()
    }

    suspend fun getNavigationTree(): ResponseData<List<NavigationBean>> = request {
        RetrofitClient.service.getNavigationList()
    }

    suspend fun getKnowledgeList(page: Int, cid: Int): ResponseData<ArticleResponseBody> = request {
        RetrofitClient.service.getKnowledgeList(page, cid)
    }
}