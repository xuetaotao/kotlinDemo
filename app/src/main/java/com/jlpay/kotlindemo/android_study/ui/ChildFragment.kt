package com.jlpay.kotlindemo.android_study.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.FragmentLifecycleBinding
import com.jlpay.kotlindemo.base.BaseLifeCycleFragment

class ChildFragment : BaseLifeCycleFragment() {

    private lateinit var mBinding: FragmentLifecycleBinding

    companion object {
        //        fun newInstance(content: String) = LifeCycleFragment()
        fun newInstance(content: String): Fragment {
            val fragment = ChildFragment()
            val bundle = Bundle()
            bundle.putString("content", content)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lifecycle, container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        val arguments = arguments
        var content: String = "暂无"
        arguments?.let {
            content = it.getString("content").toString()
        }

        mBinding.tvCurrent.text = content
    }
}