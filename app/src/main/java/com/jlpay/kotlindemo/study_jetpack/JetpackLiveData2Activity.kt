package com.jlpay.kotlindemo.study_jetpack

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

class JetpackLiveData2Activity : AppCompatActivity() {

    private val TAG: String = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livedata2_jetpack)

//        liveDataScene6()
//        liveDataScene7()
        liveDataScene8()
    }

    //LiveData使用场景6，暴露liveData的数据粘性问题，跨Activity观察数据
    private fun liveDataScene6() {
        //先改变数据，在上一个Activity
        //后订阅，监听到前面发生的数据变化，liveData粘性问题
        MyLiveData.info1.observe(this) {
            Log.e(TAG, "数据发生改变啦:$it ")
            Toast.makeText(this, "数据发生改变啦:$it", Toast.LENGTH_SHORT).show()
        }
    }

    //LiveData使用场景7，解决liveData(跨Activity观察数据)的数据粘性问题，不行，还是有问题
    private fun liveDataScene7() {
        MySingleLiveData.info1.observe(this) {
            Log.e(TAG, "数据发生改变啦:$it")
            Toast.makeText(this, "数据发生改变啦:$it", Toast.LENGTH_SHORT).show()
        }
    }

    //LiveData使用场景8，解决liveData(跨Activity观察数据)的数据粘性问题，测试OK
    private fun liveDataScene8() {
        MyUnPeekLiveData.info1.observe(this) {
            Log.e(TAG, "数据发生改变啦:$it")
            Toast.makeText(this, "数据发生改变啦:$it", Toast.LENGTH_SHORT).show()
        }
    }

    //LiveData使用场景8，解决liveData(跨Activity观察数据)的数据粘性问题，测试OK
    private fun modifyLiveData8() {
        MyUnPeekLiveData.info1.value = "我是改变后的没有粘性的烟火8"
    }

    public fun btnLiveData2(view: View) {
        modifyLiveData8()
    }
}