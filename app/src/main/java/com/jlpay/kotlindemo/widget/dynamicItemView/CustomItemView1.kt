package com.jlpay.kotlindemo.widget.dynamicItemView

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.bean.DynamicLayout
import com.jlpay.kotlindemo.databinding.ItemLayoutFromGson1Binding

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
        //使用DataBinding实现双向绑定
        val dataBinding = DataBindingUtil.inflate<ItemLayoutFromGson1Binding>(
            LayoutInflater.from(context),
            R.layout.item_layout_from_gson1,
            this,
            true
        )
//        dataBinding.lifecycleOwner = //TODO 要加上
        dataBinding.textview.text = dynamicLayout.layoutLeft
        dataBinding.edittext.inputType = InputType.TYPE_CLASS_PHONE
        dataBinding.edittext.hint = dynamicLayout.layoutRightHint
        dataBinding.edittext.setText(dynamicLayout.layoutRight)


//        val view: View =
//            LayoutInflater.from(context).inflate(R.layout.item_layout_from_gson1, this, true)
//        val textview = view.findViewById<TextView>(R.id.textview)
//        textview.text = dynamicLayout.layoutLeft
//        edittext = view.findViewById<EditText>(R.id.edittext)
//        edittext.hint = dynamicLayout.layoutRightHint
//        edittext.inputType = InputType.TYPE_CLASS_PHONE
//        edittext.setText("9999")//模拟回显
    }

    override fun getResult(): String {
        return edittext.text.toString()
    }

    override fun setResult(result: String) {
        edittext.setText(result)
    }

    //    override fun getResultAdvance(): String {
//        return "111"
//    }

    override fun getViewId() = mViewId

    override fun setViewId(viewId: String) {
        mViewId = viewId
    }
}