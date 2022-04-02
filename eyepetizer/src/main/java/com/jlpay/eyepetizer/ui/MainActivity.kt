package com.jlpay.eyepetizer.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.eyepetizer.R
import com.jlpay.eyepetizer.databinding.ActivityMainBinding
import com.jlpay.eyepetizer.jlpay_library_debug.JlLibraryDebugActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this

        initView()
    }

    private fun initView() {
        addButton("JlLibraryDebugActivity", JlLibraryDebugActivity::class.java)
    }


    fun testBlockCanary(view: View?) {
        SystemClock.sleep(2000)
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
        button.setTextColor(resources.getColor(R.color.blackAlpha60))
        button.text = clssName
        button.setOnClickListener {
            launchActivity(this, clss)
        }
        mBinding.llMain.addView(button)
    }


    /**
     * 启动Activity
     *
     * @param packageContext 上下文
     * @param clss           要启动的
     */
    fun launchActivity(packageContext: Context, clss: Class<*>?) {
        val intent = Intent(packageContext, clss)
        packageContext.startActivity(intent)
    }

}
