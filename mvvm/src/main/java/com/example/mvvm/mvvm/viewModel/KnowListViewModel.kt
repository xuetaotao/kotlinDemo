package com.example.mvvm.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.example.mvvm.httpUtils.ArticleResponseBody
import com.example.mvvm.mvvm.mainRepository.SystemRepository
import com.example.mvvm.utils.SingleLiveEvent

class KnowListViewModel : CommonViewModel() {

    private val repository = SystemRepository()
    private val articleResponse = SingleLiveEvent<ArticleResponseBody>()

    fun getKnowledgeList(page: Int, cid: Int): LiveData<ArticleResponseBody> {
        launchUI {
            val result = repository.getKnowledgeList(page, cid)
            articleResponse.value = result.data
        }
        return articleResponse
    }
}