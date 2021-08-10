package com.jlpay.kotlindemo.ui.main.chapter1and2

import android.animation.Animator
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.content.res.XmlResourceParser
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.utils.NotificationUtils
import java.io.InputStream
import java.util.*

class OtherUIModuleActivity : AppCompatActivity() {

    private val CHANNEL_ID: String = "crazyit"
    private val NOTIFICATION_ID: Int = 0x123

    private lateinit var notificationManager: NotificationManager

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent: Intent = Intent(context, OtherUIModuleActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_ui)

        initView()
    }

    fun initView() {
        toastPractice()
        calendarViewPractice()
        datePickerAndtimePicker()
        searchViewPractice()
        notificationPractice()
        numberPickerTest()

        assetsRes()
    }

    fun assetsRes() {
        val assets: AssetManager = assets
        val open: InputStream = assets.open("test.txt")
    }

    /**
     * 解析XML资源
     */
    public fun xmlParse(view: View) {
        val tv_xml: TextView = findViewById(R.id.tv_xml)

        //根据xml的资源ID获取解析该资源的解析器，返回的XmlResourceParser是 XmlPullParser 的子类
        val xml: XmlResourceParser = resources.getXml(R.xml.books)
        try {
            val stringBuilder: StringBuilder = StringBuilder()
            //还没有到XML文档的结尾处
            while (xml.eventType != XmlResourceParser.END_DOCUMENT) {
                //如果遇到开始标签
                if (xml.eventType == XmlResourceParser.START_TAG) {
                    //获取该标签的标签名
                    val name: String = xml.name
                    //如果遇到book标签
                    if (name == "book") {
                        //根据属性名来获取属性值
                        val bookPrice = xml.getAttributeValue(null, "price")
                        stringBuilder.append("价格：")
                        stringBuilder.append(bookPrice)
                        //根据属性索引来获取属性值
                        val bookDate = xml.getAttributeValue(1)
                        stringBuilder.append("  出版日期：")
                        stringBuilder.append(bookDate)
                        stringBuilder.append("  书名：")
                        //获取文本节点的值
                        stringBuilder.append(xml.nextText())
                    }
                    stringBuilder.append("\n")
                }
                //获取解析器的下一个事件
                xml.next()
            }
            tv_xml.text = stringBuilder.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 加载补间动画资源
     * Android动画分为：
     * 视图动画(View动画)：作用对象是视图View,分为补间动画和逐帧动画
     * 属性动画：作用对象是任意Java对象（不再局限视图View对象），可自定义各种动画效果（不再局限于4种基本变换）
     */
    public fun loadAnim(view: View) {
        val iv_load_image: ImageView = findViewById(R.id.iv_load_image)
        //加载动画资源
        val anim: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        //设置动画结束后保留结束状态
        anim.fillAfter = true
        //开始动画
        iv_load_image.startAnimation(anim)//startAnimation方法来自View，归属于视图动画
        //属性动画：
        val animator: Animator? = null
    }

    fun numberPickerTest() {
        var minValue: Int = 25
        val numberPicker: NumberPicker = findViewById(R.id.number_picker)
        numberPicker.minValue = 60
        numberPicker.maxValue = 100
        numberPicker.value = 80
        numberPicker.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
                minValue = newVal
                Toast.makeText(
                    this@OtherUIModuleActivity,
                    "您选择的最低价格是：$minValue",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    /**
     * 通知
     */
    fun notificationPractice() {
        //1.调用getSystemService(Context.NOTIFICATION_SERVICE)方法获取系统的NotificationManager服务
        //程序一般通过NotificationManager服务来发送Notification
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name: String = "测试Channel"//设置通知Channel的名字
        //2.创建NotificationChannel对象，并在NotificationManager上创建该Channel对象
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android8.0加入了通知Channel帮助用户来统一管理通知
            val channel: NotificationChannel =
                NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
            channel.description = "测试Channel的描述信息"//设置通知Channel的描述信息
            channel.enableLights(true)//设置通知出现时的闪光灯
            channel.lightColor = Color.RED
            channel.enableVibration(true)//设置通知出现时震动
            channel.vibrationPattern = longArrayOf(0, 50, 100, 150)
//            channel.setSound()
            notificationManager.createNotificationChannel(channel)

        } else {
            TODO()
        }
    }

    fun send(view: View) {
        val intent: Intent = Intent(this@OtherUIModuleActivity, PracticeViewActivity::class.java)
        val pi: PendingIntent = PendingIntent.getActivity(this@OtherUIModuleActivity, 0, intent, 0)
        val p = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Person.Builder()
                .setName("孙悟空")
                .setIcon(Icon.createWithResource(this, R.mipmap.hanfei))
                .build()
        } else {
            TODO("VERSION.SDK_INT < P")
        }
        //设置通知参与者
        val messageStyle: Notification.MessagingStyle = Notification.MessagingStyle(p)
        messageStyle.conversationTitle = "一条新通知"//设置消息标题
        val message: Notification.MessagingStyle.Message =
            Notification.MessagingStyle.Message("恭喜您", System.currentTimeMillis(), p)//创建一条消息
//        message.setData("image/jpeg", Uri.parse())//设置额外的数据
        messageStyle.addMessage(message)//添加一条消息

        val notify: Notification = Notification.Builder(this, CHANNEL_ID)
            .setAutoCancel(true)//设置打开该通知，该通知自动消失
            .setSmallIcon(R.mipmap.hanfei)//设置通知的图标
            .setStyle(messageStyle)
            .setContentIntent(pi)//设置通知将要启动程序的Intent
            .build()
        notificationManager.notify(NOTIFICATION_ID, notify)//发送通知
    }

    fun del(view: View) {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    fun send2(view: View) {
        val notificationUtils: NotificationUtils = NotificationUtils(this)
        val notificationManager2 = notificationUtils.getNotificationManager(this)
        val notification = notificationUtils.getNotification(
            notificationManager2,
            notificationUtils.CHANNEL_ID,
            notificationUtils.CHANNEL_NAME
        )
        notificationManager2.notify(notificationUtils.NOTIFICATION_ID, notification)
    }


    /**
     * 搜索框(SearchView)的功能与用法
     */
    fun searchViewPractice() {
        val searchView: SearchView = findViewById(R.id.searchView)
        val listView: ListView = findViewById(R.id.listView)
        val mStrings = arrayOf("aaaaa", "bbbbb", "ccccc")
        val adapter = ArrayAdapter(this, R.layout.item_array, mStrings)
        listView.adapter = adapter
        listView.isTextFilterEnabled = true//设置ListView启用过滤
        searchView.isIconifiedByDefault = false//设置该SearchView默认是否自动缩小为图标
        searchView.isSubmitButtonEnabled = true//设置该SearchView显示搜索按钮
        searchView.queryHint = "查找"//设置该SearchView内默认显示的提示文本
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            //单击搜索按钮时触发该方法
            override fun onQueryTextSubmit(query: String?): Boolean {
                //实际应用中应该在该方法内执行实际查询
                //此处仅使用Toast显示用户输入的查询内容
                Toast.makeText(this@OtherUIModuleActivity, "您的选择是：$query", Toast.LENGTH_SHORT)
                    .show()
                return false
            }

            //用户输入字符时触发该方法
            override fun onQueryTextChange(newText: String?): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    listView.clearTextFilter()//清除ListView的过滤
                } else {
                    listView.setFilterText(newText)//使用用户输入的内容对ListView的列表项进行过滤
                }
                return true
            }
        })
    }


    fun datePickerAndtimePicker() {
        val datePicker: DatePicker = findViewById(R.id.datePicker)
        val timePicker: TimePicker = findViewById(R.id.timePicker)
        //获取当前的年，月，日，小时，分钟
        val calendar: Calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var hour = calendar.get(Calendar.HOUR)
        var minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        //初始化 datePicker 组件，初始化时指定监听器
        datePicker.init(year, month, day, object : DatePicker.OnDateChangedListener {
            override fun onDateChanged(
                view: DatePicker?,
                year1: Int,
                monthOfYear1: Int,
                dayOfMonth1: Int,
            ) {
                year = year1
                month = monthOfYear1
                day = dayOfMonth1
                Toast.makeText(
                    this@OtherUIModuleActivity,
                    "日期为：" + year1 + "年" + (monthOfYear1 + 1) + "月" + dayOfMonth1 + "日" + hour + "时" +
                            minute + "分" + second + "秒",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        //为timePicker指定监听器
        timePicker.setOnTimeChangedListener(object : TimePicker.OnTimeChangedListener {
            override fun onTimeChanged(view: TimePicker?, hourOfDay1: Int, minute1: Int) {
                hour = hourOfDay1
                minute = minute1
                Toast.makeText(
                    this@OtherUIModuleActivity,
                    "日期为：" + year + "年" + (month + 1) + "月" + day + "日" + hour + "时" +
                            minute + "分" + second + "秒",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    /**
     * 日历视图
     */
    fun calendarViewPractice() {
        val calendarView: CalendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener(object : CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(
                view: CalendarView,
                year: Int,
                month: Int,
                dayOfMonth: Int,
            ) {
                Toast.makeText(
                    this@OtherUIModuleActivity,
                    "你的生日是：" + year + "年" + (month + 1) + "月" + dayOfMonth + "日",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    fun toastPractice() {
        val btnSimpleToast: Button = findViewById(R.id.btn_simple_toast)
        btnSimpleToast.setOnClickListener {
            Toast.makeText(this, "简单的信息提示", Toast.LENGTH_SHORT).show()
        }
        val btnImgToast: Button = findViewById(R.id.btn_img_toast)
        btnImgToast.setOnClickListener {
            val toast: Toast = Toast(this@OtherUIModuleActivity)
            val ll: LinearLayout = LinearLayout(this@OtherUIModuleActivity)
            ll.orientation = LinearLayout.VERTICAL
            val imageView = ImageView(this@OtherUIModuleActivity)
            imageView.setImageResource(R.mipmap.hanfei)
            ll.addView(imageView)
            val textView = TextView(this@OtherUIModuleActivity)
            textView.text = "带图片的提示信息"
            textView.setTextColor(Color.BLUE)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            ll.addView(textView)
            toast.view = ll//Android11上过时
            toast.duration = Toast.LENGTH_SHORT
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}