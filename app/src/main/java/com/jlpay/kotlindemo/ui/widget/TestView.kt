package com.jlpay.kotlindemo.ui.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.jlpay.kotlindemo.ui.utils.px

//draw的时候单位必须是px
val RADIUS = 200f.px

class TestView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint: Paint = Paint()

    //    private val paint2: Paint = Paint(Paint.ANTI_ALIAS_FLAG)//开启抗锯齿
    private val path: Path = Path()

    //Rect的参数为int类型，而RectF的参数类型为float类型，故精度更高
    private val rect: Rect = Rect(300, 300, 800, 800)
    private val rectF: RectF = RectF(300f, 300f, 800f, 800f)
    private val rectF2: RectF = RectF(400f, 50f, 700f, 200f)

    /**
     * 画笔初始化，存放公有信息
     */
    fun paintInit() {
        paint.style = Paint.Style.FILL//设置绘制模式，填充模式，默认模式
//        paint.style = Paint.Style.STROKE//设置绘制模式，画线模式（即勾边模式）
//        paint.style = Paint.Style.FILL_AND_STROKE////设置绘制模式，两种模式一并使用：既画线又填充
        paint.color = 0x3389ff//设置颜色
        paint.strokeWidth = 100f//设置线条宽度
        paint.textSize = 50f//设置文字大小
        paint.isAntiAlias = true//设置抗锯齿开关，默认关闭
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
//        Path.Direction.CW：clockwise，顺时针
//        Path.Direction.CCW：counter-clockwise，逆时针
        path.addCircle(width / 2f, height / 2f, 180f.px, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        //绘制代码
        super.onDraw(canvas)

        //在整个绘制区域统一涂上指定的颜色，一般用于绘制之前设置底色，或者在绘制之后为界面设置半透明模板
//        canvas.drawColor(Color.parseColor("#03DAC5"))
//        canvas.drawRGB(100, 200, 100)
        canvas.drawARGB(100, 100, 200, 100)

        //画点
//        paint.strokeWidth = 20f
//        paint.strokeCap = Paint.Cap.ROUND//设置点的形状。ROUND：圆头；BUTT：平头；SQUARE：方头
//        canvas.drawPoint(width / 2f, height / 2f, paint)
        //批量画点
//        val floatArray =
//            floatArrayOf(0f, 0f, 50f, 50f, 50f, 100f, 100f, 50f, 100f, 100f, 150f, 50f, 150f, 100f)
//        canvas.drawPoints(floatArray, paint)
        //floatArray 这个数组是点的坐标，每两个成一对；offset 表示跳过数组的前几个数再开始记坐标；count 表示一共要绘制几个点
//        canvas.drawPoints(floatArray, 2/* 跳过两个数，即前两个 0 */, 8 /* 一共绘制 8 个数（4 个点）*/, paint)

        //画线
//        canvas.drawLine(100f, 100f, 200f, 200f, paint)
        //批量画线
//        val floatArray2 =
//            floatArrayOf(20f, 20f, 120f, 20f, 70f, 20f, 70f, 120f, 20f, 120f, 120f, 120f)
//        canvas.drawLines(floatArray2, paint)

        //画圆，参数：圆心横坐标，圆心纵坐标，半径(单位都是像素，px)，画笔
//        canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)
//        canvas.drawCircle(width / 2f, height / 2f, Utils.dp2px(RADIUS, context), paint)
//        canvas.drawCircle(width / 2f, height / 2f, RADIUS.dp2px(), paint)
//        canvas.drawCircle(width / 2f, height / 2f, 100f.px, paint)
//        canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)

        //画椭圆
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            canvas.drawOval(50f, 50f, 350f, 200f, paint)
//        }
//        paint.style = Paint.Style.STROKE
//        canvas.drawOval(rectF2, paint)

        //画矩形
//        canvas.drawRect(300f, 300f, 800f, 800f, paint)
//        paint.style = Paint.Style.STROKE
//        canvas.drawRect(300f, 300f, 800f, 800f, paint)
        //不要在view绘制和做布局操作的时候实例化数据,即不要在自定义View的onMeasure、onLayout、onDraw等方法里面做new对象的操作
        //因为实例化对象是会耗性能的，而这几个方法会被多次调用，所以需要将对象作为属性，在初始化的时候就实例化好对象，在这些方法里面直接用就行了
//        val rect: Rect = Rect(300, 300, 800, 800)
//        canvas.drawRect(rect, paint)
//        canvas.drawRect(rectF, paint)

        //画圆角矩形
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //left, top, right, bottom 是四条边的坐标，rx 和 ry 是圆角的横向半径和纵向半径
//            canvas.drawRoundRect(100f, 100f, 500f, 300f, 50f, 50f, paint)
//        }
//        canvas.drawRoundRect(rectF, 50f, 50f, paint)
//        canvas.drawRect(300f, 900f, 800f, 1400f, paint)//对比

        //绘制弧形或扇形
        //drawArc() 是使用一个椭圆来描述弧形的,left, top, right, bottom 描述的是这个弧形所在的椭圆;
        //startAngle 是弧形的起始角度（x 轴的正向，即正右的方向，是 0 度的位置；顺时针为正角度，逆时针为负角度），sweepAngle 是弧形划过的角度；
        //useCenter 表示是否连接到圆心，如果不连接到圆心，就是弧形，如果连接到圆心，就是扇形
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            paint.style = Paint.Style.FILL
//            //绘制扇形
//            canvas.drawArc(200f, 100f, 800f, 500f, -110f, 100f, true, paint)
//            //绘制弧形
//            canvas.drawArc(200f, 100f, 800f, 500f, 20f, 140f, false, paint)
//            paint.style = Paint.Style.STROKE
//            //绘制不封口的弧形
//            canvas.drawArc(200f, 100f, 800f, 500f, 180f, 60f, false, paint)
//        }

        ////////////////////////drawPath画自定义图形///////////////////////////////////////////////////
        //用path画圆
        paint.color = Color.BLUE//设置绘制内容的颜色
        paint.style = Paint.Style.STROKE//设置绘制模式，画线模式（即勾边模式），画空心圆
        paint.strokeWidth = 20f//设置线条宽度为20像素
        paint.isAntiAlias = true//开启抗锯齿
        canvas.drawPath(path, paint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }
}