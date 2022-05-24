package com.jlpay.kotlindemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PathEffect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.jlpay.kotlindemo.utils.px
import kotlin.math.cos
import kotlin.math.sin

//绘制的时候单位必须是px，这里把 float型的200dp转为对应的px值
private val RADIUS3 = 150f.px
private val ANGLES = floatArrayOf(60f, 90f, 150f, 60f)
private val COLORS = listOf(
    Color.parseColor("#c2185b"), Color.parseColor("#00acc1"),
    Color.parseColor("#558b2f"), Color.parseColor("#5d4037")
)
private val OFFSET_LENGTH = 20f.px
private val OFFSET_WHICH = 1

/**
 * 自定义View--->饼图
 */
class PieView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint().apply {
        strokeWidth = 3f.px
    }

    private lateinit var pathEffect: PathEffect

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var startAngle = 0f
        canvas.drawARGB(100, 100, 200, 100)
        //画扇形
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for ((index, angle) in ANGLES.withIndex()) {
                paint.color = COLORS[index]
                if (index == OFFSET_WHICH) {
                    canvas.save()
                    canvas.translate(
                        OFFSET_LENGTH * cos(Math.toRadians(startAngle + angle / 2f.toDouble())).toFloat(),
                        OFFSET_LENGTH * sin(Math.toRadians(startAngle + angle / 2f.toDouble())).toFloat()
                    )
                }
                canvas.drawArc(
                    width / 2f - RADIUS3,
                    height / 2f - RADIUS3,
                    width / 2f + RADIUS3,
                    height / 2f + RADIUS3,
                    startAngle,
                    angle, true, paint
                )
                startAngle += angle
                if (index == OFFSET_WHICH) {
                    canvas.restore()
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}