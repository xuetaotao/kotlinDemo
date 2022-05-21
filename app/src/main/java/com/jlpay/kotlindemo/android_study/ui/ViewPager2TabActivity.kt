package com.jlpay.kotlindemo.android_study.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityViewpager2TabBinding
import com.jlpay.kotlindemo.ui.main.dailytest.LifeCycleFragment

/**
 * ViewPager2的使用
 */
class ViewPager2TabActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityViewpager2TabBinding
    private val fragments: ArrayList<Fragment> by lazy { ArrayList() }
    private val tabTitles: List<String> = arrayListOf<String>("微信", "通讯录", "发现", "我")
    private val viewPager2Adapter by lazy { MyViewPager2Adapter() }

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
        for (tabTitle in tabTitles) {
            //方式一：为TabLayout添加tab,也可以在xml布局中以 TabItem 方式添加
            mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(tabTitle))
            //方式二：添加自定义View
//            val tabCustomView = DataBindingUtil.inflate<ItemTabLayoutBinding>(
//                layoutInflater,
//                R.layout.item_tab_layout,
//                mBinding.tabLayout,
//                false
//            )
//            tabCustomView.tvContent.text = tabTitle
//            val newTab = mBinding.tabLayout.newTab()
//            newTab.customView = tabCustomView.root
//            mBinding.tabLayout.addTab(newTab)
        }

        //给ViewPager设置adapter
        mBinding.viewpager2.adapter = viewPager2Adapter

        //设置从 viewpager 到 tabLayout 的关联滑动
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewpager2) { tab, position ->
            //TODO 自定义的View不知道如何修改
            tab.text = tabTitles[position]
        }.attach()

        //设置从 tabLayout 到 viewpager 的关联滑动
        val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    mBinding.viewpager2.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        }
        mBinding.tabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    inner class MyViewPager2Adapter : FragmentStateAdapter(this) {

        init {
            fragments.clear()
            for (tabTitle in tabTitles) {
                fragments.add(LifeCycleFragment.newInstance(tabTitle))
            }
        }

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment {
            return LifeCycleFragment.newInstance(tabTitles[position])
        }
    }
}