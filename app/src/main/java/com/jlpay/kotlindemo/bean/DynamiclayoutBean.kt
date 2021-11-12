package com.jlpay.kotlindemo.bean

data class DynamiclayoutBean(
    val dynamicLayoutList: List<DynamicLayout>
)

data class DynamicLayout(
    val layoutLeft: String,
    val layoutRightHint: String,
    val name: String,
    val uiType: String,
    val resultType: String
)