package com.jlpay.kotlindemo.study_library.blockcanary

import android.os.Bundle
import android.util.Printer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityBlockcanaryBinding

class BlockCanaryActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityBlockcanaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_blockcanary)
        mBinding.click = OnClickProxy()
        mBinding.lifecycleOwner = this
    }

    class CustomerPrinter : Printer {
        override fun println(x: String?) {
            if (x?.contains(">>>>> Dispatching to ") == true) {
                //这是开始
            }
            if (x?.contains("<<<<< Finished to ") == true) {
                //这是结束
            }
        }
    }


    inner class OnClickProxy {
        fun blockCanaryClick(view: View) {
//            BlockCanaryContext
            Toast.makeText(this@BlockCanaryActivity, "hhh", Toast.LENGTH_SHORT).show()
        }
    }
}