package com.jlpay.kotlindemo.ui.main.jetpack.mvvm.learn2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityWanBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * MVVM+Kotlin协程+JetPack(ViewModel+LiveData)+Retrofit的小DEMO:
 * https://juejin.cn/post/6854573211363999757
 *
 * Kotlin协程：
 * 目的：解决异步回调地狱，使用链式调用结构，类似RxJava（可以理解是RxJava的替代）
 * 异步回调就是代码的多线程顺序执行，而Kotlin协程可以实现顺序编写异步代码，自动进行线程切换。
 * 在并行的执行中，异步回调其实就是代码的多线程顺序执行。 Kotlin协程可以实现既按照顺序的方式编写代码，又可以让代码在不同的线程顺序执行，自动完成线程的切换工作
 *
 * 协程的类型:
 * CoroutineScope：是协程范围，指的是协程内的代码运行的时间周期范围，如果超出了指定的协程范围，协程会被取消执行。
 * GlobalScope：指的是与应用进程相同的协程范围，也就是在进程没有结束之前协程内的代码都可以运行。
 * <p>
 * JetPack中提供的生命周期感知型协程范围：
 * ViewModelScope，为应用中的每个 ViewModel 定义了 ViewModelScope。如果 ViewModel 已清除，则在此范围内启动的协程都会自动取消。
 * LifecycleScope，为每个 Lifecycle 对象定义了 LifecycleScope。在此范围内启动的协程会在 Lifecycle 被销毁时取消。
 * 使用 LiveData 时，可能需要异步计算值。可以使用 liveData 构建器函数调用 suspend 函数，并将结果作为 LiveData 对象传送。
 */
class WanActivity : AppCompatActivity() {

    private lateinit var mViewModel: WanViewModel

    private lateinit var mBinding: ActivityWanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView<ActivityWanBinding>(this, R.layout.activity_wan)
        mBinding.presenter = Presenter()


        GlobalScope.launch {
            delay(1000L)
            Log.e("WanActivity", "Hello, WanActivity")
        }

        //获取ViewModel
        mViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            ).get(
                WanViewModel::class.java
            )

        //对加载状态进行动态观察
//        mViewModel.loadState.observe(this, object : Observer<LoadStateBean> {
//            override fun onChanged(t: LoadStateBean?) {
//                when (t) {// is 运算符类似于Java中的 instanceof 关键字的用法
//                    is LoadStateBean.Success -> mBinding.button.isEnabled = true
//                    is LoadStateBean.Fail -> {
//                        mBinding.button.isEnabled = true
//                        Toast.makeText(this@WanActivity, t.msg, Toast.LENGTH_SHORT).show()
//                    }
//                    is LoadStateBean.Loading -> {
//                        mBinding.button.isEnabled = false
//                    }
//                }
//            }
//        })

        //转Lambda写法
        mViewModel.loadState.observe(this,
            Observer<LoadStateBean> {
                when (it) {
                    is LoadStateBean.Success ->
                        mBinding.button.isEnabled = true
                    is LoadStateBean.Fail -> {
                        mBinding.button.isEnabled = true
                        Toast.makeText(this@WanActivity, it.msg, Toast.LENGTH_SHORT).show()
                    }
                    is LoadStateBean.Loading -> {
                        mBinding.button.isEnabled = false
                    }
                }
            })


        //对User数据进行观察
//        mViewModel.user.observe(this, object : Observer<BaseResponse<UserBean>> {
//            override fun onChanged(t: BaseResponse<UserBean>) {
//                if (t.errorCode == 0) {
//                    Toast.makeText(this@WanActivity, t.data?.nickname, Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this@WanActivity, t.errorMsg, Toast.LENGTH_SHORT).show()
//                }
//            }
//        })


        //转Lambda写法
        mViewModel.user.observe(this, Observer {
            if (it.errorCode == 0) {
                Toast.makeText(this@WanActivity, it.data?.nickname, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@WanActivity, it.errorMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }


    inner class Presenter {
        fun onClick(view: View) {
            when (view.id) {
                R.id.button -> {//点击登录按钮登录
                    mViewModel.login("chaozhouzhang", "123456")
                }
            }
        }
    }
}