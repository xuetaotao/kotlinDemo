package com.jlpay.kotlindemo.utils

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


/**
 * Kotlin 在类外部声明一个方法，可以随便哪里调用，有点像Java的 static方法
 */
fun worldHHH() {

}