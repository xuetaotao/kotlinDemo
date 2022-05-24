package com.jlpay.kotlindemo.daily.practice

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.base.Constants
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * APP 文件管理器
 * 1.initFileManagerDir() 方法中，支持自己切换目录，变成某个特定目录的文件管理器
 * 2.initView() 方法最后几行的代码中，支持在初始指定的目录下，筛选自己需要的某种文件
 *
 *
 * 自定义文件管理器
 * 实现见 SmallFileManagerActivity
 *
 * 思路梳理：https://naotu.baidu.com/file/e444d527b42703b3ee650b4b1438b18b
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
class SmallFileManagerActivity : AppCompatActivity() {

    private lateinit var tvCurrentDir: TextView
    private lateinit var recyclerview: RecyclerView
    private lateinit var flFileListEmpty: FrameLayout
    private lateinit var fileAdapter: FileAdapter

    private var currentDirPath: String? = null
    private var initialDir: File? = null
    private lateinit var fileLists: List<File>
    var specialFileLists: MutableList<File> = ArrayList<File>()//通过遍历某个文件夹下所有文件，来获取某个特定文件类型
    private val TAG = Constants.TAG

    val permissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var allGranted: Boolean = true
            it.forEach { entry ->
                if (!entry.value) {
                    allGranted = false
                }
            }
            if (allGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {//检查是否已经有权限
                        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                        allFilePermission.launch(intent)
                    } else {
                        initView()
                    }

                } else {
                    initView()
                }
            }
        }


    @RequiresApi(Build.VERSION_CODES.R)
    val allFilePermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Environment.isExternalStorageManager()) {
                Log.e(TAG, "所有文件访问权限申请成功 ")
                initView()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_small_file_manager)

        initFileManagerDir()
    }

    private fun initFileManagerDir() {
        //1.只做外部存储 APP私有目录 getExternalFilesDir 的文件管理器，不需要任何权限
        initialDir = getExternalFilesDir(null)
        initView()

        //2.做外部存储 所有文件的文件管理器，要非常注意权限问题
        //要设置android:requestLegacyExternalStorage="true"，详细解释见 开头的注释
//        initialDir = Environment.getExternalStorageDirectory()
//        permissions.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE))

        //TODO 文件遍历过程部分手机因性能原因耗时较长，考虑加个Loading动画
    }


    private fun initView() {
        currentDirPath = initialDir?.absolutePath
        fileLists = initialDir?.listFiles()?.toList() ?: ArrayList<File>()

        tvCurrentDir = findViewById(R.id.tv_current_dir)
        "当前目录：$currentDirPath".also { tvCurrentDir.text = it }
        recyclerview = findViewById(R.id.recyclerview)
        flFileListEmpty = findViewById(R.id.fl_file_list_empty)
        fileAdapter = FileAdapter(this, fileLists)
        fileAdapter.setOnItemClickListener(object : FileAdapter.OnItemClickListener {
            override fun onCLick(data: List<File>, position: Int) {
                val file = data[position]
                loadDataFromCurrentDir(file)
            }
        })
        recyclerview.adapter = fileAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerview.layoutManager = linearLayoutManager


        //以下代码是用来 获取 initialDir 目录下所有的 pdf文件
        specialFileLists.clear()
        initialDir?.let {
            iteratorSpecifiedDir(it, arrayOf(".pdf", ".ofd"))
//            iteratorSpecifiedDir(it, null)
        }
        specialFileLists.forEach {
            Log.e(TAG, "initView: ${it.absolutePath}")
        }
    }

    override fun onBackPressed() {
        if (currentDirPath != null && !TextUtils.isEmpty(currentDirPath)) {
            val lastIndexOf = currentDirPath!!.lastIndexOf("/")
            if (lastIndexOf >= 0) {
                //注：这里只遍历本APP私有目录下的文件
                val absolutePath = initialDir?.absolutePath
                if (absolutePath == currentDirPath) {
                    super.onBackPressed()
                } else {
                    currentDirPath = currentDirPath!!.substring(0, lastIndexOf)
                    currentDirPath?.let {
                        loadDataFromCurrentDir(File(it))
                    }
                }
            } else {
                super.onBackPressed()
            }

        } else {
            super.onBackPressed()
        }
    }

    private fun loadDataFromCurrentDir(file: File) {
        if (file.isDirectory) {
            currentDirPath = file.absolutePath
//            Log.e(Constants.TAG, "loadDataFromCurrentDir: $currentDirPath")
            "当前目录：$currentDirPath".also { tvCurrentDir.text = it }
        }

        if (file.isDirectory && !file.isFile) {
            val subFiles = file.listFiles()
            if (subFiles == null || subFiles.isEmpty()) {
                flFileListEmpty.visibility = View.VISIBLE
                fileAdapter.updateData(ArrayList<File>())
            } else {
                flFileListEmpty.visibility = View.GONE
                subFiles
                    .toList()
                    .let { fileAdapter.updateData(it) }
            }

        } else {
            Toast.makeText(this@SmallFileManagerActivity, file.name, Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * 遍历目录获取某个类型的文件
     */
    private fun iteratorSpecifiedDir(file: File, types: Array<String>?) {
        file.listFiles()?.forEach {
            if (it.isDirectory) {
                iteratorSpecifiedDir(it, types)
            } else {
                types?.forEach { type ->
                    if (it.name.endsWith(type, ignoreCase = true)) {
                        specialFileLists.add(it)
                    }
                } ?: specialFileLists.add(it)
            }
        }
    }


    class FileAdapter(var context: Context, var fileLists: List<File>) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.item_small_file_manager, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val file = fileLists[position]

            holder.itemView.setOnClickListener {
                onItemClickListener?.onCLick(fileLists, position)
            }

            val split = file.absolutePath.split("/")
            val fileName = split[split.size - 1]
            holder.tvFileListFolderName.text = fileName

            if (file.isDirectory && !file.isFile) {
                holder.ivFileListFolder.setImageDrawable(AppCompatResources.getDrawable(context,
                    R.drawable.ic_file_folder))
            } else if (file.name.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
                holder.ivFileListFolder.setImageDrawable(AppCompatResources.getDrawable(context,
                    R.drawable.ic_file_pdf))
            } else {
                holder.ivFileListFolder.setImageDrawable(AppCompatResources.getDrawable(context,
                    R.drawable.ic_file_other))
            }
        }

        override fun getItemCount(): Int = fileLists.size

        fun updateData(fileLists: List<File>) {
            this.fileLists = fileLists
            notifyDataSetChanged()
        }

        private var onItemClickListener: OnItemClickListener? = null

        fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
            this.onItemClickListener = onItemClickListener
        }

        interface OnItemClickListener {
            fun onCLick(data: List<File>, position: Int)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var llFileFolder: LinearLayout = itemView.findViewById(R.id.ll_file_folder)
        var ivFileListFolder: ImageView = itemView.findViewById(R.id.iv_file_list_folder)
        var tvFileListFolderName: TextView =
            itemView.findViewById(R.id.tv_file_list_folderName)

    }
}