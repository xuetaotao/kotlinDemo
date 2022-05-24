package com.jlpay.kotlindemo.library_study.baserecyclerviewadapterhelper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityBaseRecyclerviewAdapterHelperBinding

/**
 * 练习 BaseRecyclerViewAdapterHelper 的使用
 */
class BaseRecyclerViewAdapterHelperActivity : AppCompatActivity() {

    //数据绑定对象，用来实现数据绑定
    private lateinit var mBinding: ActivityBaseRecyclerviewAdapterHelperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_base_recyclerview_adapter_helper)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        mBinding.recyclerview.layoutManager = linearLayoutManager

//        mBinding.recyclerview.adapter =
    }

    class MyAdapter1 : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_base_recyclerview) {
        override fun convert(holder: BaseViewHolder, item: String) {

        }
    }

}