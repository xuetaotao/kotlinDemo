package com.jlpay.kotlindemo.ui.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 * Float 扩展函数
 */
fun Float.dp2px(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
}

/**
 * Float 扩展属性
 */
val Float.px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )