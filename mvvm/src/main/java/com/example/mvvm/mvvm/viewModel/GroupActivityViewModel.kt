package com.example.mvvm.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.example.mvvm.httpUtils.ArticleResponseBody
import com.example.mvvm.mvvm.repository.GroupRepository
import com.example.mvvm.utils.SingleLiveEvent

class GroupActivityViewModel : CommonViewModel() {

    private val repository = GroupRepository()
    private val data = SingleLiveEvent<ArticleResponseBody>()

    fun getGroupList(page: Int): LiveData<ArticleResponseBody> {
        launchUI {
            val res = repository.getGroupList(page)
            data.value = res.data
        }
        return data
    }
}