package com.example.mvvm.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.example.mvvm.httpUtils.ShareResponseBody
import com.example.mvvm.mvvm.repository.ShareRepository
import com.example.mvvm.utils.SingleLiveEvent

class ShareActivityViewModel : CommonViewModel() {

    private val repository = ShareRepository()
    private var data = SingleLiveEvent<Any>()
    private var shareList = SingleLiveEvent<ShareResponseBody>()
    private var delete = SingleLiveEvent<Any>()

    fun shareArticle(map: MutableMap<String, Any>): LiveData<Any> {
        launchUI {
            val res = repository.shareArticle(map)
            data.value = res.data
        }
        return data
    }

    fun getShareList(page: Int): LiveData<ShareResponseBody> {
        launchUI {
            val res = repository.getShareList(page)
            shareList.value = res.data
        }
        return shareList
    }

    fun deleteShareArticle(id: Int): LiveData<Any> {
        launchUI {
            val res = repository.deleteShareArticle(id)
            delete.value = res.data
        }
        return delete
    }
}