package com.jlpay.kotlindemo.study_jetpack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityLivedataJetpackBinding
import com.jlpay.kotlindemo.service.LiveDataService
import kotlin.concurrent.thread

/**
 * LiveData是一种可观察的数据存储器类，与常规的可观察类不同，LiveData具有生命周期感知能力，
 * 意指它遵循其他应用组件（如Activity/Fragment/Service）的生命周期。这种感知能力可确保
 * LiveData仅更新处于活跃生命周期状态的应用组件观察者。
 *
 * 它既是观察者，也是被观察者.
 *
 * 一般情况下，LiveData要配合ViewModel一起使用，这里作为学习只使用LiveData
 */
class JetpackLiveDataActivity : AppCompatActivity() {

    private val TAG: String = this::class.java.simpleName
    private lateinit var mBinding: ActivityLivedataJetpackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<ActivityLivedataJetpackBinding>(
            this,
            R.layout.activity_livedata_jetpack
        )
        mBinding.lifecycleOwner = this
        mBinding.onClick = OnClickProxy()

//        initLiveDataObserver()
        initLiveDataObserver2()
    }

    //LiveData使用场景1
    private fun initLiveDataObserver() {
        //1.观察者，观察数据变化，Kotlin写法一
        MyLiveData.info1.observe(this) {
            mBinding.tvLivedata.text = it
        }
        //1.观察者，Kotlin写法二
//        MyLiveData.info1.observe(this, object : Observer<String> {
//            override fun onChanged(t: String?) {
//                mBinding.tvLivedata.text = t
//            }
//        })
    }

    //LiveData使用场景1
    private fun modifyLiveData() {
        //2。触发数据改变 环境
        //如果把这部分放在其他的Activity处，就可以实现类似EventBus的效果
        MyLiveData.info1.value = "default"//setValue 只能用在主线程
        //这是Kotlin的线程语法糖
        thread {
            Thread.sleep(3000)
            MyLiveData.info1.postValue("三秒钟后，修改了哦")//postValue 子线程
        }
        thread {
            Thread.sleep(6000)
            MyLiveData.info1.postValue("六秒钟后，修改了哦")//postValue 子线程
        }
    }

    //LiveData使用场景2：模拟微信后台消息推送提示，但是只有界面显示的时候才更新UI
    private fun initLiveDataObserver2() {
        //会检测生命周期，不像Handler.sendMessage()
        MyLiveData.info1.observe(this) {
            Log.e(TAG, "界面可见，说明用户在查看微信列表界面啦，更新消息列表UI界面:${it}")
            Toast.makeText(this, "更新消息列表UI界面成功：${it}", Toast.LENGTH_SHORT).show()
        }
    }

    //LiveData使用场景2：模拟微信后台消息推送提示，但是只有界面显示的时候才更新UI
    private fun modifyLiveData2() {
        startService(Intent(this, LiveDataService::class.java))
        Toast.makeText(this, "推送服务器启动成功", Toast.LENGTH_SHORT).show()
    }

    inner class OnClickProxy {
        fun onClick(view: View) {
            when (view.id) {
                R.id.btn_livedata -> {
//                    Toast.makeText(this@JetpackLiveDataActivity, "LiveData", Toast.LENGTH_SHORT)
//                        .show()
//                    modifyLiveData()
                    modifyLiveData2()
                }
            }
        }
    }
}