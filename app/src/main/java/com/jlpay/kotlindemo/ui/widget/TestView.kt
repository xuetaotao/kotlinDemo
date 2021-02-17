package com.jlpay.kotlindemo.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.jlpay.kotlindemo.ui.utils.Utils

val RADIUS = 200f

class TestView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint: Paint = Paint();

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(100f, 100f, 200f, 200f, paint)
//        canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)
        canvas.drawCircle(width / 2f, height / 2f, Utils.dp2px(RADIUS, context), paint)

    }
}