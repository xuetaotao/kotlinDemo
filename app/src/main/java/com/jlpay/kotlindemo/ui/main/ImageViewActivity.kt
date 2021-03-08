package com.jlpay.kotlindemo.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.QuickContactBadge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.jlpay.kotlindemo.R
import kotlinx.android.synthetic.main.activity_image_view.*
import kotlin.math.roundToLong

class ImageViewActivity : AppCompatActivity() {

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