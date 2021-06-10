package com.jlpay.kotlindemo.ui.main.jetpack.mvvm.learn3

class RoomRepository() {

    fun insertPerson(persons: Array<Person>) {
        DataBaseRoom.getDataBaseInstance()?.getPersonDao()
            ?.insert(persons)//测试使用
    }
}