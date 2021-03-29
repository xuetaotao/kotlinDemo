package com.jlpay.kotlindemo.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
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
    private val books = arrayOf("AA", "BB", "CC", "DD", "EE")

    //    private val imageIds = arrayOf(1, 2, 3)
    private val imageIds =
        intArrayOf(R.mipmap.hanfei, R.mipmap.liang, R.mipmap.nongyu, R.mipmap.zinv)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter_view)

        arrayAdapter()
        simpleAdapter()
        autoComplete()
        expandable()
    }


    /**
     *
     */
    fun expandable() {
        val logos: IntArray =
            intArrayOf(R.mipmap.ic_launcher, R.mipmap.zhizhuxia, R.mipmap.hanfei)
        val armTypes = arrayOf("letter", "number", "characters")
        val arms = arrayOf(
            arrayOf("AA", "BB", "CC"),
            arrayOf("11", "22", "33"),
            arrayOf("嗯嗯", "好呢", "是的")
        )

        val expandableListAdapter: ExpandableListAdapter = object : BaseExpandableListAdapter() {

            //获取指定组位置处的组数据
            override fun getGroup(groupPosition: Int): Any {
                return armTypes[groupPosition]
            }

            //指定位置上的子元素是否可选中
            override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
                return true
            }

            //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
            override fun hasStableIds(): Boolean {
                return true
            }

            /**
             * 获取显示指定分组的视图
             *
             * @param groupPosition 组位置
             * @param isExpanded    该组是展开状态还是伸缩状态，true=展开
             * @param convertView   重用已有的视图对象
             * @param parent        返回的视图对象始终依附于的视图组
             * @return
             */
            override fun getGroupView(
                groupPosition: Int,
                isExpanded: Boolean,
                convertView: View?,
                parent: ViewGroup?
            ): View {
                lateinit var ll: LinearLayout
                val viewHolder: ViewHolder
                if (convertView == null) {
                    ll = LinearLayout(this@AdapterViewActivity)
                    ll.orientation = LinearLayout.HORIZONTAL
                    val imageView: ImageView = ImageView(this@AdapterViewActivity)
                    ll.addView(imageView)
                    val textView: TextView = TextView(this@AdapterViewActivity)
                    ll.addView(textView)
                    viewHolder = ViewHolder(imageView, textView)
                    ll.tag = viewHolder

                } else {
                    ll = convertView as LinearLayout
                    viewHolder = ll.tag as ViewHolder
                }

                viewHolder.imageView.setImageResource(logos[groupPosition])
                viewHolder.textView.text = armTypes[groupPosition]
                return ll
            }

            override fun getChildrenCount(groupPosition: Int): Int {
                return arms[groupPosition].size
            }

            //获取指定组位置、指定子列表项处的子列表项数据
            override fun getChild(groupPosition: Int, childPosition: Int): Any {
                return arms[groupPosition][childPosition]
            }

            override fun getGroupId(groupPosition: Int): Long {
                return groupPosition.toLong()
            }

            override fun getChildView(
                groupPosition: Int,
                childPosition: Int,
                isLastChild: Boolean,
                convertView: View?,
                parent: ViewGroup?
            ): View {
                lateinit var textView: TextView
                if (convertView == null) {
                    textView = TextView(this@AdapterViewActivity)
                    val layoutParams: AbsListView.LayoutParams = AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    textView.layoutParams = layoutParams
                    //按位与：Kotlin(and)，等价于Java(&)
                    //按位或：Kotlin(or)，等价于Java(|)
                    //按位异或：Kotlin(xor)，等价于Java(异或)
                    textView.gravity = Gravity.CENTER_VERTICAL or Gravity.START
                    textView.setPadding(36, 10, 0, 10)
                    textView.textSize = 18f

                } else {
                    textView = convertView as TextView
                }

                textView.text = getChild(groupPosition, childPosition).toString()
                return textView
            }

            override fun getChildId(groupPosition: Int, childPosition: Int): Long {
                return childPosition.toLong()
            }

            override fun getGroupCount(): Int {
                return armTypes.size
            }
        }

        val expandableListView: ExpandableListView = findViewById(R.id.expandableListView)
        expandableListView.setAdapter(expandableListAdapter)
        expandableListView.setOnChildClickListener(object :
            ExpandableListView.OnChildClickListener {
            override fun onChildClick(
                parent: ExpandableListView?,
                v: View?,
                groupPosition: Int,
                childPosition: Int,
                id: Long
            ): Boolean {
                Toast.makeText(
                    this@AdapterViewActivity,
                    armTypes[groupPosition] + "：" + arms[groupPosition][childPosition],
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        })
    }


    /**
     * 内部类：定义在类内部的类，与类成员有相似的访问控制；Kotlin默认是静态内部类，非静态类用inner关键字，this@outter,this@inner的用法
     * 菲静态内部类，持有外部类的引用，可以访问到外部类的成员变量(实例化也需要先new 外部类)；静态内部类不能访问外部类的成员变量；
     * 匿名内部类：没有定义名字的内部类，类名编译时产生，类似 OuterClass$1.class
     * 在java中，匿名内部类就能继承一个类，不能实现接口
     * kotlin中匿名内部类，就和普通的类一样，可以继承一个类，实现多个接口
     */
    class ViewHolder(var imageView: ImageView, var textView: TextView) {

    }


    /**
     * 自动完成文本框
     */
    fun autoComplete() {
        val arrayAdapter = ArrayAdapter(this, R.layout.item_array, books)
        val autoComplete: AutoCompleteTextView = findViewById(R.id.autoComplete)
        autoComplete.setAdapter(arrayAdapter)
        val multiply: MultiAutoCompleteTextView = findViewById(R.id.multiply)
        multiply.setAdapter(arrayAdapter)
        //为MultiAutoCompleteTextView设置分隔符
        multiply.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
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