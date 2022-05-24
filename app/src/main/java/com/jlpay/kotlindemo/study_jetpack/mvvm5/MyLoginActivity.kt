package com.jlpay.kotlindemo.study_jetpack.mvvm5

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModelProvider
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityMyLoginBinding

/**
 * MVVM搭建一个登录页面
 */
class MyLoginActivity : AppCompatActivity() {


    private val TAG: String? = this.javaClass.simpleName

    //若使用 MyLoginViewModel() 这样的方式初始化的话也可以，但是不会具有ViewModel的特性，如存储数据，生命周期（听人说的，没研究过）
    private lateinit var myLoginViewModel: MyLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //DataBinding初始化
        val mBinding =
            DataBindingUtil.setContentView<ActivityMyLoginBinding>(this, R.layout.activity_my_login)
        mBinding.lifecycleOwner = this

        //初始化 ViewModel
        myLoginViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(MyLoginViewModel::class.java)

        //初始化xml布局中的变量
        mBinding.click = ClickProxy()
        mBinding.vm = myLoginViewModel

        testSomething()
    }

    private fun testSomething() {
        //数据绑定优先选择Observable*类而非 LiveData。如果想对Observable*类的值进行监听，
        // 可以使用Observable*类#addOnPropertyChangedCallback添加回调，如下：
        myLoginViewModel.account.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val accountObservableField = sender as ObservableField<String>
                Log.e(TAG, "onPropertyChanged: ${accountObservableField.get()}")
            }
        })
    }


    inner class ClickProxy {

        fun login(view: View) {
            Toast.makeText(
                this@MyLoginActivity,
                "你点击登陆了:\naccount：${myLoginViewModel.account.get()},\npassword：${myLoginViewModel.password.get()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}