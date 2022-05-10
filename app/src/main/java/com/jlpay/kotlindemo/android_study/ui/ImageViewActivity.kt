package com.jlpay.kotlindemo.android_study.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.text.Html.ImageGetter
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.QuickContactBadge
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.net.RetrofitClient
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.activity_image_view.*
import okhttp3.ResponseBody
import kotlin.math.roundToLong

/**
 * ImageView 的 scaleType属性值：（摘自疯狂Android第四版）
 * matrix：使用 matrix 方式（矩阵缩放）进行缩放
 * fit_xy：对图片横向、纵向独立缩放，使得该图片完全适用于该 ImageView，图片的纵横比可能会改变
 * fit_start：保持纵横比缩放图片，直到该图片能完全显示在ImageView中（图片较长的边长与ImageView相应的边长相等），缩放完成后将该图片放在ImageView的左上角
 * fit_center：保持纵横比缩放图片，直到该图片能完全显示在ImageView中（图片较长的边长与ImageView相应的边长相等），缩放完成后将该图片放在ImageView的中央
 * fit_end：保持纵横比缩放图片，直到该图片能完全显示在ImageView中（图片较长的边长与ImageView相应的边长相等），缩放完成后将该图片放在ImageView的右下角
 * center:将图片放在ImageView的中间，但不进行任何缩放
 * center_crop：保持纵横比缩放图片，以使得图片能完全覆盖ImageView。只要图片的最短边能显示出来即可
 * center_inside：保持纵横比缩放图片，以使得ImageView能完全显示该图片
 *
 * 以下摘自网络：
 * CENTER /center 按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示
 * CENTER_CROP / centerCrop 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
 * CENTER_INSIDE / centerInside 将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽
 * FIT_CENTER / fitCenter 把图片按比例扩大/缩小到View的宽度，居中显示
 * FIT_END / fitEnd 把图片按比例扩大/缩小到View的宽度，显示在View的下部分位置
 * FIT_START / fitStart 把图片按比例扩大/缩小到View的宽度，显示在View的上部分位置
 * FIT_XY / fitXY 把图片不按比例扩大/缩小到View的大小显示
 * MATRIX / matrix 用矩阵来绘制，动态缩小放大图片来显示。
 */
class ImageViewActivity : AppCompatActivity() {

    private val TAG: String = ImageViewActivity::class.java.simpleName

    private val images: IntArray = intArrayOf(
        R.mipmap.zhizhuxia,
        R.mipmap.baby,
        R.mipmap.cashout
    )
    private var currentImg: Int = 0
    private var alpha: Int = 255//定义图片的初始透明度

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, ImageViewActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)

        imgBrowser()
        quickContactbadge()
        //可折叠的悬浮按钮略，FloatingActionButton
        downLoadImg()
        htmlText()
    }

    /**
     * 富文本填充：带图片的html文本解析
     * 两种方式
     */
    fun htmlText() {
        val tv_htmlView: TextView = findViewById(R.id.tv_htmlView)
        val htmlStr =
            "\u003Cp\u003E尊敬的嘉联合伙人：\u003C/p\u003E\n\u003Cdiv style = \"margin-left: 2em;\"\u003E\n\u003Cp\u003E为保障您与嘉联支付业务合作的健康稳定发展，防范分润提现中存在的虚开、多开增值税发票现象，近期平台开票政策将做出如下调整：\u003C/p\u003E\n\u003Cp\u003E1.自通知发布之日起，嘉联支付对预开发票金额将按“本月预开发票额度不得超过1.5倍上月分润金额”标准进行严格审核。\u003C/p\u003E\n\u003Cp\u003E2.自2021年5月1日起，您在平台提现分润收益前（含分润账户、合作账户、直发账户、合作直发账户），需提供同等金额的含6%税率的增值税专用发票。否则嘉联支付有权退回您所提交的分润发票或从分润收益中扣除税额差额。\u003C/p\u003E\n\u003Cp\u003E请您知悉。如有疑问，请垂询952005客服热线。\u003C/p\u003E\n\u003Cp\u003E感谢您的支持！\u003C/p\u003E\n\u003Cp\u003E\u003Cimg src = \"https://iknow-pic.cdn.bcebos.com/d53f8794a4c27d1e904d81311ad5ad6edcc438df?x-bce-process%3Dimage%2Fresize%2Cm_lfit%2Cw_600%2Ch_800%2Climit_1%2Fquality%2Cq_85%2Fformat%2Cf_jpg\"/\u003E\u003C/p\u003E\n\u003C/div\u003E "

        //方式一：
        //        tv_htmlView.setText(Html.fromHtml(htmlStr));
        tv_htmlView.setText(
            Html.fromHtml(
                htmlStr,
                ImageGetterUtils.MyImageGetter(this, tv_htmlView),
                null
            )
        )

        //方式二：
//        Thread(Runnable {
//            val spanned = Html.fromHtml(htmlStr, ImageGetter { source ->
//                Log.e("测试：", "测试：$source")//这里提取出来的 source 就是图片资源 src后面那部分
//                //                        OkHttpClient okHttpClient = OkHttpClient
//                try {
//                    //                            URL url = new URL(source);
//                    //                            URLConnection urlConnection = url.openConnection();
//                    //                            InputStream inputStream = urlConnection.getInputStream();
//                    //                            Drawable drawable = Drawable.createFromStream(inputStream, null);
//                    //                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//                    //                            inputStream.close();
//                    val okHttpClient = OkHttpClient.Builder().build()
//                    val request = Request.Builder()
//                        .url(source)
//                        .build()
//                    val call = okHttpClient.newCall(request)
//                    val response = call.execute()
//                    var drawable: Drawable? = null
//                    if (response.body != null) {
//                        drawable =
//                            Drawable.createFromStream(response.body!!.byteStream(), null)
//                        drawable.setBounds(
//                            0,
//                            0,
//                            drawable.intrinsicWidth,
//                            drawable.intrinsicHeight
//                        )
//                    }
//                    return@ImageGetter drawable!!
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                null
//            }, null)
//            runOnUiThread(Runnable { tv_htmlView.text = spanned })
//        }).start()
    }

    internal object ImageGetterUtils {
        fun getImageGetter(
            context: Context?,
            textView: TextView
        ): MyImageGetter {
            return MyImageGetter(context, textView)
        }

        class MyImageGetter(
            private val context: Context?,
            private val textView: TextView
        ) :
            ImageGetter {
            private var urlDrawable: URLDrawable? = null
            override fun getDrawable(source: String): Drawable {
                Log.e("source", "source:$source")
                urlDrawable = URLDrawable()
                Glide.with(context).load(source).asBitmap()
                    .into(object : SimpleTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap?,
                            glideAnimation: GlideAnimation<in Bitmap?>?
                        ) {
                            urlDrawable!!.bitmap2 = changeBitmapSize(resource)
                            urlDrawable!!.setBounds(
                                0,
                                0,
                                changeBitmapSize(resource).width,
                                changeBitmapSize(resource).height
                            )
                            textView.invalidate()
                            textView.text = textView.text //不加这句显示不出来图片，原因不详
                        }
                    })
                return urlDrawable!!
            }

            inner class URLDrawable : BitmapDrawable() {
                var bitmap2: Bitmap? = null
                override fun draw(canvas: Canvas) {
                    super.draw(canvas)
                    if (bitmap2 != null) {
                        canvas.drawBitmap(bitmap2!!, 0f, 0f, paint)
                    }
                }
            }

            private fun changeBitmapSize(bitmap: Bitmap?): Bitmap {
                var bitmap = bitmap
                val width = bitmap?.width
                val height = bitmap?.height
                Log.e("width", "width:$width")
                Log.e("height", "height:$height")
                //设置想要的大小
                //计算压缩的比率
                val scaleWidth = width!!.toFloat() / width
                val scaleHeight = height!!.toFloat() / height
                //获取想要缩放的matrix
                val matrix = Matrix()
                matrix.postScale(scaleWidth, scaleHeight)
                //获取新的bitmap
                bitmap =
                    Bitmap.createBitmap(bitmap!!, 0, 0, width, height, matrix, true)
                bitmap.width
                bitmap.height
                Log.e("newWidth", "newWidth" + bitmap.width)
                Log.e("newHeight", "newHeight" + bitmap.height)
                return bitmap
            }

        }
    }


    @SuppressLint("AutoDispose")
    fun downLoadImg() {
        val btnGetimg: Button = findViewById(R.id.btn_getImg)
        val imageView: ImageView = findViewById(R.id.imageView)
        val imgUri =
            "https://iknow-pic.cdn.bcebos.com/d53f8794a4c27d1e904d81311ad5ad6edcc438df?x-bce-process%3Dimage%2Fresize%2Cm_lfit%2Cw_600%2Ch_800%2Climit_1%2Fquality%2Cq_85%2Fformat%2Cf_jpg"
        btnGetimg.setOnClickListener {
            RetrofitClient.get()
                .getImg(imgUri)
                .map(object : Function<ResponseBody, Bitmap> {
                    override fun apply(responseBody: ResponseBody): Bitmap {
                        val byteStream = responseBody.byteStream()
                        return BitmapFactory.decodeStream(byteStream)
                    }
                })
                .doOnSubscribe(object : Consumer<Disposable> {
                    override fun accept(t: Disposable?) {
                        //TODO
                    }
                })
                .subscribe(object : Observer<Bitmap> {
                    override fun onComplete() {
                        Log.e(TAG, "downLoadImg:onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        Log.e(TAG, "downLoadImg:onSubscribe")
                    }

                    override fun onNext(t: Bitmap) {
                        Log.e(TAG, "downLoadImg:onNext")
                        imageView.setImageBitmap(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "downLoadImg:onError")
                    }
                })
        }
    }


    /**
     * 使用QuickContactBadge关联联系人
     */
    private fun quickContactbadge() {
        val badge: QuickContactBadge = findViewById(R.id.badge)
        //单击该组件，系统会打开该联系人的联系方式界面
        badge.assignContactFromPhone("0266-88669588", false)
    }


    /**
     * 图片浏览器
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun imgBrowser() {
        val ivImage1: ImageView = findViewById(R.id.iv_image1)
        val ivImage2: ImageView = findViewById(R.id.iv_image2)
        //首先执行OnTouchListener，之后为onTouchEvent，最后才执行onClickListener内的方法，
        //至于为什么OnTouchListener和onTouchEvent执行了两次，是因为在DOWN和UP时两个方法都被调用，至于onClickListener则只在UP的时候调用
        //与事件分发相关联的三个方法分别为dispatchTouchEvent，onInterceptTouchEvent， onTouchEvent
        ivImage1.setOnTouchListener { v, event ->
            val drawable = iv_image1.drawable
            val bitmap = drawable.toBitmap()
            val scale: Double =
                1.0 * bitmap.height / iv_image1.height//bitmap图片实际大小与第一个ImageView的缩放比例
            //获取需要显示的图片开始点
            var x: Long = (event.x * scale).roundToLong()//返回参数中最接近的 long ，其中 long四舍五入为正无穷大
            var y: Long = (event.y * scale).roundToLong()
            if (x + 120 > bitmap.width) {
                x = bitmap.width - 120L
            }
            if (y + 120 > bitmap.height) {
                y = bitmap.height - 120L
            }
            ivImage2.setImageBitmap(Bitmap.createBitmap(bitmap, x.toInt(), y.toInt(), 120, 120))
            ivImage2.imageAlpha = alpha
            false
        }
        val listener: View.OnClickListener = View.OnClickListener { v ->
            when (v.id) {
                R.id.btn_plus, R.id.btn_minus -> {
                    if (v.id == R.id.btn_plus) {
                        alpha += 20
                    }
                    if (v.id == R.id.btn_minus) {
                        alpha -= 20
                    }
                    if (alpha >= 255) {
                        alpha = 255
                    }
                    if (alpha <= 0) {
                        alpha = 0
                    }
                    //改变图片的透明度
                    iv_image1.imageAlpha = alpha
                }
            }
        }
        val btnPlus: Button = findViewById(R.id.btn_plus)
        btnPlus.setOnClickListener(listener)
        val btnMinus: Button = findViewById(R.id.btn_minus)
        btnMinus.setOnClickListener(listener)
        val btnNext: Button = findViewById(R.id.btn_next)
        btnNext.setOnClickListener {
            iv_image1.setImageResource(images[++currentImg % images.size])
        }
    }
}