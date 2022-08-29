package com.jlpay.kotlindemo.daily

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.base.BaseMediaActivity
import com.jlpay.kotlindemo.daily.practice.*
import com.jlpay.kotlindemo.study_android.*
import com.jlpay.kotlindemo.study_android.ui.*
import com.jlpay.kotlindemo.study_java.*
import com.jlpay.kotlindemo.study_jetpack.JetpackDataBindingActivity
import com.jlpay.kotlindemo.study_jetpack.JetpackLifeCycleActivity
import com.jlpay.kotlindemo.study_jetpack.JetpackLiveDataActivity
import com.jlpay.kotlindemo.study_jetpack.JetpackViewModelActivity
import com.jlpay.kotlindemo.study_jetpack.mvc.MvcDemoActivity
import com.jlpay.kotlindemo.study_jetpack.mvcadvance.MvcAdvanceDemoActivity
import com.jlpay.kotlindemo.study_jetpack.mvp.MvpDemoActivity
import com.jlpay.kotlindemo.study_jetpack.mvvm1.MvvmDemoActivity
import com.jlpay.kotlindemo.study_jetpack.mvvm2.ImageMvvmActivity
import com.jlpay.kotlindemo.study_jetpack.mvvm3.WanActivity
import com.jlpay.kotlindemo.study_jetpack.mvvm5.MyLoginActivity
import com.jlpay.kotlindemo.study_kotlin.CoroutinesActivity
import com.jlpay.kotlindemo.study_kotlin.HigherAndExpandFuncActivity
import com.jlpay.kotlindemo.study_kotlin.KotlinTestActivity
import com.jlpay.kotlindemo.study_library.baserecyclerviewadapterhelper.BaseRecyclerViewAdapterHelperActivity
import com.jlpay.kotlindemo.study_library.leakcanary.LeakCanaryActivity
import com.jlpay.kotlindemo.study_library.rxjava.RxAutoDisposeActivity
import com.jlpay.kotlindemo.study_library.rxjava.RxJavaActivity
import com.jlpay.kotlindemo.study_library.rxjava.RxLifecycleActivity
import com.jlpay.kotlindemo.study_library.tinker.SmaliDexLibActivity
import com.jlpay.kotlindemo.utils.Utils
import com.jlpay.kotlindemo.widget.CustomDialog
import okhttp3.*
import java.io.IOException

class MainActivity : BaseMediaActivity() {

    private lateinit var llMain: LinearLayout

    override fun getResourceId(): Int {
        return R.layout.activity_main
    }

    /**
     * TODO 这里后面考虑做个注解处理器进行优化
     * 每新建一个 Activity 自动添加到这里
     */
    override fun initView() {
        llMain = findViewById(R.id.ll_main)
        val btnLinearlayout: Button = findViewById(R.id.btn_linearLayout)
        btnLinearlayout.setOnClickListener {
            LinearLayoutActivity.newInstance(this)
        }
        val btnFramelayout: Button = findViewById(R.id.btn_framelayout)
        btnFramelayout.setOnClickListener {
            FrameLayoutActitivy.newInstance(this)
        }
        val btnConstraintlayout: Button = findViewById(R.id.btn_constraintlayout)
        btnConstraintlayout.setOnClickListener {
            ConstraintLayoutActivity.newInstance(this)
        }
        val btnPracticeview: Button = findViewById(R.id.btn_practiceview)
        btnPracticeview.setOnClickListener {
            PracticeViewActivity.newInstance(this)
        }
        val btnImageview: Button = findViewById(R.id.btn_imageview)
        btnImageview.setOnClickListener {
            ImageViewActivity.newInstance(this)
        }
        val btnAdapterview: Button = findViewById(R.id.btn_adapterview)
        btnAdapterview.setOnClickListener {
            AdapterViewActivity.newInstance(this)
        }
        val btnRecyclerview: Button = findViewById(R.id.btn_recyclerView)
        btnRecyclerview.setOnClickListener {
            RecyclerViewActivity.newInstance(this)
        }
        val btnProgressbar: Button = findViewById(R.id.btn_progressBar)
        btnProgressbar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ProgressBarActivity.newInstance(this@MainActivity)
            }
        })
        val btnViewswitcher: Button = findViewById(R.id.btn_viewSwitcher)
        btnViewswitcher.setOnClickListener {
            ViewSwitcherActivity.newInstance(this)
        }
        val btnImageswitcher: Button = findViewById(R.id.btn_imageSwitcher)
        btnImageswitcher.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ImageSwitcherActivity.newInstance(this@MainActivity)
            }
        })
        val btnViewflipper: Button = findViewById(R.id.btn_viewFlipper)
        btnViewflipper.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ViewFlipperActivity.newInstance(this@MainActivity)
            }
        })
        val btnOtherUiModule: Button = findViewById(R.id.btn_other_ui_module)
        btnOtherUiModule.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                OtherUIModuleActivity.newInstance(this@MainActivity)
            }
        })
        val btnDialog: Button = findViewById(R.id.btn_dialog)
        btnDialog.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                DialogActivity.newInstance(this@MainActivity)
            }
        })
        val btnMenu: Button = findViewById(R.id.btn_menu)
        btnMenu.setOnClickListener {
            MenuActivity.newInstance(this)
        }
        val btnXmlMenu: Button = findViewById(R.id.btn_xml_menu)
        btnXmlMenu.setOnClickListener {
            XmlMenuActivity.newInstance(this)
        }
        val btnActionbar: Button = findViewById(R.id.btn_actionBar)
        btnActionbar.setOnClickListener {
            ActionBarActivity.newInstance(this)
        }
        val btnPlaneview: Button = findViewById(R.id.btn_planeView)
        btnPlaneview.setOnClickListener {
            PlaneViewActivity.newInstance(this@MainActivity)
        }
        val btnEventlistener: Button = findViewById(R.id.btn_eventListener)
        btnEventlistener.setOnClickListener {
            EventListenerActivity.newInstance(this)
        }
        val btnConfiguration: Button = findViewById(R.id.btn_configuration)
        btnConfiguration.setOnClickListener {
            ConfigurationActivity.newInstance(this)
        }
        val btnAsynctask: Button = findViewById(R.id.btn_asyncTask)
        btnAsynctask.setOnClickListener {
            Utils.launchActivity(this, AsyncTaskActivity::class.java)
        }
        val btnPermission: Button = findViewById(R.id.btn_permission)
        btnPermission.setOnClickListener {
            openPhotoAlbum()
//            takePhoto()
        }
        val btnQrcode: Button = findViewById(R.id.btn_qrCode)
        btnQrcode.setOnClickListener {
            QRCodeActivity.newInstance(this)
        }
        val btnLibtestJava: Button = findViewById(R.id.btn_libtest_java)
        btnLibtestJava.setOnClickListener {
            LibTestJavaActivity.newInstance(this)
        }
        val btnLibtestKotlin: Button = findViewById(R.id.btn_libtest_kotlin)
        btnLibtestKotlin.setOnClickListener {
            LibTestKotlinActivity.newInstance(this)
        }
        addButton("MvcDemo", MvcDemoActivity::class.java)
        addButton("MvpDemo", MvpDemoActivity::class.java)
        addButton("MvcAdvanceDemo", MvcAdvanceDemoActivity::class.java)
        addButton("MvvmDemo", MvvmDemoActivity::class.java)
        addButton("WanActivity", WanActivity::class.java)
        addButton("MediaUtilsActivity", MediaUtilsActivity::class.java)
        addButton("ImagePickerActivity", ImagePickerActivity::class.java)
        addButton("SmaliDexLibActivity", SmaliDexLibActivity::class.java)
        addButton("AssetAndRawResActivity", AssetAndRawResActivity::class.java)
        addButton("DynamicLayoutActivity", DynamicLayoutActivity::class.java)
        addButton(
            "BaseRecyclerViewAdapterHelperActivity",
            BaseRecyclerViewAdapterHelperActivity::class.java
        )
        addButton("ListNodeActivity", ListNodeActivity::class.java)
        addButton("LruCacheActivity", LruCacheActivity::class.java)
        addButton("OcrActivity", JlLibraryDebugActivity::class.java)

        //Java基础
        addButton("Java基础：并发编程", ThreadActivity::class.java)
        addButton("Java基础：集合", CollectionActivity::class.java)
        addButton("Java基础：泛型--占坑", GenericTypeActivity::class.java)
        addButton("Java基础：反射", ReflectActivity::class.java)
        addButton("Java基础：类加载机制", ClassLoaderActivity::class.java)
        addButton("Java基础：动态代理", DynamicProxyActivity::class.java)
        addButton("Java基础：IO流", IOActivity::class.java)
        addButton("Java基础：强/软/弱/虚四大引用", ReferenceActivity::class.java)
        addButton("Java基础：代码中执行命令行命令", CommandLineActivity::class.java)

        //Kotlin基础；TODO(扩展函数)
        addButton("Kotlin基础：Kotlin基本使用", KotlinTestActivity::class.java)
        addButton("Kotlin基础：高阶函数和扩展函数", HigherAndExpandFuncActivity::class.java)
        addButton("Kotlin协程", CoroutinesActivity::class.java)

        //Android基础
        addButton("Android基础：文件管理器(未完成)", FileManagerActivity::class.java)
        addButton("Android基础：当前APK-外部私有目录的-文件管理器", SmallFileManagerActivity::class.java)
        addButton("Android基础：BroadcastReceiver的基本使用", BroadcastReceiverActivity::class.java)
        addButton("Android基础：Service的基本使用", SimpleServiceActivity::class.java)
        addButton("Android基础：IntentService的使用", IntentServiceActivity::class.java)
        addButton("Android基础：前台Service的使用--音乐播放器通知服务", MusicServiceActivity::class.java)
        addButton("Android基础：远程Service与Aidl的使用", AidlServiceActivity::class.java)
        addButton("Android基础：远程Service与Aidl的使用2", CustomAidlServiceActivity::class.java)
        addButton("Android基础：通知：Notification的使用", NotificationActivity::class.java)
        addButton("Android基础：View的绘制流程", ViewDrawingProcessActivity::class.java)
        addButton("Android基础：View的事件传递机制", ViewEventDispatchActivity::class.java)
        addButton("Android基础：ViewPager与TabLayout联动", ViewPagerTabLayoutActivity::class.java)
        addButton("Android基础：ViewPager2与TabLayout联动", ViewPager2TabActivity::class.java)
        addButton("Android基础：嵌套滑动NestedScrollView学习", NestedScrollActivity::class.java)
        addButton("Android基础：动画", AnimatorActivity::class.java)
        addButton("Android基础：音频播放器(考虑与MusicServiceActivity联姻一下)", MediaPlayerActivity::class.java)

        //Jetpack与MVVM
        addButton("Jetpack：LifeCycle", JetpackLifeCycleActivity::class.java)
        addButton("Jetpack：LiveData", JetpackLiveDataActivity::class.java)
        addButton("Jetpack：DataBinding", JetpackDataBindingActivity::class.java)
        addButton("Jetpack：ViewModel", JetpackViewModelActivity::class.java)
        addButton("MVVM：每日一图", ImageMvvmActivity::class.java)
        addButton("MVVM：简易登录页面", MyLoginActivity::class.java)

        //FrameWork
        addButton("FrameWork：Handler", HandlerActivity::class.java)

        //开源框架
        addButton("开源框架：Pdf阅读器", PdfViewerActivity::class.java)
        addButton("开源框架：LeakCanary", LeakCanaryActivity::class.java)
        addButton("开源框架：RxJava2", RxJavaActivity::class.java)
        addButton("开源框架：RxJava2--RxLifecycle", RxLifecycleActivity::class.java)
        addButton("开源框架：RxJava2--RxAutoDispose", RxAutoDisposeActivity::class.java)

        //算法与数据结构
        addButton("算法与数据结构：华为笔试刷题", HuaWeiTestActivity::class.java)
        addButton("算法与数据结构：数据结构", DataStructActivity::class.java)
    }

    override fun initData() {
//        okHttpTest()
    }


    /**
     * Java 中用?表示通配符，由于参数值是未知类型的容器类，所以只能读取其中元素，不能向其中添加元素， 因为，其类型是未知，所以编译器无法识别添加元素的类型和容器的类型是否兼容，
     * 唯一的例外是NULL
     * Kotlin中用* 表示通配符，又称星号投射，表示你并不知道类型参数的任何信息, 但是仍然希望能够安全地使用它. 这里所谓"安全地使用"是指, 对泛型类型定义一个类型投射,
     * 要求这个泛型类型的所有的实体实例, 都是这个投射的子类型。其实就是*代指了所有类型，相当于Any?
     */
    private fun addButton(clssName: String, clss: Class<*>) {
        val button: Button = Button(this)
        button.isAllCaps = false
        button.setTextColor(resources.getColor(R.color.light_black))
        button.text = clssName
        button.setOnClickListener {
            Utils.launchActivity(this, clss)
        }
        llMain.addView(button)
    }

    fun showCustomDialog() {
        val builder: CustomDialog.Builder = CustomDialog.Builder(this@MainActivity)
        builder
            .setTitle("温馨提示")
            .setTitleVisible(true)
            .setContent("测试一下")
            .setPositiveButton(
                "确定"
            ) { dialog, which ->
                Toast.makeText(this, "好的，确定了", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton(
                "取消了"
            ) { dialog, which ->
                Toast.makeText(this, "那么，取消了", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun okHttpTest() {
        val client: OkHttpClient = OkHttpClient.Builder().build()
        val request: Request = Request.Builder()
            .url("http://www.baidu.com")
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "onFailure: " + Thread.currentThread().name)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.e(
                    "MainActivity",
                    "onResponse: " + Thread.currentThread().name + "\n" + (response.body?.string()
                        ?: "加载失败")
                )
            }
        })
    }
}