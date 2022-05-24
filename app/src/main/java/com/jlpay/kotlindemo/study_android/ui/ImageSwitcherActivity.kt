package com.jlpay.kotlindemo.study_android.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import kotlinx.android.synthetic.main.activity_image_switcher.*

/**
 * 支持动画的图片浏览器
 */
class ImageSwitcherActivity : AppCompatActivity() {

    private val imageIds: IntArray = intArrayOf(
        R.mipmap.zhizhuxia,
        R.mipmap.hanfei,
        R.mipmap.zinv,
        R.mipmap.nongyu,
        R.mipmap.liang
    )

    private lateinit var listItems: ArrayList<Map<String, Any>>

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, ImageSwitcherActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_switcher)

        initData()
        initView()
    }


    fun initView() {
        val gridview: GridView = findViewById(R.id.gridview)
        val simpleAdapter: SimpleAdapter = SimpleAdapter(
            this,
            listItems,
            R.layout.item_simple,
            arrayOf("image"),
            intArrayOf(R.id.header)
        )
        gridview.adapter = simpleAdapter
        //添加列表项被选中的监听器
        gridview.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //显示当前被选中的图片
                imageSwitcher.setImageResource(imageIds[position])
            }
        }
        //添加列表项被单击的监听器
        gridview.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //显示被单击的图片
                imageSwitcher.setImageResource(imageIds[position])
            }
        })

        val imageSwitcher: ImageSwitcher = findViewById(R.id.imageSwitcher)
        imageSwitcher.setFactory(object : ViewSwitcher.ViewFactory {
            override fun makeView(): View {
                //这里生成的View组件必须是ImageView，如果是TextSwitcher，这里则必须返回TextView
                val imageView: ImageView = ImageView(this@ImageSwitcherActivity)
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
//                val layoutParam: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
                val layoutParam: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                imageView.layoutParams = layoutParam
                return imageView
            }
        })
    }

    fun initData() {
        listItems = ArrayList<Map<String, Any>>()
        for ((i) in imageIds.withIndex()) {
            val listItem = HashMap<String, Any>()
            listItem["image"] = imageIds[i]
            listItems.add(listItem)
        }
    }
}