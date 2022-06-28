package com.jlpay.kotlindemo.study_jetpack

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jlpay.kotlindemo.R

/**
 * ViewModel
 * 在横竖屏切换的时候，就是保证数据的稳定性；
 */
class JetpackViewModelActivity : AppCompatActivity() {

    private val TAG: String = this::class.java.simpleName
    private var numCommon: Int = 0
    private lateinit var myViewModel: MyViewModel
    private lateinit var tvViewmodel: TextView
    private lateinit var tvCommon: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "JetpackViewModelActivity：\tonCreate")
        setContentView(R.layout.activity_jetpack_viewmodel)
        tvViewmodel = findViewById(R.id.tv_viewmodel)
        tvCommon = findViewById(R.id.tv_common)

        //ViewModel初始化，不能直接new
        myViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MyViewModel::class.java)
    }

    //做横竖屏翻转测试，翻转后ViewModel中的num不会销毁重建，但是numCommon就会重新变为0
    public fun viewModelStudy(view: View) {
        tvViewmodel.setText("使用了ViewModel的TextView：\t${myViewModel.num++}")
        tvCommon.setText("普通的TextView：\t${numCommon++}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "JetpackViewModelActivity：\tonDestroy")
    }
}