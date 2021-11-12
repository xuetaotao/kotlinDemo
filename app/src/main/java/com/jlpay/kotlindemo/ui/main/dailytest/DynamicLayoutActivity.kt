package com.jlpay.kotlindemo.ui.main.dailytest

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.bean.DynamicLayout
import com.jlpay.kotlindemo.ui.utils.DialogUtils
import com.jlpay.kotlindemo.ui.widget.dynamicItemView.CustomItemView1
import com.jlpay.kotlindemo.ui.widget.dynamicItemView.DispatchItemData
import com.jlpay.kotlindemo.ui.widget.dynamicItemView.parseJsonFile

class DynamicLayoutActivity : AppCompatActivity() {

    private val TAG = DynamicLayoutActivity::class.java.simpleName

    private lateinit var ll_parent: LinearLayoutCompat
    private lateinit var parseLayoutFromGson2: ParseLayoutFromGson2
    private lateinit var customItemView1: CustomItemView1
    private var dynamicLayoutList: List<DynamicLayout>? = mutableListOf()
    private lateinit var dispatchItemData: DispatchItemData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_layout)

        initView()
        initData()

        ll_parent = findViewById(R.id.ll_parent)

        //first way
        ll_parent.addView(ParseLayoutFromGson(this, "0", "套餐名称", "请输入套餐自定义名称").createViewType1())
        ll_parent.addView(ParseLayoutFromGson(this, "0", "介绍信息", "请输入介绍信息").createViewType1())
        ll_parent.addView(
            ParseLayoutFromGson(
                this,
                "0",
                "内卡借记卡(%)",
                "请输入费率(0.2-1)"
            ).createViewType1()
        )


        //second way
        //添加多个布局的话需要加个id区分哪个edittext
        parseLayoutFromGson2 = ParseLayoutFromGson2(this, "0", "借记卡封顶(元)", "默认值")
        val createViewType2 = parseLayoutFromGson2.createViewType2()
        ll_parent.addView(createViewType2)


        //third way
        var editText3: String = ""
        val parseLayoutFromGson3 =
            ParseLayoutFromGson3(this, "0", "debit_card", "内卡贷记卡(%)", "请输入费率(0.2-1)")
//        val createViewType3 = parseLayoutFromGson3.createViewType2(object : GetInputContent {
//            override fun updateInput(editStr: String) {
//                editText3 = editStr
//                Log.e(TAG, "getInputContent: editText3:$editText3")
//            }
//        })
        val createViewType3 = parseLayoutFromGson3.createViewType3 {
            editText3 = it
            Log.e(TAG, "getInputContent: editText3:$editText3")
        }
        ll_parent.addView(createViewType3)


        //fourth way
        var editText4: String = ""
        ll_parent.addView(
            ParseLayoutFromGson4.Builder()
                .context(this)
                .layoutLeft("银联二维码(%)")
                .layoutRightHint("请输入费率(0.2-1)")
                .editStrObserver {
                    editText4 = it
                    Log.e(TAG, "onCreate: editText4:$editText4")
                }
                .build()
        )
        ll_parent.addView(
            ParseLayoutFromGson4.Builder()
                .context(this)
                .layoutLeft("银联二维码(%)")
                .layoutRightHint("请输入费率(0.2-1)")
                .uiType("1")
                .optionSelectClick {
                    DialogUtils.showTimeSelect(this, it as TextView)
                }
                .build()
        )

        //fifth way
//        var char = 'a'
//        for (i in 0 until 10) {
//            val charToString = (char++).toString()
//            dynamicLayoutList.add(
//                DynamicLayout(
//                    charToString,
//                    charToString,
//                    charToString,
//                    "0",
//                    "string"
//                )
//            )
//        }

        //从JSON文件中加载
        dynamicLayoutList = parseJsonFile(this)?.dynamicLayoutList

        dynamicLayoutList?.let {
            dispatchItemData = DispatchItemData(ll_parent, this, it)
            dispatchItemData.dispatchView()
        }
    }

    //second way 使用
    fun btnGet(view: android.view.View) {
        //second way
//        val editTextStr = parseLayoutFromGson2.getEditTextStr()
//        Toast.makeText(this, "这是：$editTextStr", Toast.LENGTH_SHORT).show()

        //fifth way
//        val result = customItemView1.getResult()
//        Toast.makeText(this, "这是：$result", Toast.LENGTH_SHORT).show()

        //获取单个内容
//        dynamicLayoutList?.let {
//            val name = it[0].name
//            val viewResult =
//                if (!TextUtils.isEmpty(dispatchItemData.getViewResult(name))) dispatchItemData.getViewResult(
//                    name
//                ) else "结果为空"
//            Toast.makeText(this, "${name}的结果是：${viewResult}", Toast.LENGTH_SHORT)
//                .show()
//        }

        //获取全部内容
        dynamicLayoutList?.let { dynamicLayoutList ->
            val stringBuilder = StringBuilder()
            dynamicLayoutList.forEach {
                stringBuilder.append(
                    "${it.name}的结果是：${
                        if (dispatchItemData.getViewResult(it.name)
                                .isNullOrEmpty()
                        ) "结果为空" else dispatchItemData.getViewResult(it.name)
                    }\n"
                )
            }
            Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initData() {
    }

    private fun initView() {
    }

    /////////////////////////fourth way////////////////////////////////////////////////////////////////////
    class ParseLayoutFromGson4 private constructor(builder: Builder) {

        class Builder constructor() {
            private var context: Context? = null
            private var uiType: String = "0"
            private var lineName: String? = null
            private var layoutLeft: String? = null
            private var layoutRightHint: String? = null
            private var editStrObserver: ((editStr: String) -> Unit)? = null
            private var optionSelectClick: ((view: View) -> Unit)? = null

            fun context(context: Context) = apply {
                this.context = context
            }

            fun uiType(uiType: String) = apply {
                this.uiType = uiType
            }

            fun lineName(lineName: String) = apply {
                this.lineName = lineName
            }

            fun layoutLeft(layoutLeft: String) = apply {
                this.layoutLeft = layoutLeft
            }

            fun layoutRightHint(layoutRightHint: String) = apply {
                this.layoutRightHint = layoutRightHint
            }

            fun editStrObserver(editStrObserver: ((str: String) -> Unit)?) = apply {
                this.editStrObserver = editStrObserver
            }

            fun optionSelectClick(optionSelectClick: ((view: View) -> Unit)?) = apply {
                this.optionSelectClick = optionSelectClick
            }

            private fun createViewType(): View {
                if (context == null) {
                    throw NullPointerException("Context is null")
                }
                return when (uiType) {
                    "0" -> {
                        createViewType0(editStrObserver)
                    }
                    "1" -> {
                        createViewType1(optionSelectClick)
                    }
                    else -> {//TODO
                        createViewType0(editStrObserver)
                    }
                }
            }

            //左边TextView，右边EditText
            private fun createViewType0(lambda: ((editStr: String) -> Unit)?): View {
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.item_layout_from_gson1, null)
                val textview = view.findViewById<TextView>(R.id.textview)
                textview.text = layoutLeft
                val edittext = view.findViewById<EditText>(R.id.edittext)
                edittext.hint = layoutRightHint
                edittext.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        lambda?.invoke(s.toString())
                    }
                })
                return view
            }

            //左边TextView，右边TextView(点击事件触发选择器)
            private fun createViewType1(optionSelectClick: ((view: View) -> Unit)?): View {
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.item_layout_from_gson3, null)
                val textview = view.findViewById<TextView>(R.id.textview)
                textview.text = layoutLeft
                val tvOption = view.findViewById<TextView>(R.id.tv_option)
                tvOption.text = layoutRightHint
                optionSelectClick?.let {
                    tvOption.setOnClickListener(optionSelectClick)
                }
                return view
            }

            fun build(): View = createViewType()
        }
    }


    /////////////////////////fourth way////////////////////////////////////////////////////////////////////


    /////////////////////////third way////////////////////////////////////////////////////////////////////
    //面向接口？？
    interface GetInputContent {
        fun updateInput(editStr: String)
    }

    //改成Builder() 建造者模式？？
    class ParseLayoutFromGson3(
        private var context: Context,
        private var uiType: String,
        private var inputType: String,//这一栏是什么东西
        private var layoutLeft: String,//这一栏左侧是什么东西
        private var layoutRightHint: String,//这一栏右侧提示输入是什么东西
    ) {

        //        fun createViewType2(getInputContent: GetInputContent): View {
        fun createViewType3(lambda: (str: String) -> Unit): View {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.item_layout_from_gson1, null)
            val textview = view.findViewById<TextView>(R.id.textview)
            textview.text = layoutLeft
            val edittext = view.findViewById<EditText>(R.id.edittext)
            edittext.hint = layoutRightHint
            edittext.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
//                    Log.e("MVVMS：", "表现数据通知内存了！！${s}")
//                    getInputContent.updateInput(s.toString())
                    lambda.invoke(s.toString())
                }
            })
            return view
        }
    }


    /////////////////////////third way////////////////////////////////////////////////////////////////////


    /////////////////////////second way////////////////////////////////////////////////////////////////////
    class ParseLayoutFromGson2(
        private var context: Context,
        private var type: String,
        private var layoutLeft: String,
        private var layoutRightHint: String,
    ) {
        //second way
        var edittext: EditText? = null

        fun createViewType2(): View {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.item_layout_from_gson1, null)
            val textview = view.findViewById<TextView>(R.id.textview)
            textview.text = layoutLeft
            edittext = view.findViewById<EditText>(R.id.edittext)
            edittext!!.hint = layoutRightHint

            //实现数据UI双向绑定，其实可以不要，用不到
//            CustomViewBindData(edittext!!)
            return view
        }

        fun getEditTextStr(): String {
            return edittext?.text.toString()
        }

    }

    class StringWrap {
        var str: String? = null
            set(value) {
                field = value
                onChangeListener?.onChange(value)
            }
        var onChangeListener: OnChangeListener? = null

        interface OnChangeListener {
            fun onChange(newStrValue: String?)
        }
    }

    class CustomViewBindData(editText: EditText) {
        init {
            val stringWrap = StringWrap()
            bind(editText, stringWrap)
//            stringWrap.str = "测试，内存通知表现数据了"
        }

        private fun bind(editText: EditText, stringWrap: StringWrap) {
            stringWrap.onChangeListener = object : StringWrap.OnChangeListener {
                override fun onChange(newStrValue: String?) {
                    if (!TextUtils.equals(newStrValue, editText.text)) {
                        editText.setText(newStrValue)
//                        Log.e("MVVMS：", "内存通知表现数据了！！${newStrValue}")
                    }
                }
            }
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (!TextUtils.equals(s, stringWrap.str)) {
                        stringWrap.str = s.toString()
//                        Log.e("MVVMS：", "表现数据通知内存了！！${s}")
                    }
                }
            })
        }
    }
    /////////////////////////second way////////////////////////////////////////////////////////////////////


    /////////////////////////first way////////////////////////////////////////////////////////////////////
    class ParseLayoutFromGson(
        private var context: Context,
        private var type: String,
        private var layoutLeft: String,
        private var layoutRightHint: String,
    ) {
        //first way
        fun createViewType1(): View {
            val linearLayoutCompat: LinearLayoutCompat = LinearLayoutCompat(context)
            val layoutParams = LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, dip2px(context, 10), 0, 0)
            linearLayoutCompat.layoutParams = layoutParams
            linearLayoutCompat.setBackgroundResource(R.drawable.rectangle19)
            linearLayoutCompat.setPadding(
                dip2px(context, 2),
                dip2px(context, 2),
                dip2px(context, 2),
                dip2px(context, 2)
            )
            linearLayoutCompat.gravity = Gravity.CENTER_VERTICAL
            //左侧TextView：提示输入的属性名称
            val layoutLeftTextView: TextView = TextView(context)
            layoutLeftTextView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutLeftTextView.setPadding(
                dip2px(context, 14),
                dip2px(context, 14),
                dip2px(context, 14),
                dip2px(context, 14)
            )
            layoutLeftTextView.text = layoutLeft
            layoutLeftTextView.textSize = 14F
            layoutLeftTextView.setTextColor(context.resources.getColor(R.color.common_black))
            layoutLeftTextView.gravity = Gravity.CENTER
            //右侧EditText：提示Hint，默认输入为String
            val layoutRightEditText: EditText = EditText(context)
            layoutRightEditText.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutRightEditText.setPadding(
                dip2px(context, 14),
                dip2px(context, 14),
                dip2px(context, 14),
                dip2px(context, 14)
            )
            layoutRightEditText.gravity = Gravity.END
            layoutRightEditText.background = null
            layoutRightEditText.hint = layoutRightHint
            layoutRightEditText.textSize = 14F
            layoutRightEditText.setHintTextColor(context.resources.getColor(R.color.common_gray))
            linearLayoutCompat.addView(layoutLeftTextView)
            linearLayoutCompat.addView(layoutRightEditText)
            return linearLayoutCompat
        }


        /**
         * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
         */
        fun dip2px(context: Context, dpValue: Int): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (dpValue.toFloat() * scale + 0.5f).toInt()
        }
    }
    /////////////////////////first way////////////////////////////////////////////////////////////////////


}