package com.jlpay.kotlindemo.study_android.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

class XmlMenuActivity : AppCompatActivity() {

    private lateinit var tvContextmenu: TextView
    private lateinit var popupMenu: PopupMenu

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, XmlMenuActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xml_menu)

        initView()
    }

    fun initView() {
        tvContextmenu = findViewById(R.id.tv_contextMenu)
        registerForContextMenu(tvContextmenu)//为文本框注册上下文菜单

        //1.创建PopupMenu菜单
        val btnPopupmenu: Button = findViewById(R.id.btn_popupMenu)
        popupMenu = PopupMenu(this, btnPopupmenu)
        //2.调用menuInflater.inflate方法将菜单资源加载到popupMenu菜单中
        menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        //为popupMenu菜单项绑定事件监听器
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.edit -> Toast.makeText(this@XmlMenuActivity, "点击了编辑菜单", Toast.LENGTH_SHORT)
                        .show()
                    R.id.exit -> popupMenu.dismiss()
                }
                return true
            }
        })
    }

    /**
     * 创建上下文菜单时触发该方法
     */
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu, menu)
        menu?.setHeaderIcon(R.mipmap.hanfei)
        menu?.setHeaderTitle("请选择背景色")

//        super.onCreateContextMenu(menu, v, menuInfo)
    }

    /**
     * 上下文菜单的菜单项被单击时触发该方法
     */
    override fun onContextItemSelected(item: MenuItem): Boolean {
        item.isChecked = true//勾选该菜单项
        when (item.itemId) {
            R.id.red -> tvContextmenu.setBackgroundColor(Color.RED)
            R.id.green -> tvContextmenu.setBackgroundColor(Color.GREEN)
            R.id.blue -> tvContextmenu.setBackgroundColor(Color.BLUE)
        }

        return true
//        return super.onContextItemSelected(item)
    }

    /**
     * 选项菜单和子菜单(SubMenu)，当用户单击Menu按键时触发该方法
     * Android提供了ActionBar来打开选项菜单(ActionBar右边的折叠图标-三个点)
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)//装填R.menu.options_menu对应的菜单，并添加到menu中
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 选项菜单和子菜单 的菜单项被单击后的回调方法
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.isCheckable) {
            item.isChecked = true
        }
        when (item.itemId) {
            R.id.font_10 -> tvContextmenu.textSize = 10 * 2f
            R.id.font_12 -> tvContextmenu.textSize = 12 * 2f
            R.id.font_14 -> tvContextmenu.textSize = 14 * 2f
            R.id.font_16 -> tvContextmenu.textSize = 16 * 2f
            R.id.font_18 -> tvContextmenu.textSize = 18 * 2f
            R.id.red_font -> tvContextmenu.setTextColor(Color.RED)
            R.id.green_font -> tvContextmenu.setTextColor(Color.GREEN)
            R.id.blue_font -> tvContextmenu.setTextColor(Color.BLUE)
            R.id.plain_item -> Toast.makeText(this, "common options Menu", Toast.LENGTH_SHORT)
                .show()
        }

        return true
//        return super.onOptionsItemSelected(item)
    }

    fun popUpMenuClick(view: View) {
        //3.显示popupMenu菜单
        popupMenu.show()
    }
}