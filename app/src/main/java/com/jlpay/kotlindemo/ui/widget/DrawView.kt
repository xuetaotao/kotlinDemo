package com.jlpay.kotlindemo.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.jlpay.kotlindemo.ui.utils.px

/**
 * 跟随手指的小球
 */
class DrawView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {

    //当开发自定义View时，根据业务重写部分方法即可
    //如果要新建的自定义组件只是组合现有组件，不需要重写绘制所有组件内容，那就只要实现自定义组件的构造器
    //在自定义构造器中使用 LayoutInflate 加载布局文件即可

    private var currentX: Float = 200f
    private var currentY: Float = 200f
    private val paint: Paint = Paint()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.BLUE
        canvas.drawCircle(currentX, currentY, 30f.px, paint)
    }


    //为该组件的触碰事件重写事件处理方法
    override fun onTouchEvent(event: MotionEvent): Boolean {
        currentX = event.x
        currentY = event.y
        //通知当前组件重绘自己
        invalidate()
        Log.e("eventDeliver：", "DrawView： onTouchEvent")
        //返回true表明该处理方法已经处理该事件，不会向外传播
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(event)
    }


}