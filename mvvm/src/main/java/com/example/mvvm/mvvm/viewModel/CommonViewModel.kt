package com.example.mvvm.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.example.mvvm.base.BaseViewModel
import com.example.mvvm.httpUtils.LoginData
import com.example.mvvm.mvvm.repository.CommonRepository
import com.example.mvvm.utils.SingleLiveEvent

/**
 * 通用的ViewModel,如收藏,登录等接口很多页面都用
 * Created on 2021-1-15.
 * TODO unfinished
 */
open class CommonViewModel : BaseViewModel() {

    private val repository = CommonRepository()

    private val loginData = SingleLiveEvent<LoginData>()
    private val logoutData = SingleLiveEvent<Any>()


    fun login(name: String, psw: String): LiveData<LoginData> {
        launchUI {
            val result = repository.login(name, psw)
            loginData.value = result.data
        }
        return loginData
    }

    fun logout(): LiveData<Any> {
        launchUI {
            val result = repository.logout()
            logoutData.value = result.data
        }
        return logoutData
    }
}