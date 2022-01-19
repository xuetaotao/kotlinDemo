package com.jlpay.kotlindemo.ui.main.dailytest

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityLifecycleBinding
import com.jlpay.kotlindemo.ui.base.BaseLifeCycleActivity

/**
 * Android控件-TabLayout使用介绍
 * https://juejin.cn/post/6999807478284173342
 *
 * TODO 去掉滑动到最左边或者最右边后，继续向左或者向右滑动的动画
 * TODO 使用ViewPager2实现当前效果
 */
class LifeCycleActivity : BaseLifeCycleActivity() {

    private val TAG = this::class.java.simpleName
    private lateinit var mBinding: ActivityLifecycleBinding
    private lateinit var fragments: ArrayList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView<ActivityLifecycleBinding>(
            this,
            R.layout.activity_lifecycle
        )
        mBinding.lifecycleOwner = this

        initView()
    }

    private fun initView() {
        initViewPager()
    }

    private fun initViewPager() {
        fragments = ArrayList()
        fragments.run {
            add(LifeCycleFragment.newInstance("微信"))
            add(LifeCycleFragment.newInstance("通讯录"))
            add(LifeCycleFragment.newInstance("发现"))
            add(LifeCycleFragment.newInstance("我"))
        }
        //给ViewPager设置adapter
        mBinding.viewpager.adapter = LifePageAdapter(
            fragments, supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
        )
//        mBinding.viewpager.adapter = LifePageAdapter(
//            fragments, supportFragmentManager,
//            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
//        )
        //设置 viewpager 的滑动监听
//        mBinding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                Log.e(TAG, "onPageScrolled: ")
//            }
//
//            override fun onPageSelected(position: Int) {
//                Log.e(TAG, "onPageSelected: ")
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//                Log.e(TAG, "onPageScrollStateChanged: ")
//            }
//        })
        //设置从 viewpager 到 tabLayout 的关联滑动
        mBinding.viewpager.addOnPageChangeListener(object :
            TabLayout.TabLayoutOnPageChangeListener(mBinding.tabLayout) {
        })
        //为TabLayout添加tab,也可以在xml布局中以 TabItem 方式添加
        mBinding.tabLayout.run {
            addTab(mBinding.tabLayout.newTab().setText("微信"))
            addTab(mBinding.tabLayout.newTab().setText("通讯录"))
            addTab(mBinding.tabLayout.newTab().setText("发现"))
            addTab(mBinding.tabLayout.newTab().setText("我"))
        }
        //设置从 tabLayout 到 viewpager 的关联滑动
        mBinding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.ViewPagerOnTabSelectedListener(mBinding.viewpager) {
        })
    }


    class LifePageAdapter(var fragments: List<Fragment>, fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }
    }
}