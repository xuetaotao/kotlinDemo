package com.jlpay.kotlindemo.ui.main.dailytest

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

class KotlinTestActivity : AppCompatActivity() {

    private val TAG = KotlinTestActivity::class.java.simpleName

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)

        textView = findViewById(R.id.textView)
        initData()
    }

    private fun initData() {
        arrayTest()
    }

    /**
     * Kotlin数组学习
     * https://www.jianshu.com/p/a33003d7f93a
     *
     * 数组在 Kotlin 中使用 Array 类来表示，它定义了 get 与 set 函数（按照运算符重载约定这会转变为 []）以及 size 属性
     *
     * inline 关键字 的工作原理就是将内联函数的函数体复制到调用处实现内联
     */
    private fun arrayTest() {

        /*******定义数组*******/

        //创建指定大小的数组
        val array1 = Array(1) { i ->
            i * 1
        }

        //创建指定类型指定大小的数组
        val array2 = IntArray(10)
        val array5 = IntArray(6) { it }

        //创建指定类型指定大小的空数组
        val array3 = arrayOfNulls<Int>(5)

        //创建指定基本类型的数组，明确元素
        val array4 = intArrayOf(1, 2, 3, 4, 5)

        //创建空数组
        val emptyArray = emptyArray<Int>()


        /*******遍历数组*******/
        val arr1 = arrayOf(1, 2, 3)
        for (i in arr1) {
            Log.e(TAG, "arr1:$i")
        }

        //通过索引
        val arr2 = arrayOf(6, 7, 8)
        for (index in arr2.indices) {
            Log.e(TAG, "arr2:$index -> \t${arr2[index]}")
        }

        //函数式
        val arr3 = arrayOf(5, 6, 7)
        arr3.forEach { item ->
            Log.e(TAG, "arr3:$item")
        }

        if (1 in arr1) {
            Log.e(TAG, "arr1中有1")
        }

        if (5 !in arr1) {
            Log.e(TAG, "arr1中没有5")
        }
    }


}