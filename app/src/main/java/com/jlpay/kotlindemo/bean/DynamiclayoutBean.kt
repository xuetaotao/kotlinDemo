package com.jlpay.kotlindemo.bean

data class DynamiclayoutBean(
    val `data`: List<Data>
)

data class Data(
//    val decimalDigits: String,
//    val feeRange: FeeRange,
//    val feeRangeMax: Int,
//    val feeRangeMin: Double,
//    val haveHint: String,
    val name: String,
    val type: String,
    val typeInput: String,
    val typeName: String
)

//data class FeeRange(
//    val 对公收款: String,
//    val 法人收款: String
//)