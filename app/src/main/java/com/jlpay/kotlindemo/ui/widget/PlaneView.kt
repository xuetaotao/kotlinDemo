package com.jlpay.kotlindemo.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.jlpay.kotlindemo.R
import java.util.*

/**
 * 控制飞机移动
 */
class PlaneView(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
    View(context, attributeSet, defStyleAttr) {

    private var index: Int = 0
    var currentX: Float = 0f
    var currentY: Float = 0f
    private var plane0: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.add_one)
    private var plane1: Bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.add_two)

    private var paint: Paint = Paint()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    init {
        //启动定时器来切换飞机图片，实现动画效果
        Timer().schedule(object : TimerTask() {
            override fun run() {
                index++
                this@PlaneView.invalidate()//通知当前组件重绘自己
            }
        }, 0L, 100L)
        isFocusable = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制飞机
        val plane: Bitmap = if (index % 2 == 0) {
            plane0
        } else {
            plane1
        }
        canvas.drawBitmap(plane, currentX, currentY, paint)
    }
}