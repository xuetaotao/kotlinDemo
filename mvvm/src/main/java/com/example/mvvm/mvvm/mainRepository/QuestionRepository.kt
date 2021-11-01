package com.example.mvvm.mvvm.mainRepository

import com.example.mvvm.httpUtils.ArticleListBean
import com.example.mvvm.httpUtils.ResponseData
import com.example.mvvm.httpUtils.RetrofitClient
import com.example.mvvm.mvvm.repository.CommonRepository

class QuestionRepository : CommonRepository() {

    suspend fun getQuestionList(page: Int): ResponseData<ArticleListBean> = request {
        RetrofitClient.service.getQuestionList(page)
    }
}