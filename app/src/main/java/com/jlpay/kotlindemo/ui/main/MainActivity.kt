package com.jlpay.kotlindemo.ui.main

import android.view.View
import android.widget.Button
import android.widget.Toast
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.base.BaseMediaActivity
import com.jlpay.kotlindemo.ui.widget.CustomDialog
import okhttp3.*
import java.io.IOException

class MainActivity : BaseMediaActivity() {

    override fun getResourceId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
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
        val btnLibtestJava: Button = findViewById(R.id.btn_libtest_java)
        btnLibtestJava.setOnClickListener {
            LibTestJavaActivity.newInstance(this)
        }
        val btnLibtestKotlin: Button = findViewById(R.id.btn_libtest_kotlin)
        btnLibtestKotlin.setOnClickListener {
            LibTestKotlinActivity.newInstance(this)
        }
    }

    override fun initData() {

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
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {

            }
        })
    }
}