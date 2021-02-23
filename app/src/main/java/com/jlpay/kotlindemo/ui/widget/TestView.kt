package com.jlpay.kotlindemo.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.utils.px

//draw的时候单位必须是px
val RADIUS = 200f.px

class TestView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null) {

    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {

    }

    private val paint: Paint = Paint().apply {
        strokeWidth = 10f
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    //    private val paint2: Paint = Paint(Paint.ANTI_ALIAS_FLAG)//开启抗锯齿
    private val path: Path = Path()

    //Rect的参数为int类型，而RectF的参数类型为float类型，故精度更高
    private val rect: Rect = Rect(300, 300, 800, 800)
    private val rectF: RectF = RectF(300f, 300f, 800f, 800f)
    private val rectF2: RectF = RectF(400f, 50f, 700f, 200f)

    private val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.cashout)

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
//        path.reset()
//        Path.Direction.CW：clockwise，顺时针
//        Path.Direction.CCW：counter-clockwise，逆时针
//        path.addCircle(width / 2f, height / 2f, 180f.px, Path.Direction.CW)
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


        /**
         * drawPath()画自定义图形，一般是在绘制组合图形时才会用到的
         * Path 可以描述直线、二次曲线、三次曲线、圆、椭圆、弧形、矩形、圆角矩形。把这些图形结合起来，就可以描述出很多复杂的图形
         * Path 有两类方法，一类是直接描述路径的，另一类是辅助的设置或计算
         */
        //用path画圆
//        paint.color = Color.BLUE//设置绘制内容的颜色
//        paint.style = Paint.Style.STROKE//设置绘制模式，画线模式（即勾边模式），画空心圆
//        paint.strokeWidth = 20f//设置线条宽度为20像素
//        paint.isAntiAlias = true//开启抗锯齿

        //画个心形，不会画
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            path.addArc(200f, 200f, 400f, 400f, -225f, 225f)
//            path.arcTo(400f, 200f, 600f, 400f, -180f, 225f, false)
//        }
//        path.lineTo(400f, 542f)
//        canvas.drawPath(path, paint)

        /**
         * Path方法第一类：直接描述路径；细分为两组：添加子图形和画线（直线或曲线）
         */

        //第一组：addXxx()-----添加子图形
        //x, y, radius 这三个参数是圆的基本信息，最后一个参数 dir 是画圆的路径的方向
        //其他的如：addOval(),addRect(),addRoundRect(),addPath()用法相似
//        path.addCircle(width / 2f, height / 2f, 200f, Path.Direction.CW)
//        canvas.drawPath(path, paint)

        //第二组：xxxTo()-----画线（直线或者曲线）
        //这一组和第一组addXxx()方法的区别在于，第一组是添加的完整封闭图形（除了addPath()），而这一组添加的只是一条线
        //lineTo(float x, float y) / rLineTo(float x, float y) 画直线
        //从当前位置向目标位置画一条直线， x 和 y 是目标位置的坐标。这两个方法的区别是，lineTo(x, y) 的参数是绝对坐标，而 rLineTo(x, y) 的参数是相对当前位置的相对坐标 （前缀 r 指的就是 relatively 「相对地」)。
        //当前位置：所谓当前位置，即最后一次调用画 Path 的方法的终点位置。初始值为原点 (0, 0)
//        paint.style = Paint.Style.STROKE
//        path.lineTo(100f, 100f)// 由当前位置 (0, 0) 向 (100, 100) 画一条直线
//        path.rLineTo(100f, 0f)// 由当前位置 (100, 100) 向正右方 100 像素的位置画一条直线
//        canvas.drawPath(path, paint)

        //quadTo(float x1, float y1, float x2, float y2) / rQuadTo(float dx1, float dy1, float dx2, float dy2) 画二次贝塞尔曲线
        //这条二次贝塞尔曲线的起点就是当前位置，而参数中的 x1, y1 和 x2, y2 则分别是控制点和终点的坐标。和 rLineTo(x, y) 同理，rQuadTo(dx1, dy1, dx2, dy2) 的参数也是相对坐标
//        path.quadTo(513f, 23f, 650f, 360f)
//        canvas.drawPath(path, paint)//不了解这个贝塞尔曲线，随便看看
        //cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) / rCubicTo(float x1, float y1, float x2, float y2, float x3, float y3) 画三次贝塞尔曲线
//        path.cubicTo(513f, 23f, 650f, 360f, 400f, 589f)
//        canvas.drawPath(path, paint)

        //不论是直线还是贝塞尔曲线，都是以当前位置作为起点，而不能指定起点。但你可以通过 moveTo(x, y) 或 rMoveTo() 来改变当前位置，从而间接地设置这些方法的起点。
        //moveTo(x, y) 虽然不添加图形，但它会设置图形的起点，所以它是非常重要的一个辅助方法
//        paint.style = Paint.Style.STROKE
//        path.lineTo(100f, 100f)// 画斜线
//        path.moveTo(200f, 100f)// 我移~~，这里预览界面会多一条线，实际上没有的
//        path.lineTo(200f, 0f)// 画竖线
//        canvas.drawPath(path, paint)

        //两个特殊的方法：arcTo() 和 addArc()，也是用来画线的，但并不使用当前位置作为弧线的起点
        //这个方法和 Canvas.drawArc() 比起来，少了一个参数 useCenter，而多了一个参数 forceMoveTo
        //少了 useCenter ，是因为 arcTo() 只用来画弧形而不画扇形，所以不再需要 useCenter 参数；
        // 而多出来的这个 forceMoveTo 参数的意思是，绘制是要「抬一下笔移动过去」，还是「直接拖着笔过去」，区别在于是否留下移动的痕迹。
//        paint.style = Paint.Style.STROKE
//        path.lineTo(100f, 100f)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            path.arcTo(100f, 100f, 300f, 300f, -90f, 90f, true)// 强制移动到弧形起点（无痕迹）
////            path.arcTo(100f,100f,300f,300f,-90f,90f, false)// 直接连线连到弧形起点（有痕迹）
//        }
//        canvas.drawPath(path, paint)

        //addArc() 只是一个直接使用了 forceMoveTo = true 的简化版 arcTo()
//        paint.style = Paint.Style.STROKE
//        path.lineTo(100f, 100f)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            path.addArc(100f, 100f, 300f, 300f, -90f, 90f)
//        }
//        canvas.drawPath(path, paint)

        //close() 封闭当前子图形，它的作用是把当前的子图形封闭，即由当前位置向当前子图形的起点绘制一条直线
        //close() 和 lineTo(起点坐标) 是完全等价的
//        paint.style = Paint.Style.STROKE
//        path.moveTo(100f, 100f)
//        path.lineTo(200f, 100f)
//        path.lineTo(150f, 150f)//子图形未封闭
//        path.close()//使用 close() 封闭子图形。等价于path.lineTo(100f, 100f)
//        canvas.drawPath(path, paint)

        //「子图形」：官方文档里叫做 contour 。前面说到，第一组方法是「添加子图形」，所谓「子图形」，指的就是一次不间断的连线。一个 Path 可以包含多个子图形。
        // 当使用第一组方法，即 addCircle() addRect() 等方法的时候，每一次方法调用都是新增了一个独立的子图形；
        // 而如果使用第二组方法，即 lineTo() arcTo() 等方法的时候，则是每一次断线（即每一次「抬笔」），都标志着一个子图形的结束，以及一个新的子图形的开始
        //另外，不是所有的子图形都需要使用 close() 来封闭。当需要填充图形时（即 Paint.Style 为 FILL 或 FILL_AND_STROKE），Path 会自动封闭子图形
//        paint.style = Paint.Style.FILL
//        path.moveTo(100f, 100f)
//        path.lineTo(200f, 100f)
//        path.lineTo(150f, 150f)
//        canvas.drawPath(path, paint)
        // 这里只绘制了两条边，但由于 Style 是 FILL ，所以绘制时会自动封口


        /**
         * Path方法第二类：辅助的设置或计算。使用场景比较少
         */
        //Path.setFillType(Path.FillType ft) 设置图形自相交时的填充方式
        //Path.FillType.WINDING：全填充
        //Path.FillType.EVEN_ODD：交叉填充
        //Path.FillType.INVERSE_WINDING：反色全填充
        //Path.FillType.INVERSE_EVEN_ODD：反色交叉填充
        //简单写个测试例子，原理较为复杂，暂不做深入研究
//        paint.style = Paint.Style.FILL
//        path.fillType = Path.FillType.EVEN_ODD
//        path.addCircle(200f, 200f, 100f, Path.Direction.CW)
//        path.addCircle(300f, 300f, 150f, Path.Direction.CW)
//        canvas.drawPath(path, paint)

        //绘制 Bitmap 对象，也就是把这个 Bitmap 中的像素内容贴过来。其中 left 和 top 是要把 bitmap 绘制到的位置坐标
//        canvas.drawBitmap(bitmap, 200f, 100f, paint)

        //绘制文字,drawText(String text, float x, float y, Paint paint)
        //界面里所有的显示内容，都是绘制出来的，包括文字。 drawText() 这个方法就是用来绘制文字的。参数 text 是用来绘制的字符串，x 和 y 是绘制的起点坐标
        paint.textSize = 60f//设置文字的大小
        canvas.drawText("恭喜发财", width / 2f - 100f, height / 2f, paint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }
}