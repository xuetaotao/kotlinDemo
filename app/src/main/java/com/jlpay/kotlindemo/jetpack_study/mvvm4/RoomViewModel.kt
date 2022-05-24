package com.jlpay.kotlindemo.jetpack_study.mvvm4

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoomViewModel : ViewModel() {

    private val TAG: String = RoomViewModel::class.java.simpleName

    val person: MutableLiveData<List<Person>> = MutableLiveData<List<Person>>()

    private val roomRepository: RoomRepository = RoomRepository()

    fun insertPerson() {
        roomRepository.insertPerson(arrayOf(Person("hanfei", 18)))
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "RoomViewModel is onCleared")
    }
}