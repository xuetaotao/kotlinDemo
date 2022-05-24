package com.jlpay.kotlindemo.study_jetpack.mvvm1

import android.widget.EditText


class ViewModel(et_name: EditText, et_password: EditText) {
    var name: StringAttr = StringAttr()
    var password: StringAttr = StringAttr()

    init {
        ViewBinder.bind(et_name, name)
        ViewBinder.bind(et_password, password)
    }

    fun init() {
        val data = DataCenter.getData()
        name.value = data[0]
        password.value = data[1]
    }
}