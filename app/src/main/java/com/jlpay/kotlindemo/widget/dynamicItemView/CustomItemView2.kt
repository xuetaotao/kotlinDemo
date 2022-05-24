package com.jlpay.kotlindemo.widget.dynamicItemView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.bean.DynamicLayout

class CustomItemView2 : LinearLayout, ItemViewInterceptor {

    private var dynamicLayout: DynamicLayout
    private lateinit var tvOption: TextView
    private lateinit var mViewId: String

    constructor(mContext: Context, dynamicLayout: DynamicLayout) : this(
        mContext,
        dynamicLayout,
        null
    )

    constructor(
        mContext: Context,
        dynamicLayout: DynamicLayout,
        attributeSet: AttributeSet?
    ) : this(mContext, dynamicLayout, attributeSet, 0)

    constructor(
        mContext: Context,
        dynamicLayout: DynamicLayout,
        attributeSet: AttributeSet?,
        defStyleAttr: Int
    ) : super(mContext, attributeSet, defStyleAttr) {
        this.dynamicLayout = dynamicLayout
        setViewId(dynamicLayout.name)
        inflateView(dynamicLayout)
    }


    override fun inflateView(dynamicLayout: DynamicLayout) {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_layout_from_gson3, this, true)
        val textview = view.findViewById<TextView>(R.id.textview)
        textview.text = dynamicLayout.layoutLeft
        tvOption = view.findViewById<TextView>(R.id.tv_option)
        tvOption.text = dynamicLayout.layoutRightHint
        tvOption.setOnClickListener {
            Toast.makeText(context, "你点击了：${tvOption.text}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getResult(): String {
        return tvOption.text.toString()
    }

    override fun setResult(result: String) {
        tvOption.text = result
    }

    //    override fun getResultAdvance(): String {
//        return "222"
//    }

    override fun getViewId() = mViewId

    override fun setViewId(viewId: String) {
        mViewId = viewId
    }
}