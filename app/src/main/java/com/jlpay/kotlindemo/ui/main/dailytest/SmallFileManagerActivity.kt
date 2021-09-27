package com.jlpay.kotlindemo.ui.main.dailytest

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
 * APP私有目录下的文件管理器
 * 不需要获取任务相关权限
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_small_file_manager)

        initView()
    }


    private fun initView() {
        initialDir = getExternalFilesDir(null)
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


        //以下代码是用来 获取 getExternalFilesDir 目录下所有的 pdf文件
        specialFileLists.clear()
        initialDir?.let {
            iteratorSpecifiedDir(it, arrayOf(".pdf", ".ofd"))
//            iteratorSpecifiedDir(it, null)
        }
        specialFileLists.forEach {
            Log.e(TAG, "initView: ${it.name} \n ${it.absolutePath}")
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
                subFiles.toList().let { fileAdapter.updateData(it) }
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