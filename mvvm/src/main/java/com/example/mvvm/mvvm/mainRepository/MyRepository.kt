package com.example.mvvm.mvvm.mainRepository

import com.example.mvvm.httpUtils.ResponseData
import com.example.mvvm.httpUtils.RetrofitClient
import com.example.mvvm.httpUtils.UserInfoBody
import com.example.mvvm.mvvm.repository.CommonRepository

class MyRepository : CommonRepository() {

    suspend fun getUserInfo(): ResponseData<UserInfoBody> = request {
        RetrofitClient.service.getUserInfo()
    }
}