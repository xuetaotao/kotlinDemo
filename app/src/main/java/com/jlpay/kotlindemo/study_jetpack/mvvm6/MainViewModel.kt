package com.jlpay.kotlindemo.study_jetpack.mvvm6

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val TAG = MainViewModel::class.java.simpleName
    private val wanService by lazy {
        MainRepository.getWanService()
    }

    var userLiveData = MutableLiveData<MyUserBean>()


    fun getUser(username: String, password: String) {
//        viewModelScope.launch(Dispatchers.IO) {//这样会崩溃
        //默认不指定Dispatchers的话，看结果应该是在main线程，这个和GlobalScope默认是Dispatchers.Default不同
        viewModelScope.launch {
            Log.e(TAG, "getUser-->1: ${Thread.currentThread().name}")
            //耗时操作这里会自动启动一个IO线程（Retrofit自动操作）
            val result = wanService.loginForm(username, password)
            userLiveData.value = result.data!!
            Log.e(TAG, "getUser-->2: ${Thread.currentThread().name}")
        }
        Log.e(TAG, "getUser-->3: ${Thread.currentThread().name}")
        //结果：
        //getUser-->1: main
        //getUser-->3: main
        //getUser-->2: main
    }

    fun getUser2(username: String, password: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                Log.e(TAG, "getUser2-->1: ${Thread.currentThread().name}")
                wanService.loginForm(username, password)
            }
            Log.e(TAG, "getUser2-->2: ${Thread.currentThread().name}")
            userLiveData.value = result.data!!
        }
        Log.e(TAG, "getUser2-->3: ${Thread.currentThread().name}")
        //结果：
        //getUser2-->3: main
        //getUser2-->1: DefaultDispatcher-worker-1
        //getUser2-->2: main
    }

    //TODO 这样写不行，原因不知，以后再研究
    fun getUser3(username: String, password: String) {
        viewModelScope.launch {
            //(Dispatchers.IO)可以不写
            val temp: LiveData<MyUserBean> = liveData<MyUserBean>(Dispatchers.IO) {
                val result = wanService.loginForm(username, password)
                result.data?.let { emit(it) }
            }
            userLiveData = temp as MutableLiveData<MyUserBean>//TODO 这里不能直接赋值，原因不知
        }
    }
}