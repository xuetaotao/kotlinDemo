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
 *
 * 经过测试，SingleLiveData 无法解决粘性问题，原因待定；UnPeekLiveData可以解决粘性问题
 *
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
//        initLiveDataObserver2()
//        liveDataScene3()
//        liveDataScene4()
//        liveDataScene5()
    }

    //LiveData使用场景1，数据监听
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

    //LiveData使用场景1，数据监听
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

    //LiveData使用场景3，暴露liveData的数据粘性问题
    private fun liveDataScene3() {
        //先订阅再改变，会监听到发生的改变，这是我们的常识
        //先改变再订阅，也会监听到之前发生的改变，这就是粘性
        modifyLiveData3()
        initLiveDataObserver3()
    }

    //LiveData使用场景3，暴露liveData的数据粘性问题
    private fun initLiveDataObserver3() {
        MyLiveData.info1.observe(this) {
            Log.e(TAG, "数据发生改变啦:$it ")
            Toast.makeText(this, "数据发生改变啦:$it", Toast.LENGTH_SHORT).show()
        }
    }

    //LiveData使用场景3，暴露liveData的数据粘性问题
    private fun modifyLiveData3() {
        MyLiveData.info1.value = "我就是不一样的烟火3"
    }

    //LiveData使用场景4，解决liveData的数据粘性问题，不行，还是有问题
    private fun liveDataScene4() {
        //先改变
        val mySingleLiveData = SingleLiveData<String>()
        mySingleLiveData.value = "我是没有粘性的烟火4"
        //后订阅
        mySingleLiveData.observe(this) {
            Log.e(TAG, "数据发生改变啦:$it ")
            Toast.makeText(this, "数据发生改变啦:$it", Toast.LENGTH_SHORT).show()
        }
    }

    //LiveData使用场景5，解决liveData的数据粘性问题，测试OK
    val unPeekLiveData = UnPeekLiveData<String>()
    private fun liveDataScene5() {
        //先改变
        unPeekLiveData.value = "我是没有粘性的烟火5"
        //后订阅
        unPeekLiveData.observe(this) {
            Log.e(TAG, "数据发生改变啦:$it ")
            Toast.makeText(this, "数据发生改变啦:$it", Toast.LENGTH_SHORT).show()
        }
    }

    //LiveData使用场景5，解决liveData的数据粘性问题，测试OK
    private fun modifyLiveData5() {
        unPeekLiveData.value = "我是改变后的没有粘性的烟火5"
    }

    //LiveData使用场景6，暴露liveData的数据粘性问题，跨Activity观察数据
    private fun liveDataScene6() {
        //先改变数据
        MyLiveData.info1.value = "我就是不一样的烟火6"
        //只会以最后一次数据作为粘性数据
        MyLiveData.info1.value = "我就是不一样的烟火66"
        startActivity(Intent(this, JetpackLiveData2Activity::class.java))
    }

    //LiveData使用场景7，解决liveData(跨Activity观察数据)的数据粘性问题，不行，还是有问题
    private fun liveDataScene7() {
        //先改变数据
        MySingleLiveData.info1.value = "我就是不一样的烟火7"
        startActivity(Intent(this, JetpackLiveData2Activity::class.java))
    }

    //LiveData使用场景8，解决liveData(跨Activity观察数据)的数据粘性问题，测试OK
    private fun liveDataScene8() {
        //先改变数据
        MyUnPeekLiveData.info1.value = "我就是不一样的烟火8"
        startActivity(Intent(this, JetpackLiveData2Activity::class.java))
    }

    inner class OnClickProxy {
        fun onClick(view: View) {
            when (view.id) {
                R.id.btn_livedata -> {
//                    Toast.makeText(this@JetpackLiveDataActivity, "LiveData", Toast.LENGTH_SHORT)
//                        .show()
//                    modifyLiveData()
//                    modifyLiveData2()
//                    modifyLiveData5()
//                    liveDataScene6()
//                    liveDataScene7()
                    liveDataScene8()
                }
            }
        }
    }
}