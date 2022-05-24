package com.jlpay.kotlindemo.study_jetpack.mvvm1

class StringAttr {
    var value: String? = null
        set(value) {
            field = value//内部值需要重新设置
            onChangeListener?.onChange(value)
        }
    var onChangeListener: OnChangeListener? = null

    interface OnChangeListener {
        fun onChange(newValue: String?)
    }
}