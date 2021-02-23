package com.jlpay.kotlindemo.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.jlpay.kotlindemo.R

class PracticeDrawView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PracticeDrawView)
        val color = typedArray.getColor(R.styleable.PracticeDrawView_background_color, Color.WHITE)
        val dimensionPixelOffset = typedArray.getDimensionPixelOffset(
            R.styleable.PracticeDrawView_practice_view_text_size,
            14
        )//这里获取的值，单位是 sp， 调用时候：setTextSize(TypedValue.COMPLEX_UNIT_PX, indicatorTextSize);
        typedArray.recycle()
    }
}