package com.jlpay.kotlindemo.ui.main.dailytest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityViewpager2TabBinding
import com.jlpay.kotlindemo.databinding.ItemTabLayoutBinding

class ViewPager2TabActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityViewpager2TabBinding
    private lateinit var fragments: ArrayList<Fragment>
    private val tabTitles: List<String> = arrayListOf<String>("微信", "通讯录", "发现", "我")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView<ActivityViewpager2TabBinding>(
            this,
            R.layout.activity_viewpager2_tab
        )
        mBinding.lifecycleOwner = this

        initView()
    }

    private fun initView() {
        fragments = ArrayList()
        for (tabTitle in tabTitles) {
            fragments.add(LifeCycleFragment.newInstance(tabTitle))
            //为TabLayout添加tab,也可以在xml布局中以 TabItem 方式添加
//            mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(tabTitle))
            val tabCustomView = DataBindingUtil.inflate<ItemTabLayoutBinding>(
                layoutInflater,
                R.layout.item_tab_layout,
                mBinding.tabLayout,
                false
            )
            tabCustomView.tvContent.text = tabTitle
            val newTab = mBinding.tabLayout.newTab()
            newTab.customView = tabCustomView.root
            mBinding.tabLayout.addTab(newTab)
        }

//        mBinding.tabLayout.addOnTabSelectedListener(object :
//            TabLayout.ViewPagerOnTabSelectedListener(mBinding.viewpager2))
    }
}