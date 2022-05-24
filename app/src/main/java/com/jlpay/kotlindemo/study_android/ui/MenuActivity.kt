package com.jlpay.kotlindemo.study_android.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

/**
 * 为Android应用添加菜单或者子菜单的步骤如下：
 * 1.重写Activity的onCreateOptionsMenu(menu: Menu?)方法，在该方法里调用Menu对象的方法来添加菜单项或者子菜单
 * 2.如果希望应用程序能响应菜单项的单击事件，那么重写Activity的onOptionsItemSelected(item: MenuItem)方法即可
 */
class MenuActivity : AppCompatActivity() {

    //定义"字体大小"菜单项的标志
    private val FONT_10: Int = 0x111
    private val FONT_12: Int = 0x112
    private val FONT_14: Int = 0x113
    private val FONT_16: Int = 0x114
    private val FONT_18: Int = 0x115

    //定义"普通菜单项"的标志
    private val PLAIN_ITEM: Int = 0x11b

    //定义"字体颜色"菜单项的标志
    private val FONT_RED: Int = 0x116
    private val FONT_BLUE: Int = 0x117
    private val FONT_GREEN: Int = 0x118

    //定义“启动程序”的标志
    private val START_PROG: Int = 0x119
    private val START_PROG2: Int = 0x120

    //定义上下文菜单的标志
    private val MENU1: Int = 0x121
    private val MENU2: Int = 0x122
    private val MENU3: Int = 0x123

    private lateinit var tvContextmenu: TextView

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, MenuActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        initView()
    }

    fun initView() {
        tvContextmenu = findViewById(R.id.tv_contextMenu)
        registerForContextMenu(tvContextmenu)//为文本框注册上下文菜单
    }

    /**
     * 创建上下文菜单时触发该方法
     */
    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu.add(0, MENU1, 0, "红色")
        menu.add(0, MENU2, 0, "绿色")
        menu.add(0, MENU3, 0, "蓝色")
        menu.setGroupCheckable(0, true, true)//将三个菜单项设为单选菜单项
        menu.setHeaderIcon(R.mipmap.nongyu)
        menu.setHeaderTitle("选择背景色：")

//        super.onCreateContextMenu(menu, v, menuInfo)
    }

    /**
     * 上下文菜单的菜单项被单击时触发该方法
     */
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENU1 -> tvContextmenu.setBackgroundColor(Color.RED)
            MENU2 -> tvContextmenu.setBackgroundColor(Color.GREEN)
            MENU3 -> tvContextmenu.setBackgroundColor(Color.BLUE)
        }

        return true
    }

    /**
     * 选项菜单和子菜单(SubMenu)，当用户单击Menu按键时触发该方法
     * Android提供了ActionBar来打开选项菜单(ActionBar右边的折叠图标-三个点)
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //向menu中添加“字体大小”的子菜单
        val fontMenu = menu.addSubMenu("字体大小")
        fontMenu.setIcon(R.mipmap.icon_arrow_gray)//设置菜单的图标
        fontMenu.setHeaderIcon(R.mipmap.icon_arrow_gray)//设置菜单头的图标
        fontMenu.setHeaderTitle("选择字体大小：")//设置菜单头的标题
        fontMenu.add(0, FONT_10, 0, "10号字体")//添加一个新的处于groupId组的子菜单
        fontMenu.add(0, FONT_12, 0, "12号字体")
        fontMenu.add(0, FONT_14, 0, "14号字体")
        fontMenu.add(0, FONT_16, 0, "16号字体")
        fontMenu.add(0, FONT_18, 0, "18号字体")

        //向Menu中添加普通菜单项
        menu.add(0, PLAIN_ITEM, 0, "普通菜单项")

        //向menu中添加“字体颜色”的子菜单
        val colorMenu = menu.addSubMenu("字体颜色")
        colorMenu.setIcon(R.mipmap.icon_arrow_gray)//设置菜单的图标
        colorMenu.setHeaderIcon(R.mipmap.icon_arrow_gray)//设置菜单头的图标
        colorMenu.setHeaderTitle("选择文字颜色：")//设置菜单头的标题
        colorMenu.add(0, FONT_RED, 0, "红色")
        colorMenu.add(0, FONT_GREEN, 0, "绿色")
        colorMenu.add(0, FONT_BLUE, 0, "蓝色")

        //向menu中添加“启动程序”的子菜单
        val progMenu = menu.addSubMenu("启动程序")
        progMenu.setHeaderIcon(R.mipmap.hanfei)
        progMenu.setHeaderTitle("选择您要启动的程序：")
        progMenu.add(0, START_PROG, 0, "ImageViewActivity")
        progMenu.add(0, START_PROG2, 0, "LinearLayoutActivity")

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 选项菜单和子菜单 的菜单项被单击后的回调方法
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //判断单击的是哪个菜单项，并有针对性的做出响应
        when (item.itemId) {
            FONT_10 -> Toast.makeText(this, "10号字体", Toast.LENGTH_SHORT).show()
            FONT_12 -> Toast.makeText(this, "12号字体", Toast.LENGTH_SHORT).show()
            FONT_14 -> Toast.makeText(this, "14号字体", Toast.LENGTH_SHORT).show()
            FONT_16 -> Toast.makeText(this, "16号字体", Toast.LENGTH_SHORT).show()
            FONT_18 -> Toast.makeText(this, "18号字体", Toast.LENGTH_SHORT).show()
            FONT_RED -> Toast.makeText(this, "红色", Toast.LENGTH_SHORT).show()
            FONT_GREEN -> Toast.makeText(this, "绿色", Toast.LENGTH_SHORT).show()
            FONT_BLUE -> Toast.makeText(this, "蓝色", Toast.LENGTH_SHORT).show()
            PLAIN_ITEM -> {
                Toast.makeText(this, "普通菜单项", Toast.LENGTH_SHORT).show()
            }
            START_PROG -> {
                val intent: Intent = Intent(this, ImageViewActivity::class.java)
//                item.intent = intent//官方给的这个方法不知道为什么无效
                startActivity(intent)
            }
            START_PROG2 -> {
                val intent: Intent = Intent(this, LinearLayoutActivity::class.java)
//                item.intent = intent
                startActivity(intent)
            }
        }

        return true
    }
}