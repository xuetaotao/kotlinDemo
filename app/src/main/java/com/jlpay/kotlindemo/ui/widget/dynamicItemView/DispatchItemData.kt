package com.jlpay.kotlindemo.ui.widget.dynamicItemView

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.jlpay.kotlindemo.bean.DynamicLayout
import java.lang.reflect.Type

class DispatchItemData(
    private var linearLayout: ViewGroup,
    private var context: Context,
    private var dynamicLayoutList: List<DynamicLayout>
) {

    fun dispatchView() {
        dynamicLayoutList.forEach { dynamicLayout ->
            val view: View? = when (dynamicLayout.uiType) {
                "0" -> CustomItemView1(context, dynamicLayout)
                "1" -> CustomItemView2(context, dynamicLayout)
                else -> null
            }
            view?.let { linearLayout.addView(it) }
        }
    }


    fun getViewResult(viewId: String): String? {
        //获取返回结果的类型 TODO
        val resultType: Type
        dynamicLayoutList.forEach {
            if (it.name == viewId) {
                parseResultType(it.resultType)
            }
        }

        val childCount = linearLayout.childCount
        for (i in 0 until childCount) {
            try {
                val itemViewInterceptor: View = linearLayout.getChildAt(i)
                if (itemViewInterceptor is ItemViewInterceptor) {
                    if (itemViewInterceptor.getViewId() == viewId) {
                        return itemViewInterceptor.getResult()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    //TODO
    private fun parseResultType(resultType: String) {
        when (resultType) {
            "string" -> {
            }
            "double" -> {
            }
            "boolean" -> {
            }
        }
    }
}