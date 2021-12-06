package com.jlpay.kotlindemo.bean

import androidx.databinding.ObservableField

data class DynamiclayoutBean(
    var dynamicLayoutList: List<DynamicLayout>
)

data class DynamicLayout(
    var layoutLeft: String,
    var layoutRightHint: String,
    var layoutRight: String,
    var name: String,
    var uiType: String,
    var resultType: String
)