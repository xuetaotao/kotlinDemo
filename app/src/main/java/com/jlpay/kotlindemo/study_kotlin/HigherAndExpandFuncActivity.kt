package com.jlpay.kotlindemo.study_kotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityHignerAndExpandFuncActivityBinding

/**
 * Kotlin 高阶函数和扩展函数
 * Kotlin 高阶函数与Lambda：https://www.bilibili.com/video/BV1kp4y1C7DE?spm_id_from=333.999.0.0
 * Kotlin 的扩展函数和扩展属性： https://www.bilibili.com/video/BV16K411W7kU/?spm_id_from=pageDriver
 *
 * Kotlin高阶函数：
 * 1. ()->Unit与(T) -> Unit是相同的，只是一个带参，一个不带参
 * 2. (T) -> Unit通过形参T可将对象作为实参传给函数，所以函数体里能通过it或者指定名称的方式来访问该对象
 * 3. T.()->Unit 等同于为T定义了一个无参数的扩展函数，所以在函数体内可以直接通过this或省略来访问T代表的对象
 */
class HigherAndExpandFuncActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityHignerAndExpandFuncActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding =
            DataBindingUtil.setContentView<ActivityHignerAndExpandFuncActivityBinding>(
                this,
                R.layout.activity_higner_and_expand_func_activity
            )
    }

    fun customTest(view: android.view.View) {
        method1Demo()
    }


    fun method1Demo() {
        (String::method1)("rengwuxian", 1)
        (String::method1).invoke("rengwuxian", 1)
        "rengwuxian".method1(3)
        "rengwuxian".a(1)
        a("rengwuxian", 1)
        a.invoke("rengwuxian", 1)
    }
}

private val TAG: String? = HigherAndExpandFuncActivity::class.java.simpleName


/**
 * 扩展函数的引用赋值给变量
 */
fun String.method1(i: Int) {
    Log.e(TAG, "method1: $i")
}

fun method3(s: String, i: Int) {

}

//如果这个函数放在HigherAndExpandFuncActivity里，就不能这么通过引用赋值了
val a: String.(Int) -> Unit = String::method1
val b: (String, Int) -> Unit = String::method1
val c: (String, Int) -> Unit = a
val d: String.(Int) -> Unit = b
val e: (String, Int) -> Unit = ::method3
val f: String.(Int) -> Unit = ::method3
