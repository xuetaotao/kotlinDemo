package com.jlpay.kotlindemo.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

class AdapterViewActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, AdapterViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val names = arrayOf("韩非", "张良", "弄玉", "紫女")
    private val descs = arrayOf("老大", "擅长谋略", "擅长音律的女孩", "擅长经营的女孩")

    //    private val imageIds = arrayOf(1, 2, 3)
    private val imageIds =
        intArrayOf(R.mipmap.hanfei, R.mipmap.liang, R.mipmap.nongyu, R.mipmap.zinv)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter_view)

        arrayAdapter()
        simpleAdapter()
    }

    /**
     *  SimpleAdapter创建ListView
     */
    fun simpleAdapter() {
        val listItems = ArrayList<Map<String, Any>>()
        for ((index, name) in names.withIndex()) {
            val listItem = HashMap<String, Any>()
            listItem["header"] = imageIds[index]
            listItem["personName"] = names[index]
            listItem["desc"] = descs[index]
            listItems.add(listItem)
        }
        //第四个参数：该参数决定提取Map<String,?>对象中哪些key对应的value来生成列表项
        //第五个参数：该参数应该是一个int[]类型的参数，该参数决定填充哪些组件
        val simpleAdapter: SimpleAdapter = SimpleAdapter(
            this, listItems, R.layout.item_simple, arrayOf("personName", "header", "desc"),
            intArrayOf(R.id.name, R.id.header, R.id.desc)
        )
        val mylist = findViewById<ListView>(R.id.mylist)
        mylist.adapter = simpleAdapter

        //为单击事件绑定监听器
        mylist.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(
                this@AdapterViewActivity,
                names[position] + "被点击了",
                Toast.LENGTH_SHORT
            ).show()
        }

        //为选中事件绑定监听器
        mylist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    this@AdapterViewActivity,
                    names[position] + "被选中了",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    /**
     * ArrayAdapter创建ListView
     */
    fun arrayAdapter() {
        val list1: ListView = findViewById(R.id.list1)
        val arr1 = arrayOf("Sun", "Zhu", "Sha")
        //ArrayAdapter 的每个列表项只能是TextView
        val adapter1 = ArrayAdapter(this@AdapterViewActivity, R.layout.item_array, arr1)
        list1.adapter = adapter1

        val list2: ListView = findViewById(R.id.list2)
        val arr2 = arrayOf("Spring", "Summer", "Actumn", "Winter")
        val adapter2 = ArrayAdapter(this, R.layout.item_array, arr2)
        list2.adapter = adapter2
    }
}