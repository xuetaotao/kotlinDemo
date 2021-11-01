package com.example.mvvm.mvvm.mainViewModel

import androidx.lifecycle.LiveData
import com.example.mvvm.httpUtils.WXChapterBean
import com.example.mvvm.mvvm.mainRepository.WeiXinRepository
import com.example.mvvm.mvvm.viewModel.CommonViewModel
import com.example.mvvm.utils.SingleLiveEvent

class WeiXinViewModel : CommonViewModel() {

    private val repository = WeiXinRepository()
    private val weiXinData = SingleLiveEvent<List<WXChapterBean>>()

    fun getWeiXin(): LiveData<List<WXChapterBean>> {
        launchUI {
            val result = repository.getWeiXin()
            weiXinData.value = result.data
        }
        return weiXinData
    }
}