package com.jlpay.kotlindemo.ui.widget.dynamicItemView

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.bean.DynamicLayout

class CustomItemView1 : LinearLayout, ItemViewInterceptor {

    private var dynamicLayout: DynamicLayout
    private lateinit var edittext: EditText
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
            LayoutInflater.from(context).inflate(R.layout.item_layout_from_gson1, this, true)
        val textview = view.findViewById<TextView>(R.id.textview)
        textview.text = dynamicLayout.layoutLeft
        edittext = view.findViewById<EditText>(R.id.edittext)
        edittext.hint = dynamicLayout.layoutRightHint
        edittext.inputType = InputType.TYPE_CLASS_PHONE
    }

    override fun getResult(): String {
        return edittext.text.toString()
    }

//    override fun getResultAdvance(): String {
//        return "111"
//    }

    override fun getViewId() = mViewId

    override fun setViewId(viewId: String) {
        mViewId = viewId
    }
}