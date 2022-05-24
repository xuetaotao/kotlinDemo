package com.jlpay.kotlindemo.study_jetpack.mvvm4

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.databinding.ActivityRoomBinding

/**
 * Android从零开始搭建MVVM架构（4）————Room（从入门到进阶）
 * https://juejin.cn/post/6844903966086529038
 *
 * Room：
 *
 * 简介：Room是google为了简化旧式的SQLite操作专门提供的一个覆盖SQLite抽象层框架库
 * 作用：实现SQLite的增删改查（通过注解的方式实现增删改查，类似Retrofit。）
 *
 * 在使用Room，有4个模块：
 * Bean：实体类，表示数据库表的数据
 * Dao：数据操作类，包含用于访问数据库的方法
 * Database：数据库持有者 & 数据库版本管理者
 * Room：数据库的创建者 & 负责数据库版本更新的具体实现者
 *
 * 与greendao的区别（这里只是简单从表面看）：同样基于ORM模式封装的数据库。而Room和其他ORM对比，具有编译时验证查询语句正常性，支持LiveData数据返回等优势。
 * 我们选择room，更多是因为对LiveData的完美支持。同时也支持RxJava，我们都知道数据库操作这些耗时操作都应该放在子线程里，所以配合RxJava和LiveData很完美了。因为他们都是异步的
 *
 * TODO 项目未完成，没有考虑好ViewModel层怎么写
 */
class RoomActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRoomBinding
    private lateinit var mViewModel: RoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //DataBinding初始化
        mBinding = DataBindingUtil.setContentView<ActivityRoomBinding>(this, R.layout.activity_room)
        mBinding.presenter = Presenter()

        //ViewModel初始化
        mViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(RoomViewModel::class.java)

        //对person数据进行观察
        mViewModel.person.observe(this, Observer {
            Toast.makeText(this, "插入了一条Person数据", Toast.LENGTH_SHORT).show()
        })
    }


    inner class Presenter {
        fun onClick(view: View) {
            when (view.id) {
                R.id.btn_insert -> {
                    mViewModel.insertPerson()
                }
            }
        }
    }
}