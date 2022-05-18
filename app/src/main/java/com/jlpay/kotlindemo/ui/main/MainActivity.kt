package com.jlpay.kotlindemo.ui.main

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.android_study.*
import com.jlpay.kotlindemo.java_study.*
import com.jlpay.kotlindemo.kotlin_study.CoroutinesActivity
import com.jlpay.kotlindemo.kotlin_study.HigherAndExpandFuncActivity
import com.jlpay.kotlindemo.kotlin_study.KotlinTestActivity
import com.jlpay.kotlindemo.ui.base.BaseMediaActivity
import com.jlpay.kotlindemo.android_study.MediaPlayerActivity
import com.jlpay.kotlindemo.android_study.ConfigurationActivity
import com.jlpay.kotlindemo.android_study.EventListenerActivity
import com.jlpay.kotlindemo.android_study.AssetAndRawResActivity
import com.jlpay.kotlindemo.android_study.ui.*
import com.jlpay.kotlindemo.ui.main.dailytest.*
import com.jlpay.kotlindemo.ui.main.hencoder.IOActivity
import com.jlpay.kotlindemo.ui.main.hencoder.mvc.MvcDemoActivity
import com.jlpay.kotlindemo.ui.main.hencoder.mvcadvance.MvcAdvanceDemoActivity
import com.jlpay.kotlindemo.ui.main.hencoder.mvp.MvpDemoActivity
import com.jlpay.kotlindemo.ui.main.hencoder.mvvm.MvvmDemoActivity
import com.jlpay.kotlindemo.java_study.GenericTypeActivity
import com.jlpay.kotlindemo.ui.main.jetpack.mvvm.learn1.ImageMvvmActivity
import com.jlpay.kotlindemo.ui.main.jetpack.mvvm.learn2.WanActivity
import com.jlpay.kotlindemo.ui.main.jetpack.mvvm.learn4.MyLoginActivity
import com.jlpay.kotlindemo.ui.main.jlpay.JlLibraryDebugActivity
import com.jlpay.kotlindemo.ui.main.rxjava.RxAutoDisposeActivity
import com.jlpay.kotlindemo.ui.main.rxjava.RxJavaActivity
import com.jlpay.kotlindemo.ui.main.rxjava.RxLifecycleActivity
import com.jlpay.kotlindemo.ui.utils.Utils
import com.jlpay.kotlindemo.ui.widget.CustomDialog
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
        val btnHandler: Button = findViewById(R.id.btn_handler)
        btnHandler.setOnClickListener {
            HandlerActivity.newInstance(this)
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
        val btnThread: Button = findViewById(R.id.btn_thread)
        btnThread.setOnClickListener {
            ThreadActivity.newInstance(this)
        }
        val btnIo: Button = findViewById(R.id.btn_io)
        btnIo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                IOActivity.newInstance(this@MainActivity)
            }
        })
        val btnRxjava2: Button = findViewById(R.id.btn_rxjava2)
        btnRxjava2.setOnClickListener {
            RxJavaActivity.newInstance(this)
        }
        val btnRxlifecycle: Button = findViewById(R.id.btn_rxlifecycle)
        btnRxlifecycle.setOnClickListener {
            RxLifecycleActivity.newInstance(this)
        }
        val btnRxautodispose: Button = findViewById(R.id.btn_rxautodispose)
        btnRxautodispose.setOnClickListener {
            RxAutoDisposeActivity.newInstance(this)
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
        addButton("ImageMvvmActivity", ImageMvvmActivity::class.java)
        addButton("WanActivity", WanActivity::class.java)
        addButton("KotlinTestActivity", KotlinTestActivity::class.java)
        addButton("SimpleServiceActivity", SimpleServiceActivity::class.java)
        addButton("IntentServiceActivity", IntentServiceActivity::class.java)
        addButton("AidlServiceActivity", AidlServiceActivity::class.java)
        addButton("CustomAidlServiceActivity", CustomAidlServiceActivity::class.java)
        addButton("MediaUtilsActivity", MediaUtilsActivity::class.java)
        addButton("BroadcastReceiverActivity", BroadcastReceiverActivity::class.java)
        addButton("ImagePickerActivity", ImagePickerActivity::class.java)
        addButton("SmaliDexLibActivity", SmaliDexLibActivity::class.java)
        addButton("AssetAndRawResActivity", AssetAndRawResActivity::class.java)
        addButton("MediaPlayerActivity", MediaPlayerActivity::class.java)
        addButton("GenericTypeActivity", GenericTypeActivity::class.java)
        addButton("PdfViewerActivity", PdfViewerActivity::class.java)
        addButton("FileManagerActivity", FileManagerActivity::class.java)
        addButton("SmallFileManagerActivity", SmallFileManagerActivity::class.java)
        addButton("DataBindingActivity", DataBindingActivity::class.java)
        addButton("DynamicLayoutActivity", DynamicLayoutActivity::class.java)
        addButton(
            "BaseRecyclerViewAdapterHelperActivity",
            BaseRecyclerViewAdapterHelperActivity::class.java
        )
        addButton("MyLoginActivity", MyLoginActivity::class.java)
        addButton("LifeCycleActivity", LifeCycleActivity::class.java)
        addButton("ViewPager2TabActivity", ViewPager2TabActivity::class.java)
        addButton("ListNodeActivity", ListNodeActivity::class.java)
        addButton("ReflectActivity", ReflectActivity::class.java)
        addButton("ReferenceActivity", ReferenceActivity::class.java)
        addButton("LruCacheActivity", LruCacheActivity::class.java)
        addButton("OcrActivity", JlLibraryDebugActivity::class.java)
        addButton("Kotlin协程：CoroutinesActivity", CoroutinesActivity::class.java)
        addButton("Kotlin高阶函数和扩展函数", HigherAndExpandFuncActivity::class.java)
        addButton("Java基础：集合", CollectionActivity::class.java)
        addButton("Android基础：View的绘制流程", ViewDrawingProcessActivity::class.java)
        addButton("Android基础：View的事件传递机制", ViewEventDispatchActivity::class.java)
        addButton("华为笔试刷题", HuaWeiTestActivity::class.java)
        addButton("Android动画", AnimatorActivity::class.java)
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