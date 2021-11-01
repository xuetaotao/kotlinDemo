package com.example.mvvm.mvvm.mainViewModel

import androidx.lifecycle.LiveData
import com.example.mvvm.httpUtils.UserInfoBody
import com.example.mvvm.mvvm.mainRepository.MyRepository
import com.example.mvvm.mvvm.viewModel.CommonViewModel
import com.example.mvvm.utils.SingleLiveEvent

class MyViewModel : CommonViewModel() {

    private val repository = MyRepository()
    private val userInfo = SingleLiveEvent<UserInfoBody>()

    fun getUserInfo(): LiveData<UserInfoBody> {
        launchUI {
            val result = repository.getUserInfo()
            userInfo.value = result.data
        }
        return userInfo
    }
}