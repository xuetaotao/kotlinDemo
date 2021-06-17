package com.example.mvvm.mvvm.repository

import com.example.mvvm.base.BaseRepository
import com.example.mvvm.httpUtils.LoginData
import com.example.mvvm.httpUtils.ResponseData
import com.example.mvvm.httpUtils.RetrofitClient

/**
 * 通用的Repository
 * Created on 2021-1-13.
 * TODO unfinished
 */
open class CommonRepository : BaseRepository() {

    suspend fun login(name: String, psw: String): ResponseData<LoginData> = request {
        RetrofitClient.service.login(name, psw)
    }

    suspend fun logout(): ResponseData<Any> = request {
        RetrofitClient.service.logout()
    }
}