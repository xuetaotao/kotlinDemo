package com.jlpay.kotlindemo.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseLifeCycleFragment : Fragment() {

    private val TAG = this::class.java.simpleName
    private var isShowLifecycleLog = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (isShowLifecycleLog) {
            Log.e(TAG, "onAttach: ")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isShowLifecycleLog) {
            Log.e(TAG, "onCreate: ")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (isShowLifecycleLog) {
            Log.e(TAG, "onCreateView: ")
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isShowLifecycleLog) {
            Log.e(TAG, "onViewCreated: ")
        }
    }

    /**
     * This is called after onCreateView and before onViewStateRestored(Bundle).
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isShowLifecycleLog) {
            Log.e(TAG, "onActivityCreated: ")
        }
    }

    override fun onStart() {
        super.onStart()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onStart: ")
        }
    }

    override fun onResume() {
        super.onResume()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onResume: ")
        }
    }

    override fun onPause() {
        super.onPause()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onPause: ")
        }
    }

    override fun onStop() {
        super.onStop()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onStop: ")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onDestroyView: ")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onDestroy: ")
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (isShowLifecycleLog) {
            Log.e(TAG, "onDetach: ")
        }
    }

    fun setIsShowLifecycleLog(isShowLifecycleLog: Boolean) {
        this.isShowLifecycleLog = isShowLifecycleLog
    }
}