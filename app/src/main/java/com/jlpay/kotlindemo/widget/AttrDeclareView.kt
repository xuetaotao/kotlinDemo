package com.jlpay.kotlindemo.widget


import android.content.Context

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import com.jlpay.kotlindemo.R


class AttrDeclareView(
    context: Context,
    @Nullable attrs: AttributeSet?,
    defStyleAttr: Int
) :
    View(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {}

    init {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.AttrDeclareView)
        val color = a.getColor(R.styleable.AttrDeclareView_background_color, 0)
        a.recycle()
        if (color != 0) {
            setBackgroundColor(color)
        }
    }
}