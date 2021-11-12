package com.jlpay.kotlindemo.ui.widget.dynamicItemView

import com.jlpay.kotlindemo.bean.DynamicLayout

interface ItemViewInterceptor {

    fun inflateView(dynamicLayout: DynamicLayout)

    fun getResult(): String

    //回显
    fun setResult(result: String)

//    fun getResultAdvance(): T //TODO  结果返回

    fun getViewId(): String

    fun setViewId(viewId: String)
}