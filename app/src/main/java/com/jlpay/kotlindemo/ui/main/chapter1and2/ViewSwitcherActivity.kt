package com.jlpay.kotlindemo.ui.main.chapter1and2

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.bean.DataItem

/**
 * 仿Android系统的Launcher界面
 * 可以看看再优化优化滑动显示的流畅度和第一屏界面
 */
class ViewSwitcherActivity : AppCompatActivity() {

    private lateinit var viewSwitcher: ViewSwitcher
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var baseAdapter: BaseAdapter//该BaseAdapter负责为每屏显示的GridView提供列表项

    private var dataItems: ArrayList<DataItem> = ArrayList<DataItem>()
    private var screenNo: Int = -1//记录当前正在显示第几屏的程序
    private var screenCount: Int = 0//保存程序所占的总屏数

    companion object {

        val NUMBER_PER_SCREEN: Int = 12//定义一个常量，用于显示每屏显示的应用程序数

        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, ViewSwitcherActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_switch)

        initView()
        initData()
    }

    private fun initView() {
        viewSwitcher = findViewById(R.id.viewSwitcher)
        viewSwitcher.setFactory(object : ViewSwitcher.ViewFactory {
            override fun makeView(): View {
                //加载组件，实际上就是一个GridView组件
                return LayoutInflater.from(this@ViewSwitcherActivity)
                    .inflate(R.layout.item_gridview, null)
            }
        })

        btnPrev = findViewById(R.id.btn_prev)
        btnNext = findViewById(R.id.btn_next)
        next(null)//页面加载时先显示第一屏

        baseAdapter = object : BaseAdapter() {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view: View
                val viewHolder: ViewHolder
                if (convertView == null) {
                    view = LayoutInflater.from(this@ViewSwitcherActivity)
                        .inflate(R.layout.item_labelicon, null)
                    val iv_image: ImageView = view.findViewById(R.id.iv_image)
                    val tv_text: TextView = view.findViewById(R.id.tv_text)
                    viewHolder =
                        ViewHolder(
                            iv_image,
                            tv_text
                        )
                    view.tag = viewHolder

                } else {
                    view = convertView
                    viewHolder = view.tag as ViewHolder
                }

                //获取R.layout.item_labelicon布局文件中的ImageView组件，并为之设置图标
                viewHolder.iv_image.setImageDrawable(getItem(position).drawable)
                //获取R.layout.item_labelicon布局文件中的TextView组件，并为之设置文本
                viewHolder.tv_text.text = getItem(position).dataName
                return view
            }

            override fun getItem(position: Int): DataItem {
                //根据screenNo计算第position个列表项的数据
                return dataItems[screenNo * NUMBER_PER_SCREEN + position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getCount(): Int {
                //如果已经到了最后一屏，且应用程序的数量不能整除NUMBER_PER_SCREEN
                return if (screenNo == screenCount - 1 && dataItems.size % NUMBER_PER_SCREEN != 0) {
                    dataItems.size % NUMBER_PER_SCREEN//最后一屏显示的
                } else {
                    NUMBER_PER_SCREEN
                }
            }
        }
    }

    private fun initData() {
        for (i in 0 until 40) {//[0,40)
            val label: String = i.toString()
            val drawable: Drawable = resources.getDrawable(R.mipmap.ic_launcher)
            val dataItem: DataItem = DataItem(label, drawable)
            dataItems.add(dataItem)
        }
        //计算应用程序所占的总屏数
        screenCount = if (dataItems.size % NUMBER_PER_SCREEN == 0) {
            dataItems.size / NUMBER_PER_SCREEN
        } else {
            dataItems.size / NUMBER_PER_SCREEN + 1
        }
    }

    fun prev(view: View) {
        if (screenNo > 0) {
            screenNo--
            //为ViewSwitcher的组件显示过程设置动画
            viewSwitcher.setInAnimation(this, R.anim.slide_in_right)
            //为ViewSwitcher的组件隐藏过程设置动画
            viewSwitcher.setOutAnimation(this, R.anim.slide_out_left)
            //控制下一屏将要显示的 GridView对应的Adapter
            (viewSwitcher.nextView as GridView).adapter = baseAdapter
            //单击右边按钮，显示上一屏
            viewSwitcher.showPrevious()
        }
    }

    fun next(view: View?) {
        if (screenNo < screenCount - 1) {
            screenNo++
            //为ViewSwitcher的组件显示过程设置动画
            viewSwitcher.setInAnimation(this, R.anim.slide_in_right)
            //为ViewSwitcher的组件隐藏过程设置动画
            viewSwitcher.setOutAnimation(this, R.anim.slide_out_left)
            //控制下一屏将要显示的 GridView对应的Adapter
            (viewSwitcher.nextView as GridView).adapter = baseAdapter
            //单击右边按钮，显示下一屏
            viewSwitcher.showNext()
        }
    }

    class ViewHolder(var iv_image: ImageView, var tv_text: TextView) {

    }
}