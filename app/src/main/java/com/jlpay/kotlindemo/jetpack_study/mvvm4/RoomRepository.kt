package com.jlpay.kotlindemo.jetpack_study.mvvm4

class RoomRepository() {

    fun insertPerson(persons: Array<Person>) {
        DataBaseRoom.getDataBaseInstance()?.getPersonDao()
            ?.insert(persons)//测试使用
    }
}