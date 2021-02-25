package com.jlpay.kotlindemo.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.utils.px

//绘制的时候单位必须是px，这里把 float型的200dp转为对应的px值
val RADIUS2 = 200f.px
const val OPEN_ANGLE = 120f

/**
 * 绘制仪表盘
 */
class DashBoardView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f.px
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.DashBoardView)
        val color = typedArray.getColor(R.styleable.DashBoardView_background_color, Color.WHITE)
        val dimensionPixelSize = typedArray.getDimensionPixelSize(
            R.styleable.DashBoardView_dash_width,
            14
        )//这里获取的值，单位是 sp， 调用时候：setTextSize(TypedValue.COMPLEX_UNIT_PX, indicatorTextSize);
        typedArray.recycle()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawARGB(100, 100, 200, 100)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(
                width / 2f - 150f.px,
                height / 2f - 150f.px,
                width / 2f + 150f.px,
                height / 2f + 150f.px,
                90 + OPEN_ANGLE / 2f,
                360 - OPEN_ANGLE, false,
                paint
            )
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}