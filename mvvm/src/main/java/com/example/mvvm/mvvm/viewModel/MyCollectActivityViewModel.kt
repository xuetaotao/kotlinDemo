package com.example.mvvm.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.example.mvvm.httpUtils.BaseListResponseBody
import com.example.mvvm.httpUtils.CollectionArticle
import com.example.mvvm.mvvm.repository.CollectRepository
import com.example.mvvm.utils.SingleLiveEvent

class MyCollectActivityViewModel : CommonViewModel() {

    private val repository = CollectRepository()
    private val collectList = SingleLiveEvent<BaseListResponseBody<CollectionArticle>>()

    fun getCollectList(page: Int): LiveData<BaseListResponseBody<CollectionArticle>> {
        launchUI {
            val res = repository.getCollectList(page)
            collectList.value = res.data
        }
        return collectList
    }
}