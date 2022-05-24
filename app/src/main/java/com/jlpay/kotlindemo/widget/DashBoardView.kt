package com.jlpay.kotlindemo.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.utils.px
import kotlin.math.cos
import kotlin.math.sin

//绘制的时候单位必须是px，这里把 float型的200dp转为对应的px值
private const val OPEN_ANGLE = 120f
private const val MARK = 8
private val RADIUS3 = 150f.px
private val LENGTH = 120f.px
private val DASH_WIDTH = 2f.px
private val DASH_LENGTH = 10f.px

/**
 * 绘制仪表盘
 */
class DashBoardView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f.px
    }
    private val path = Path()
    private val dash = Path().apply {
        addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)
    }
    private lateinit var pathEffect: PathEffect

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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            path.addArc(
                width / 2f - RADIUS3,
                height / 2f - RADIUS3,
                width / 2f + RADIUS3,
                height / 2f + RADIUS3,
                90 + OPEN_ANGLE / 2f,
                360 - OPEN_ANGLE
            )
        }
        val pathMeasure: PathMeasure = PathMeasure(path, false)//做测量，算刻度间距
        pathEffect = PathDashPathEffect(
            dash,
            (pathMeasure.length - DASH_WIDTH) / 20f,
            0f,
            PathDashPathEffect.Style.ROTATE
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawARGB(100, 100, 200, 100)
        //画弧
        canvas.drawPath(path, paint)
        //画刻度
        paint.pathEffect = pathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null
        //画标线
        paint.color = Color.BLUE
        canvas.drawLine(
            width / 2f,
            height / 2f,
            width / 2f + LENGTH * cos(markToRadians(MARK)).toFloat(),
            height / 2f + LENGTH * sin(markToRadians(MARK)).toFloat(), paint
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * 角度转弧度
     */
    private fun markToRadians(mark: Int): Double {
        return Math.toRadians((90 + OPEN_ANGLE / 2f + (360 - OPEN_ANGLE) / 20f * mark).toDouble())
    }
}