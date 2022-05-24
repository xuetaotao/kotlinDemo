package com.jlpay.kotlindemo.study_jetpack.mvvm2

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityImageMvvmBinding

/**
 * View层：
 *
 * View层由Activity/Fragment/自定义View组成。
 *
 * 在View层中，View不做具体的数据和逻辑处理操作，View层只会将数据和事件操作和ViewModel进行绑定。
 *
 * View 监听数据的变化，当ViewModel中的LiveData数据发生变化时，进行Ui的更新操作。同时，View层将事件绑定以命令的方式交给ViewModel去处理。
 *
 * 总之，View层只做UI相关操作，逻辑处理数据获取等交给ViewModel
 *
 * <p>
 * 参考资料:
 * 1.Mvvm模式: Databinding 与 ViewModel+LiveData+Repository: https://www.jianshu.com/p/e7628d6e6f61/
 * 2.Android官方架构组件ViewModel+LiveData+DataBinding架构属于自己的MVVM: https://www.cnblogs.com/dev-njp/p/8783341.html
 */
class ImageMvvmActivity : AppCompatActivity() {

    //数据绑定对象，用来实现数据绑定
    private lateinit var mBinding: ActivityImageMvvmBinding

    //用来获取数据，实现与数据层的解耦
    private lateinit var mViewModel: ImageViewModel

    //用来弹出加载提示框
    private lateinit var mProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView<ActivityImageMvvmBinding>(
            this,
            R.layout.activity_image_mvvm
        )

        mViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                ImageViewModel::class.java
            )

        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("加载中")

        //LiveData可以理解为带有Activity生命周期的Observable = RxLifecycle(或者AutoDispose) + RxJava的Observable
        mViewModel.getImage().observe(this, object : Observer<Data<Image>> {
            override fun onChanged(t: Data<Image>?) {
                if (t?.errorMsg != null) {
                    Toast.makeText(this@ImageMvvmActivity, t.errorMsg, Toast.LENGTH_SHORT).show()
                    mProgressDialog.dismiss()
                    return
                }
                mBinding.imageBean = t?.data
                title = t?.data?.copyright
                mProgressDialog.dismiss()
            }
        })

        mBinding.presenter = Presenter()
        mProgressDialog.show()
        mViewModel.loadImage()

        //不监听LiveData，通过DataBinding实现页面更新的方式
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this
    }

    override fun onStop() {
        super.onStop()
        mViewModel.getNoObserveLiveData().postValue("不需要observe的LiveData")
    }

    inner class Presenter {
        fun onClick(view: View) {
            mProgressDialog.show()
            when (view.id) {
                R.id.btn_previous -> {
                    mViewModel.previousImage()
                }
                R.id.btn_load -> mViewModel.loadImage()
                R.id.btn_next -> mViewModel.nextImage()
                else -> Toast.makeText(this@ImageMvvmActivity, "没有绑定视图操作", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}