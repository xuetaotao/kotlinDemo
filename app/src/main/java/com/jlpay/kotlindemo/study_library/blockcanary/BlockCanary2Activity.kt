package com.jlpay.kotlindemo.study_library.blockcanary

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater.Factory2
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.view.LayoutInflaterCompat
import com.jlpay.kotlindemo.R
import com.zhangyue.we.x2c.X2C
import com.zhangyue.we.x2c.ano.Xml

/**
 * 布局加载耗时监测及解决办法
 */
//X2C：在使用布局的任意java类或方法添加注解即可
@Xml(layouts = arrayOf("activity_blockcanary2"))
class BlockCanary2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
//        setLayoutInflaterFactory2()
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_blockcanary2)
        x2cDemo()

//        asyncLayoutInflaterDemo()

        //使用asyncLayoutInflaterDemo时这部分要放在下面方法中
        val btnBlock = findViewById<Button>(R.id.btn_block)
        btnBlock.text = "测试一下"
    }

    public fun blockCanaryClick(view: View) {
        Toast.makeText(this, "哈哈哈", Toast.LENGTH_SHORT).show()
    }


    /**
     * 异步布局加载
     * 那就要把 setContentView 方法和 findViewById  注释掉，放在异步线程做，最后回调到主线程。
     * 注意：不能加载xml布局中有fragment和 webView 的控件
     * 通过 AsyncLayoutInflater 实现，这个类可以学习一下
     */
    fun asyncLayoutInflaterDemo() {
        AsyncLayoutInflater(this).inflate(R.layout.activity_blockcanary2, null,
            object : AsyncLayoutInflater.OnInflateFinishedListener {
                override fun onInflateFinished(view: View, resid: Int, parent: ViewGroup?) {
                    Log.e("TAG", "onInflateFinished: ${Thread.currentThread().name}")//main
                    setContentView(view)
                    val btnBlock = findViewById<Button>(R.id.btn_block)
                    btnBlock.text = "测试一下"
                }
            })
    }


    /**
     * 创建控件对象，减少反射
     * X2C框架
     * 在使用布局的任意java类或方法添加注解即可 @Xml(layouts = "activity_main")
     * 通过X2C加载布局 在原先使用setContentView或inflate的地方替换，如下：
     * this.setContentView(R.layout.activity_main); --> X2C.setContentView(this, R.layout.activity_main);
     * LayoutInflater.from(this).inflate(R.layout.activity_main,null); --> X2C.inflate(this,R.layout.activity_main,null);
     * 另外，findViewById的代码要放开注释
     */
    fun x2cDemo() {
        X2C.setContentView(this, R.layout.activity_blockcanary2)
    }

    /**
     * 一、测试XML文件中每个View创建的耗时时间
     * setFactory2这个方法一定要在super.onCreate(savedInstanceState)和setContentView()之前进行处理
     * 前者方法里面会setFactory2，这个方法注释里说了只能设置一次
     * 另外就是这个方法在DataBinding的setContentView()前使用有点问题，所以正常使用写在BlockCanary2Activity里
     * 至于setFactory2与setFactory方法区别是setFactory2是为了提供这么个设置功能
     */
    fun setLayoutInflaterFactory2() {
        LayoutInflaterCompat.setFactory2(layoutInflater, object : Factory2 {
            //这个方法每次由XML文件创建View都会被回调，可以输出主页面所有的控件
            //parent：父View的对象  name：当前View的XML名字    attrs：View的属性及属性值
            override fun onCreateView(
                parent: View?,
                name: String,
                context: Context,
                attrs: AttributeSet
            ): View? {
                //name是每个控件的名字
                if (TextUtils.equals(name, "Button")) {
                    //如果是Button控件，在这里可以进行自定义View的替换，类似于下面的方法
                    //androidx.appcompat.app.AppCompatViewInflater.createView(...)
                    //如果是Button控件，在这里可以进行自定义View的替换，类似于下面的方法
                    //androidx.appcompat.app.AppCompatViewInflater.createView(...)
//                    return AppCompatButton(context, attrs)
                }
                //玩一些属性
                val attributeCount = attrs.attributeCount
                val startTime = System.currentTimeMillis()
                //原代码中的创建View流程
                val view = delegate.createView(parent, name, context, attrs)
                val stopTime = System.currentTimeMillis()
                Log.e("TAG", "onCreateView: " + name + "\tcost " + (stopTime - startTime) + "ms")
                return view
            }

            override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                //这个是Factory的方法
                return null
            }
        })
    }
}