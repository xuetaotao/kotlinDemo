package com.jlpay.kotlindemo.ui.main.dailytest

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jlpay.kotlindemo.R

/**
 * 自定义文件管理器
 * 因为Android11上要申请的权限过于霸道，申请MANAGE_EXTERNAL_STORAGE权限并且适配Android11以上，会被禁止上Google商店，所以这里仅测试适配外部共享存储
 *
 * 1.在targetSdkVersion = 29应用中，设置android:requestLegacyExternalStorage="true"，就可以不启动分区存储，让以前的文件读取正常使用。
 * 但是targetSdkVersion = 30中不行了，强制开启分区存储。
 *
 * 2.假若App开启了分区存储功能，当App运行在Android 10.0的设备上时，是没法遍历/sdcard/目录的。而在Android 11.0上运行时是可以遍历的，需要进行如下几个步骤。
 *  1)声明管理权限:<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
 *  2)动态申请所有文件访问权限:startActivityForResult(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION), 101)
 *  3)遍历目录、读写文件
 *
 * 3.综上，如果想要做文件管理器、病毒扫描管理器等功能。则判断运行设备版本是否大于等于Android 6.0，若是先需要申请普通的存储权。若运行设备版本为Android 10.0，
 * 则可以直接通过路径访问/sdcard/目录下文件(因为禁用了分区存储)；若运行设备版本为Android 11.0，则需要申请MANAGE_EXTERNAL_STORAGE 权限。
 *
 * 实现方案可参考：https://blog.csdn.net/fitaotao/article/details/119700579
 * https://github.com/zippo88888888/ZFileManager
 */
class FileManagerActivity : AppCompatActivity() {

    lateinit var recyclerview: RecyclerView
    private lateinit var myAdapter: MyAdapter
    var dataList = ArrayList<String>()
    val permissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var allGranted: Boolean = true
            it.forEach { entry ->
                if (!entry.value) {
                    allGranted = false
                }
            }
            if (allGranted) {
                //TODO 在Android 10设备上读取不出来，在Android11设备上可以找出来
                getDocument.launch(arrayOf("*/*"))

                /*
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                        allFilePermission.launch(intent)
                    }
                     */
            }
        }


    val getDocument = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        it?.toString()?.let { it1 -> Log.e("TAG", it1) }
    }


    /*
    @RequiresApi(Build.VERSION_CODES.R)
    val allFilePermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Environment.isExternalStorageManager()) {
                Log.e("TAG", "申请成功所有文件访问权限")
            }
            iteratorExternDirFile()
        }
        */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manager)

        recyclerview = findViewById(R.id.recyclerview)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerview.layoutManager = linearLayoutManager
        myAdapter = MyAdapter(this, dataList)
        recyclerview.adapter = myAdapter
    }


    fun foreachFile(view: View) {
        permissions.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    private fun iteratorExternDirFile() {


        /*
        val externDirFile = Environment.getExternalStorageDirectory()
        val listFiles: Array<out File>? = externDirFile.listFiles()
        listFiles?.forEach { file ->
            if (file.isDirectory) {
                dataList.add(file.absolutePath)
            }
        }
        myAdapter.notifyDataSetChanged()
         */
    }

    class MyAdapter(private var context: Context, private var dataList: ArrayList<String>) :
        RecyclerView.Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view =
                LayoutInflater.from(context).inflate(R.layout.item_file_manager, parent, false)
            val holder = MyViewHolder(view)
            //TODO 点击事件：如果为目录，就跳转下一层
            return holder
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.tv_fileDir.text = dataList[position]
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_fileDir: TextView = itemView.findViewById(R.id.tv_fileDir)

    }
}