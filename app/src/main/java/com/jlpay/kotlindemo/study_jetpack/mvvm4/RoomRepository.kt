package com.jlpay.kotlindemo.study_jetpack.mvvm4

class RoomRepository() {

    fun insertPerson(persons: Array<Person>) {
        DataBaseRoom.getDataBaseInstance()?.getPersonDao()
            ?.insert(persons)//测试使用
    }
}