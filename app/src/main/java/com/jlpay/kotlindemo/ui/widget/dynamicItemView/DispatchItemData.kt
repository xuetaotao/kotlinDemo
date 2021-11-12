package com.jlpay.kotlindemo.ui.widget.dynamicItemView

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.jlpay.kotlindemo.bean.DynamicLayout
import com.jlpay.kotlindemo.bean.DynamiclayoutBean
import java.lang.reflect.Type

fun parseJsonFile(context: Context): DynamiclayoutBean? {
    var dynamiclayoutBean: DynamiclayoutBean? = null
    try {
        val openInputStream = context.assets.open("dynamic_layout.json")
        openInputStream.use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                //创建数据类型
                dynamiclayoutBean =
                    Gson().fromJson<DynamiclayoutBean>(jsonReader, DynamiclayoutBean::class.java)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return dynamiclayoutBean
}


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
        dynamicLayoutList.forEach { it ->
            if (it.name == viewId) {
                //暂时没返回这个字段
                it.resultType?.let { resultType ->
                    parseResultType(resultType)
                }
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

    fun setViewResult(viewId: String, result: String) {
        val childCount = linearLayout.childCount
        for (i in 0 until childCount) {
            try {
                val itemViewInterceptor: View = linearLayout.getChildAt(i)
                if (itemViewInterceptor is ItemViewInterceptor) {
                    if (itemViewInterceptor.getViewId() == viewId) {
                        return itemViewInterceptor.setResult(result)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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