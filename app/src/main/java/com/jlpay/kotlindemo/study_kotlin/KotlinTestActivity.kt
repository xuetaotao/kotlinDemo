package com.jlpay.kotlindemo.study_kotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Kotlin关键字整理：
 * out: Java的 ? extends T  对应  Kotlin的 out T， 只可以被读取而不可以被写入，协变
 * in:  Java的 ? super T  对应  Kotlin的 in T， 只可以被写入而不可以被读取，逆变
 * inline:  将内联函数的函数体复制到调用处实现内联，即调用的函数在编译的时候会变成代码内嵌的形式
 * reified: 用于Kotlin内联函数的,修饰内联函数的泛型,泛型被修饰后,在方法体里,能从泛型拿到泛型的Class对象,这与java是不同的,java需要泛型且需要泛型的Class类型时,是要把Class传过来的,但是kotlin不用了 (https://www.jianshu.com/p/e59fda556464)
 * const val：编译时常量，编译的时候会进行内联式编译
 * object：TODO 相当于static？
 * as：强制类型转换
 * is：a is b，判断a是不是b的子类型，就是java的 instanceof
 *
 * shl(bits) – 左移位 (Java’s <<)
 * shr(bits) – 右移位 (Java’s >>)
 * ushr(bits) – 无符号右移位 (Java’s >>>)
 * and(bits) – 按位与 (Java’s  &&)
 * or(bits) – 按位或 (Java’s  ||)
 * xor(bits) – 按位异或 (Java’s ^)
 * inv() – 按位非 (Java’s !)
 */
class KotlinTestActivity : AppCompatActivity() {

    private val TAG = KotlinTestActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)

        initData()
    }

    private fun initData() {
        arrayTest()
        exceptionTest()

        val onClickListener: View.OnClickListener =
            View.OnClickListener {
                Toast.makeText(
                    this,
                    "我是onClickListener:${it.id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        findViewById<Button>(R.id.button).setOnClickListener(onClickListener)

        LambdaTest().main()

        testIt()
    }

    /**
     * use 的使用: 可以让我们不用自己写IO操作完成后，一系列关流的操作
     * copy文件
     */
    private fun useDemo(source: File, dest: File) {
        FileInputStream(source).use { input ->
            FileOutputStream(dest).use { output ->
                val buf = ByteArray(1024)
                while (true) {
                    val readLength: Int = input.read(buf)
                    if (readLength <= 0) {
                        break
                    }
                    output.write(buf, 0, readLength)
                }
            }
        }

//        val inputChannel = FileInputStream(source).channel
    }

    abstract class Base {
        var code: Int = calculate()

        //加上下面这两行也不行
//        var code: Int = initCode()
//        fun initCode() = calculate()
        abstract fun calculate(): Int
    }

    class Derived(private val x: Int) : Base() {
        override fun calculate(): Int {
            return x
        }
    }

    fun testIt() {
        println(Derived(42).code) // Expected: 42, actual: 0
        Log.e(TAG, "testIt: ${Derived(42).code}")
    }

    /**
     * 测试异常
     */
    private fun exceptionTest() {
        try {
            throw Exception("测试异常")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("LibTestKotlinActivity", "测试异常")
        Log.e("LibTestKotlinActivity", "测试异常222")
        Log.e("LibTestKotlinActivity", "测试异常333")
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

    class LambdaTest {

        fun main() {
            // 前面为什么用var方法不应该是fun吗？
            // 我们一直在写匿名函数，把匿名函数给 method01 变量
            // KT函数是一等公民

            // 函数中的函数，就是高阶函数

            // KT中的 基本上都是表达式 包括 if  可以灵活返回
            // Java中，基本上都是 语句 包括 if  执行体 不可以返回

            //下面全部都是函数声明， 既然是函数声明，就不能调用

            // 函数的声明 用lambda去描述函数的声明
//            var method1: () -> Unit
//            var method2: (Int, Int) -> Unit
//            var method3: (String, Double) -> Any
//            var method4: (Int, Double, Long, String) -> Boolean
//            var method5: (Int, Int) -> Int


            // 函数的声明 + 函数的实现
            // : (参数)->返回  方式一 () 基本上属于声明 上面已经讲过了 就是函数的声明
            // = {参数->方式}  方式二 () 基本上属于声明实现结合
            var method01: () -> Unit = { println("我是method01函数") }
            method01()//我是method01函数  // 调用函数  () == 操作符重载 invoke操作符
            method01.invoke()

            var method02/*: () -> String*/ = { "我是method02函数" }
            println(method02())

            var method03 = { str: String -> println("method03:你传入进来的值是$str") }
            method03("白小纯")

            var method04: (Int, Int) -> String =
                { num1: Int, num2: Int -> num1.toString() + num2.toString() }
            println("method04:" + method04(2, 3))

            var method05 = { num1: Int, num2: Int -> num1 + num2 }
            println("method05:" + method05(2, 3))

            var method06: (Int) -> String // 先声明 后实现
            method06 = fun(value: Int): String = value.toString()
            println("method06:" + method06(99))

            var method07: (Int) -> String
            method07 = fun(value) = value.toString()
            println("method07:" + method07(7))

            var method08: (Int) -> String = { value -> "method08:$value" }
            println(method08(8))

            //TODO 暂时理解不到位  (Int, Int) -> String = { n1, n2 -> "相加的结果是:${n1 + n2}  $n1aaa" } 放一起看
            var fun4 = fun(n1aaa: Int, n2aaa: Int): (Int, Int) -> String =
                { n1, n2 -> "相加的结果是:${n1 + n2}  $n1aaa" }
            println(fun4(100, 100)(1000, 1000))//相加的结果是:2000  100
            println(
                fun4(
                    100,
                    100
                )
            )//Function2<java.lang.Integer, java.lang.Integer, java.lang.String>

            var k01: (String) -> (String) -> (Boolean) -> (Int) -> (String) -> Int =
                {
                    {
                        {
                            {
                                {
                                    99
                                }
                            }
                        }
                    }

                }
            println(k01("AAA")("BBB")(true)(800)("Derry"))//99

            // 高阶函数 规则
            fun loginServer(
                userName: String,
                userPwd: String,
                responseResult: (String, String) -> Unit,
            ) =
                responseResult(userName, userPwd)

            fun loginEngine(userName: String, userPwd: String, responseResult: (Boolean) -> Unit) {
                if (userName == "Derry" && userPwd == "123456") {
                    responseResult(true)
                } else {
                    responseResult(false)
                }
            }

            // 我对T扩展，T本身就等于==this
            fun <T> T.myRunOK(mm: T.(Double) -> Boolean) /*: Boolean*/ = mm(664.45) // 执行lambda

            "AA".myRunOK {
                println("这是一个:$this,它的$it")
                true
            }

            fun <T> T.myRunOK2(mm: (Float) -> Boolean) /*: Boolean*/ = mm(656.4f) // 执行lambda

            "derry".run {
                println("derry的长度:$length") // 已经等到derry字符串长度了
            }

            "derry".let {
                println("derry的长度:${it.length}") // 已经等到derry字符串长度了
            }

            val list: List<String> = listOf("aa", "bb", "vv", "cc", "dd")
            println(list.getOrElse(8) { "ads" })
//            list.add
            val mutableList = list.toMutableList()
            mutableList.add("HHH")
            list.forEach { s ->
                println("this is $s")
            }
            list.forEachIndexed { index: Int, s: String ->
                println("this is the $index : $s")
            }
        }
    }

    //mapAction: (I) -> O 中的形参 I 必须带括号
    inline fun <I, O> map(input: I, isMap: Boolean = true, mapAction: (I) -> O) =
//        if (isMap) mapAction(input) else null
        mapAction(input).takeIf { isMap }

    //调用自己写的Map变换符（模仿RxJava）
    var aa/*: String?*/ = map(123) {
        "自定义Map变换符的结果：$it"
    }
}