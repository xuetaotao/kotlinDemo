package com.jlpay.kotlindemo.ui.main.chapter1and2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.bean.Person

class RecyclerViewActivity : AppCompatActivity() {

    private var personList: ArrayList<Person> = ArrayList()

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, RecyclerViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)

        initData()
        val recyclerview: RecyclerView = findViewById(R.id.recyclerview)
        //设置RecyclerView保持固定大小，这样可以优化RecyclerView的性能
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerview.layoutManager = linearLayoutManager
        recyclerview.adapter =
            MyRecyclerAdapter(
                this,
                personList
            )
    }

    private fun initData() {
        val names = arrayOf("韩非", "张良", "弄玉", "紫女")
        val descs: Array<String> = arrayOf("老大", "擅长谋略", "擅长音律的女孩", "擅长经营的女孩")
        val imageIds: IntArray =
            intArrayOf(R.mipmap.hanfei, R.mipmap.liang, R.mipmap.nongyu, R.mipmap.zinv)
        for ((i) in names.withIndex()) {
            val person: Person = Person(names[i], descs[i], imageIds[i])
            personList.add(person)
        }
    }

    class MyRecyclerAdapter(
        private var context: Context,
        private var personList: ArrayList<Person>
    ) :
        RecyclerView.Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.item_simple, parent, false)
            val viewHolder: MyViewHolder =
                MyViewHolder(
                    view
                )
//            val position:Int = viewHolder.adapterPosition

            viewHolder.llRoot.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val i: Int = viewHolder.adapterPosition
                    val person: Person =
                        Person(personList[i].name, personList[i].desc, personList[i].headerImg)
                    notifyItemInserted(i + 1)
                    personList.add(person)
                    notifyItemRangeChanged(i + 1, itemCount)
                }
            })
            viewHolder.llRoot.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {
                    val position: Int = viewHolder.adapterPosition
                    notifyItemRemoved(position)
                    //删除底层数据模型中的数据
                    personList.remove(personList[position])
                    //通知RecyclerView执行实际的删除动作
                    notifyItemRangeChanged(position, itemCount)
                    return false
                }
            })

            return viewHolder
        }

        override fun getItemCount(): Int {
            return personList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val person: Person = personList[position]
            holder.name.text = person.name
            holder.desc.text = person.desc
            holder.header.setImageResource(person.headerImg)
        }

        fun updateData(personList: ArrayList<Person>) {
            this.personList = personList
            notifyDataSetChanged()
        }
    }

    class MyViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        var header: ImageView = view.findViewById(R.id.header)
        var name: TextView = view.findViewById(R.id.name)
        var desc: TextView = view.findViewById(R.id.desc)
        var llRoot: LinearLayout = view.findViewById(R.id.ll_root)
    }

}