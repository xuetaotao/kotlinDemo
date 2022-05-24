package com.jlpay.kotlindemo.widget

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.utils.px

//绘制的时候单位必须是px，这里把 float型的200dp转为对应的px值
private val RADIUS = 200f.px

/**
 * canvas 和 Paint 基本API的实例学习
 */
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
    private val pathCircle = Path().apply {
        addCircle(100f, 100f, 10f, Path.Direction.CW)
    }

    //Rect的参数为int类型，而RectF的参数类型为float类型，故精度更高
    private val rect: Rect = Rect(300, 300, 800, 800)
    private val rectF: RectF = RectF(300f, 300f, 800f, 800f)
    private val rectF2: RectF = RectF(400f, 50f, 700f, 200f)

    private val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.zhizhuxia)
    private val bitmapCashout = BitmapFactory.decodeResource(context.resources, R.mipmap.cashout)

    //LinearGradient 线性渐变
    //x0 y0 x1 y1：渐变的两个端点的位置
    //color0 color1 是端点的颜色
    //tile：端点范围之外的着色规则，类型是 TileMode。TileMode 一共有 3 个值可选： CLAMP, MIRROR 和 REPEAT。CLAMP会在端点之外延续端点处的颜色；MIRROR 是镜像模式；REPEAT 是重复模式。具体的看一下例子就明白
//    private val shader: LinearGradient = LinearGradient(
//        100f, 100f, 500f, 500f, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"),
//        Shader.TileMode.CLAMP
//    )

    //RadialGradient 辐射渐变
    //centerX centerY：辐射中心的坐标
    //radius：辐射半径
    //centerColor：辐射中心的颜色
    //edgeColor：辐射边缘的颜色
    //tileMode：辐射范围之外的着色模式
//    private val shader: RadialGradient = RadialGradient(
//        300f,
//        300f,
//        200f,
//        Color.parseColor("#E91E63"),
//        Color.parseColor("#2196F3"),
//        Shader.TileMode.CLAMP
//    )

    //SweepGradient 扫描渐变
    //SweepGradient(float cx, float cy, int color0, int color1)
    //cx cy ：扫描的中心
    //color0：扫描的起始颜色
    //color1：扫描的终止颜色
//    private val shader: SweepGradient =
//        SweepGradient(300f, 300f, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"))

    //BitmapShader：用 Bitmap 来着色（终于不是渐变了）。其实也就是用 Bitmap 的像素来作为图形或文字的填充
    //看着跟 Canvas.drawBitmap() 好像啊？事实上也是一样的效果。如果你想绘制圆形的 Bitmap，就别用 drawBitmap() 了，改用 drawCircle() + BitmapShader 就可以了（其他形状同理）
    //bitmap：用来做模板的 Bitmap 对象
    //tileX：横向的 TileMode
    //tileY：纵向的 TileMode
//    private val shader: BitmapShader =
//        BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

    //ComposeShader 混合着色器，所谓混合，就是把两个 Shader 一起使用
    //ComposeShader() 在硬件加速下是不支持两个相同类型的 Shader 的，所以用的话需要关闭硬件加速才能看到效果
    //shaderA, shaderB：两个相继使用的 Shader
    //mode: 两个 Shader 的叠加模式，即 shaderA 和 shaderB 应该怎样共同绘制。它的类型是 PorterDuff.Mode(这里不做具体介绍了)
    private val shader1: BitmapShader =
        BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    private val shader2: LinearGradient = LinearGradient(
        100f,
        100f,
        500f,
        500f,
        Color.parseColor("#E91E63"),
        Color.parseColor("#2196F3"),
        Shader.TileMode.CLAMP
    )
    private val shader: ComposeShader = ComposeShader(shader1, shader2, PorterDuff.Mode.DST_IN)

    //去掉红色
    //R' = R * 0x0 / 0xff + 0x0 = 0 // 红色被移除
    //G' = G * 0xff / 0xff + 0x0 = G
    //B' = B * 0xff / 0xff + 0x0 = B
    private val lightingColorFilter: LightingColorFilter = LightingColorFilter(0x00ffff, 0x000000)

    private val porterDuffColorFilter: PorterDuffColorFilter =
        PorterDuffColorFilter(Color.parseColor("#E91E63"), PorterDuff.Mode.DST_IN)

    private val cornerPathEffect: CornerPathEffect = CornerPathEffect(20f)

    //segmentLength 是用来拼接的每个线段的长度， deviation 是偏离量
    private val discretePathEffect: DiscretePathEffect = DiscretePathEffect(20f, 5f)

    //第一个参数 intervals 是一个数组，它指定了虚线的格式：数组中元素必须为偶数（最少是 2 个），按照「画线长度、空白长度、画线长度、空白长度」……的顺序排列，例如上面代码中的 20, 5, 10, 5 就表示虚线是按照「画 20 像素、空 5 像素、画 10 像素、空 5 像素」的模式来绘制；第二个参数 phase 是虚线的偏移量
    private val dashPathEffect: DashPathEffect = DashPathEffect(floatArrayOf(20f, 10f, 5f, 10f), 0f)

    //shape 参数是用来绘制的 Path ； advance 是两个相邻的 shape 段之间的间隔，不过注意，这个间隔是两个 shape 段的起点的间隔，而不是前一个的终点和后一个的起点的距离； phase 和 DashPathEffect 中一样，是虚线的偏移；最后一个参数 style，是用来指定拐弯改变的时候 shape 的转换方式。style 的类型为 PathDashPathEffect.Style ，是一个 enum
    //具体有三个值： TRANSLATE：位移 ROTATE：旋转 MORPH：变体
    private val pathDashPathEffect: PathDashPathEffect =
        PathDashPathEffect(pathCircle, 40f, 0f, PathDashPathEffect.Style.TRANSLATE)

    private val sumPathEffect: SumPathEffect = SumPathEffect(dashPathEffect, discretePathEffect)

    //构造方法 ComposePathEffect(PathEffect outerpe, PathEffect innerpe) 中的两个 PathEffect 参数， innerpe 是先应用的， outerpe 是后应用的。所以上面的代码就是「先偏离，再变虚线」。而如果把两个参数调换，就成了「先变虚线，再偏离」
    private val composePathEffect: ComposePathEffect =
        ComposePathEffect(dashPathEffect, discretePathEffect)

    //radius 参数是模糊的范围， style 是模糊的类型。一共有四种：NORMAL: 内外都模糊绘制； SOLID: 内部正常绘制，外部模糊；INNER: 内部模糊，外部不绘制; OUTER: 内部不绘制，外部模糊
    private val blurMaskFilter: BlurMaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.NORMAL)

    //参数里， direction 是一个 3 个元素的数组，指定了光源的方向； ambient 是环境光的强度，数值范围是 0 到 1； specular 是炫光的系数； blurRadius 是应用光线的范围
    private val embossMaskFilter: EmbossMaskFilter =
        EmbossMaskFilter(floatArrayOf(0f, 1f, 1f), 0.2f, 8f, 10f)

    private val textPaint: TextPaint = TextPaint(paint.apply { textSize = 30f })

    //参数里：width 是文字区域的宽度，文字到达这个宽度后就会自动换行；align 是文字的对齐方向；
    //spacingmult 是行间距的倍数，通常情况下填 1 就好； spacingadd 是行间距的额外增加值，通常情况下填 0 就好；
    //includepad 是指是否在文字上下添加额外的空间，来避免某些过高的字符的绘制出现越界
    private val staticLayout: StaticLayout = StaticLayout(
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
        textPaint, 600, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true
    )

    private val staticLayout2: StaticLayout = StaticLayout(
        "a\nbc\ndefghi\njklm\nnopqrst\nuvwx\nyz",
        textPaint, 600, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true
    )

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
        //Path.FillType.EVEN_ODD：交叉填充，镂空，与方向(Path.Direction.CW)无关
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
//        paint.textSize = 60f//设置文字的大小
//        canvas.drawText("恭喜发财", width / 2f - 100f, height / 2f, paint)


        /**
         * Paint详解
         */
        //1.颜色
        //1.1直接设置颜色：setColor(int color)
//        paint.color = Color.parseColor("#009688")
//        canvas.drawRect(30f, 30f, 230f, 180f, paint)
//        paint.color = Color.parseColor("#FF9800")
//        canvas.drawLine(300f, 30f, 450f, 180f, paint)
//        paint.color = Color.parseColor("#E91E63")
//        paint.textSize = 60f
//        canvas.drawText("恭喜发财", 500f, 130f, paint)
        //setARGB(int a, int r, int g, int b)，和上面其实一样，只是它的参数用的是更直接的三原色与透明度的值
//        paint.setARGB(100, 255, 0, 0)
//        canvas.drawRect(0f, 0f, 200f, 200f, paint)
//        paint.setARGB(100, 0, 0, 0)
//        canvas.drawLine(0f, 0f, 200f, 200f, paint)

        //setShader(Shader shader) 设置 Shader(着色器)
        //它和直接设置颜色的区别是，着色器设置的是一个颜色方案，或者说是一套着色规则。当设置了 Shader 之后，Paint 在绘制图形和文字时就不使用 setColor/ARGB() 设置的颜色了，而是使用 Shader 的方案中的颜色
        //在 Android 的绘制里使用 Shader ，并不直接用 Shader 这个类，而是用它的几个子类。具体来讲有 LinearGradient RadialGradient SweepGradient BitmapShader ComposeShader 这么几个
        //LinearGradient 线性渐变，设置两个点和两种颜色，以这两个点作为端点，使用两种颜色的渐变来绘制颜色
        //在设置了 Shader 的情况下， Paint.setColor/ARGB() 所设置的颜色就不再起作用
//        paint.shader = shader
//        canvas.drawLine(100f, 100f, 500f, 500f, paint)
//        canvas.drawCircle(300f, 300f, 200f, paint)

        //1.2 setColorFilter(ColorFilter colorFilter)
        //ColorFilter 这个类，为绘制设置颜色过滤。颜色过滤的意思，就是为绘制的内容设置一个统一的过滤策略，然后 Canvas.drawXXX() 方法会对每个像素都进行过滤后再绘制出来。现实中比如：有色玻璃透视
        //ColorFilter 并不直接使用，而是使用它的子类。它共有三个子类：LightingColorFilter PorterDuffColorFilter 和 ColorMatrixColorFilter
        //LightingColorFilter 的构造方法是 LightingColorFilter(int mul, int add) ，参数里的 mul 和 add 都是和颜色值格式相同的 int 值，其中 mul 用来和目标像素相乘，add 用来和目标像素相加：
        //R' = R * mul.R / 0xff + add.R
        //G' = G * mul.G / 0xff + add.G
        //B' = B * mul.B / 0xff + add.B
//        paint.colorFilter = lightingColorFilter
//        canvas.drawBitmap(bitmap, 200f, 100f, paint)

        //PorterDuffColorFilter 的作用是使用一个指定的颜色和一种指定的 PorterDuff.Mode 来与绘制对象进行合成
        //构造方法是 PorterDuffColorFilter(int color, PorterDuff.Mode mode) 其中的 color 参数是指定的颜色， mode 参数是指定的 Mode。同样也是 PorterDuff.Mode ，不过和 ComposeShader 不同的是，PorterDuffColorFilter 作为一个 ColorFilter，只能指定一种颜色作为源，而不是一个 Bitmap
//        paint.colorFilter = porterDuffColorFilter
//        canvas.drawCircle(300f, 300f, 200f, paint)

        //ColorMatrixColorFilter 使用一个 ColorMatrix 来对颜色进行处理。 ColorMatrix 这个类，内部是一个 4x5 的矩阵, ColorMatrix 可以把要绘制的像素进行转换，这里不做介绍

        //1.3 setXfermode(Xfermode xfermode)
        //用来处理源图像和 View 已有内容的关系，这部分暂时就不做介绍了
        //Xfermode 指的是你要绘制的内容和 Canvas 的目标位置的内容应该怎样结合计算出最终的颜色。但通俗地说，其实就是要你以绘制的内容作为源图像，以 View 中已有的内容作为目标图像，选取一个 PorterDuff.Mode 作为绘制内容的颜色处理方案

        //2.效果：效果类的 API ，指的就是抗锯齿、填充/轮廓、线条宽度等等这些
        //2.1 setAntiAlias (boolean aa) 设置抗锯齿
        //抗锯齿默认是关闭的，如果需要抗锯齿，需要显式地打开。另外，除了 setAntiAlias(aa) 方法，打开抗锯齿还有一个更方便的方式：构造方法。创建 Paint 对象的时候，构造方法的参数里加一个 ANTI_ALIAS_FLAG 的 flag，就可以在初始化的时候就开启抗锯齿
//        paint.isAntiAlias = true
//        val paint1:Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        //2.2 setStyle(Paint.Style style)，设置图形是线条风格还是填充风格的（也可以二者并用）
//        paint.style = Paint.Style.FILL//默认模式
//        canvas.drawCircle(300f, 300f, 200f, paint)
//        paint.style = Paint.Style.STROKE
//        canvas.drawCircle(300f, 700f, 100f, paint)
//        paint.style = Paint.Style.FILL_AND_STROKE
//        canvas.drawCircle(300f, 1000f, 50f, paint)

        //2.3 线条形状
        //4 个方法：setStrokeWidth(float width), setStrokeCap(Paint.Cap cap), setStrokeJoin(Paint.Join join), setStrokeMiter(float miter)
        //2.3.1 设置线条宽度。单位为像素，默认值是 0
        //为 Canvas 设置 Matrix 来实现几何变换（如放大、缩小、平移、旋转），在几何变换之后 Canvas 绘制的内容就会发生相应变化，包括线条也会加粗，例如 2 像素宽度的线条在 Canvas 放大 2 倍后会被以 4 像素宽度来绘制。而当线条宽度被设置为 0 时，它的宽度就被固定为 1 像素
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = 1f
//        canvas.drawCircle(300f, 300f, 200f, paint)
//        paint.strokeWidth = 5f
//        canvas.drawCircle(300f, 700f, 100f, paint)
//        paint.strokeWidth = 0f
//        canvas.drawCircle(300f, 1000f, 50f, paint)

        //2.3.2 设置线头的形状。线头形状有三种：BUTT 平头、ROUND 圆头、SQUARE 方头。默认为 BUTT
        //BUTT和SQUARE是有区别的
//        paint.strokeWidth = 50f
//        paint.strokeCap = Paint.Cap.BUTT
//        canvas.drawLine(100f, 100f, 300f, 100f, paint)
//        paint.strokeCap = Paint.Cap.ROUND
//        canvas.drawLine(100f, 200f, 300f, 200f, paint)
//        paint.strokeCap = Paint.Cap.SQUARE
//        canvas.drawLine(100f, 300f, 300f, 300f, paint)

        //2.3.3 setStrokeJoin(Paint.Join join) 设置拐角的形状。有三个值可以选择：MITER 尖角、 BEVEL 平角和 ROUND 圆角。默认为 MITER
        //2.3.4 setStrokeMiter(float miter) 它用于设置 MITER 型拐角的延长线的最大值。所谓「延长线的最大值」
        //至于多尖的角属于过于尖，尖到需要转为使用 BEVEL 来绘制，则是由一个属性控制的，而这个属性就是 setStrokeMiter(miter) 方法中的 miter 参数

        //2.4 色彩优化
        //2.4.1 setDither(boolean dither)  设置图像的抖动，优化色彩深度降低时的绘制效果
        //现在的 Android 版本的绘制，默认的色彩深度已经是 32 位的 ARGB_8888 ，效果已经足够清晰了。只有当你向自建的 Bitmap 中绘制，并且选择 16 位色的 ARGB_4444 或者 RGB_565 的时候，开启它才会有比较明显的效果
//        paint.isDither = true
        //2.4.2 setFilterBitmap(boolean filter) 设置双线性过滤来优化 Bitmap 放大绘制的效果
//        paint.isFilterBitmap = true

        //2.5 使用 PathEffect 来给图形的轮廓设置效果。对 Canvas 所有的图形绘制有效，也就是 drawLine() drawCircle() drawPath() 这些方法
        //setPathEffect(PathEffect effect)
        // 6 种 PathEffect。分为两类，单一效果的 CornerPathEffect DiscretePathEffect DashPathEffect PathDashPathEffect ，和组合效果的 SumPathEffect ComposePathEffect
        //2.5.1 CornerPathEffect ：把所有拐角变成圆角
//        paint.style = Paint.Style.STROKE
//        paint.pathEffect = cornerPathEffect
//        path.lineTo(150f, 150f)// 画斜线
//        path.lineTo(200f, 50f)
//        path.lineTo(300f, 50f)// 画竖线
//        path.lineTo(500f, 300f)
//        canvas.drawPath(path, paint)

        //2.5.2 DiscretePathEffect 把线条进行随机的偏离，让轮廓变得乱七八糟。乱七八糟的方式和程度由参数决定
//        paint.style = Paint.Style.STROKE
//        paint.pathEffect = discretePathEffect
//        path.lineTo(150f, 150f)// 画斜线
//        path.lineTo(200f, 50f)
//        path.lineTo(300f, 50f)// 画竖线
//        path.lineTo(500f, 300f)
//        canvas.drawPath(path, paint)

        //2.5.3 DashPathEffect 使用虚线来绘制线条
//        paint.style = Paint.Style.STROKE
//        paint.pathEffect = dashPathEffect
//        path.lineTo(150f, 150f)// 画斜线
//        path.lineTo(200f, 50f)
//        path.lineTo(300f, 50f)// 画竖线
//        path.lineTo(500f, 300f)
//        canvas.drawPath(path, paint)

        //2.5.4 PathDashPathEffect 它是使用一个 Path 来绘制「虚线」
//        paint.style = Paint.Style.STROKE
//        paint.pathEffect = pathDashPathEffect
//        path.lineTo(150f, 150f)// 画斜线
//        path.lineTo(200f, 50f)
//        path.lineTo(300f, 50f)// 画竖线
//        path.lineTo(500f, 300f)
//        canvas.drawPath(path, paint)

        //2.5.5 SumPathEffect 这是一个组合效果类的 PathEffect 。它的行为特别简单，就是分别按照两种 PathEffect 分别对目标进行绘制
//        paint.style = Paint.Style.STROKE
//        paint.pathEffect = sumPathEffect
//        path.lineTo(150f, 150f)// 画斜线
//        path.lineTo(200f, 50f)
//        path.lineTo(300f, 50f)// 画竖线
//        path.lineTo(500f, 300f)
//        canvas.drawPath(path, paint)

        //2.5.6 ComposePathEffect
        //这也是一个组合效果类的 PathEffect 。不过它是先对目标 Path 使用一个 PathEffect，然后再对这个改变后的 Path 使用另一个 PathEffect
//        paint.style = Paint.Style.STROKE
//        paint.pathEffect = composePathEffect
//        path.lineTo(150f, 150f)// 画斜线
//        path.lineTo(200f, 50f)
//        path.lineTo(300f, 50f)// 画竖线
//        path.lineTo(500f, 300f)
//        canvas.drawPath(path, paint)

        //注意： PathEffect 在有些情况下不支持硬件加速，需要关闭硬件加速才能正常使用：
        //Canvas.drawLine() 和 Canvas.drawLines() 方法画直线时，setPathEffect() 是不支持硬件加速的；
        //PathDashPathEffect 对硬件加速的支持也有问题，所以当使用 PathDashPathEffect 的时候，最好也把硬件加速关了。

        //剩下的两个效果类方法：setShadowLayer() 和 setMaskFilter() ，它们和前面的效果类方法有点不一样：它们设置的是「附加效果」，也就是基于在绘制内容的额外效果
        //2.6 setShadowLayer(float radius, float dx, float dy, int shadowColor)，在之后的绘制内容下面加一层阴影
        //方法的参数里， radius 是阴影的模糊范围； dx dy 是阴影的偏移量； shadowColor 是阴影的颜色
        //如果要清除阴影层，使用 clearShadowLayer()
        //在硬件加速开启的情况下， setShadowLayer() 只支持文字的绘制，文字之外的绘制必须关闭硬件加速才能正常绘制阴影。
        //如果 shadowColor 是半透明的，阴影的透明度就使用 shadowColor 自己的透明度；而如果 shadowColor 是不透明的，阴影的透明度就使用 paint 的透明度
//        paint.setShadowLayer(10f, 0f, 0f, Color.RED)
//        paint.textSize = 60f
//        canvas.drawText("恭喜发财", 80f, 300f, paint)

        //2.7 setMaskFilter(MaskFilter maskfilter)
        //为之后的绘制设置 MaskFilter。上一个方法 setShadowLayer() 是设置的在绘制层下方的附加效果；而这个 MaskFilter 和它相反，设置的是在绘制层上方的附加效果
        //到现在已经有两个 setXxxFilter(filter) 了。前面有一个 setColorFilter(filter) ，是对每个像素的颜色进行过滤；而这里的 setMaskFilter(filter) 则是基于整个画面来进行过滤
        //2.7.1 BlurMaskFilter 模糊效果的 MaskFilter
//        paint.maskFilter = blurMaskFilter
//        canvas.drawBitmap(bitmap, 100f, 100f, paint)
        //2.7.2 EmbossMaskFilter 浮雕效果的 MaskFilter
//        paint.maskFilter = embossMaskFilter
//        canvas.drawBitmap(bitmap, 100f, 100f, paint)

        //2.8 获取绘制的 Path
        //2.8.1 getFillPath(Path src, Path dst)
        //「实际 Path」。所谓实际 Path ，指的就是 drawPath() 的绘制内容的轮廓，要算上线条宽度和设置的 PathEffect
        //默认情况下（线条宽度为 0、没有 PathEffect），原 Path 和实际 Path 是一样的；而在线条宽度不为 0 （并且模式为 STROKE 模式或 FLL_AND_STROKE ），或者设置了 PathEffect 的时候，实际 Path 就和原 Path 不一样了
        //通过 getFillPath(src, dst) 方法就能获取这个实际 Path。方法的参数里，src 是原 Path ，而 dst 就是实际 Path 的保存位置。 getFillPath(src, dst) 会计算出实际 Path，然后把结果保存在 dst 里

        //2.8.2 getTextPath(String text, int start, int end, float x, float y, Path path) / getTextPath(char[] text, int index, int count, float x, float y, Path path)
        //「文字的 Path」。文字的绘制，虽然是使用 Canvas.drawText() 方法，但其实在下层，文字信息全是被转化成图形，对图形进行绘制的。 getTextPath() 方法，获取的就是目标文字所对应的 Path 。这个就是所谓「文字的 Path」
        //这两个方法， getFillPath() 和 getTextPath() ，就是获取绘制的 Path 的方法。之所以把它们归类到「效果」类方法，是因为它们主要是用于图形和文字的装饰效果的位置计算，比如自定义的下划线效果

        //3 drawText() 相关
        //Paint 有些设置是文字绘制相关的，即和 drawText() 相关的
        //比如设置文字大小，文字间隔，各种文字效果

        //4 初始化类
        //这一类方法很简单，它们是用来初始化 Paint 对象，或者是批量设置 Paint 的多个属性的方法
        //4.1 reset()  重置 Paint 的所有属性为默认值。相当于重新 new 一个，不过性能当然高一些啦
        //4.2 set(Paint src)  把 src 的所有属性全部复制过来。相当于调用 src 所有的 get 方法，然后调用这个 Paint 的对应的 set 方法来设置它们
        //4.3 setFlags(int flags)  批量设置 flags。相当于依次调用它们的 set 方法，比如下面一行和后面两行是等效的
//        paint.flags = (Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
//        paint.isAntiAlias = true
//        paint.isDither = true


        /**
         * drawText() 文字的绘制
         */
        //1 Canvas 绘制文字的方式，Canvas 的文字绘制方法有三个：drawText() drawTextRun() 和 drawTextOnPath()
        //1.1  drawText() 是 Canvas 最基本的绘制文字的方法：给出文字的内容和位置， Canvas 按要求去绘制文字
        //text 是文字内容，x 和 y 是文字的坐标。但需要注意：这个坐标并不是文字的左上角，而是一个与左下角比较接近的位置
        //drawText() 参数中的 y ，指的是文字的基线（ baseline ） 的位置； x 坐标在 "H" 的左边再往左一点点的位置
//        paint.textSize = 60f
//        canvas.drawText("Hello", 200f, 100f, paint)
//        paint.color = Color.RED
//        canvas.drawPoint(200f, 100f, paint)

        //1.2 drawTextRun() 略过不看
        //1.3 drawTextOnPath()  沿着一条 Path 来绘制文字。这是一个耍杂技的方法
        //参数里，需要解释的只有两个： hOffset 和 vOffset。它们是文字相对于 Path 的水平偏移量和竖直偏移量，利用它们可以调整文字的位置。例如你设置 hOffset 为 5， vOffset 为 10，文字就会右移 5 像素和下移 10 像素
//        paint.textSize = 30f
//        canvas.drawPath(pathCircle, paint)
//        canvas.drawTextOnPath("Hello World", pathCircle, 0f, 0f, paint)

        //1.4 StaticLayout
        //Canvas.drawText() 只能绘制单行的文字，而不能换行；不能在换行符 \n 处换行
        //StaticLayout 支持换行，它既可以为文字设置宽度上限来让文字自动换行，也会在 \n 处主动换行
        //Canvas.save() Canvas.translate() Canvas.restore() 配合起来可以对绘制的内容进行移动
//        canvas.save()
//        canvas.translate(50f, 100f)
//        staticLayout.draw(canvas)
//        canvas.translate(0f, 200f)
//        staticLayout2.draw(canvas)
//        canvas.restore()

        //2 Paint 对文字绘制的辅助
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }
}