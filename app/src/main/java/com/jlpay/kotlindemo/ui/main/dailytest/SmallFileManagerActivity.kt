package com.jlpay.kotlindemo.ui.main.dailytest

import android.Manifest
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
import com.jlpay.kotlindemo.ui.base.Constants
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * APP 文件管理器
 * 1.initFileManagerDir() 方法中，支持自己切换目录，变成某个特定目录的文件管理器
 * 2.initView() 方法最后几行的代码中，支持在初始指定的目录下，筛选自己需要的某种文件
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
        //1.只做APP私有目录 getExternalFilesDir 的文件管理器，不需要任何权限
//        initialDir = getExternalFilesDir(null)

        //2.做手机所有文件的文件管理器，要非常注意权限问题
        initialDir = Environment.getExternalStorageDirectory()
        permissions.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE))

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