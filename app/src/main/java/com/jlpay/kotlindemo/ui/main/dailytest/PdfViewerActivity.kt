package com.jlpay.kotlindemo.ui.main.dailytest

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.jlpay.kotlindemo.R

class PdfViewerActivity : AppCompatActivity() {

    lateinit var pdf_view: PDFView
    lateinit var imageView: ImageView

    val getPdf = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != RESULT_OK) {
            return@registerForActivityResult
        } else {
            it.data?.data?.let { uri ->
                pdf_view.fromUri(uri).load()
            }
        }
    }

    val getImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != RESULT_OK) {
            return@registerForActivityResult
        } else {
            it.data?.data?.let { uri ->
                try {
                    val openInputStream = contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(openInputStream)
                    imageView.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        try {
            val openInputStream = contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(openInputStream)
            imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val permissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var allGranted: Boolean = true
            it.forEach { entry ->
                if (!entry.value) {
                    allGranted = false
                }
            }
            if (allGranted) {
                //TODO 这里需要测试微信发过来的图片，看看保存在哪里，以及都能访问到哪里的文件
                //通过SAF文件访问框架 访问共享目录下其他应用创建的非media文件
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "application/pdf"//TODO  貌似没啥用，具体还需进一步确定

                getPdf.launch(intent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfviewer)

        pdf_view = findViewById(R.id.pdf_view)
        imageView = findViewById(R.id.imageView)
    }

    fun pdfViewer(view: View) {
        permissions.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    fun imageLook(view: View) {
        //方式一：
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.type = "image/*"
//        getImage.launch(intent)

        //方式二：
        getContent.launch("image/*")
    }


}