package com.jlpay.kotlindemo.study_jetpack.mvvm6

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityViewmodelScopeBinding

class ViewModelScopeActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityViewmodelScopeBinding
    private val TAG: String = ViewModelScopeActivity::class.java.simpleName

    //委托实现，本质也是使用ViewModelProvider
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityViewmodelScopeBinding>(
            this,
            R.layout.activity_viewmodel_scope
        )
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mainViewModel
        mBinding.onClickProxy = OnViewClickProxy()
        mBinding.textView3.text = ""
    }


    inner class OnViewClickProxy {
        fun onClickTest(view: View) {
            mainViewModel.getUser2("chaozhouzhang", "123456")
//            Log.e(TAG, "onClickTest: ")
        }
    }
}