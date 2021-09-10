package com.jlpay.kotlindemo.ui.main.dailytest

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.ui.base.Constants
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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

        receivePdfFile()
    }

    private fun receivePdfFile() {
        val intent = intent
        val action = intent.action
        val mimeType = intent.type

        if (action == Intent.ACTION_SEND && !TextUtils.isEmpty(mimeType)) {
            when (mimeType) {
                "text/plain" -> handlerSendText(intent)
                "image/*" -> handlerSendImage(intent)
                "application/pdf" -> handlerSendPdf(intent)
            }
        } else if (action == Intent.ACTION_SEND_MULTIPLE && !TextUtils.isEmpty(mimeType)) {
            Toast.makeText(this, "暂未处理$mimeType", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handlerSendPdf(intent: Intent) {
        //TODO 由于无法知道其他程序发送过来的数据内容是文本还是其他类型的数据，若数据量巨大，则需要大量处理时间，因此我们应避免在UI线程里面去处理那些获取到的数据
        val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        uri?.let {
            pdf_view.fromUri(it).load()
            val savePdf = savePdf(it)
            Log.e("TAG", "这是保存的:$savePdf")
        }
    }

    private fun savePdf(uri: Uri): String {
        val file = File(Constants.FILE_SAVE_DIR + "my.pdf")
        var openInputStream: InputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            openInputStream = contentResolver.openInputStream(uri)
            fileOutputStream = FileOutputStream(file)
            openInputStream?.let {
                val buffer: ByteArray = ByteArray(1024)
                var readLength: Int = 0
                while (openInputStream.read(buffer).also { readLength = it } != -1) {
                    fileOutputStream.write(buffer, 0, readLength)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                openInputStream?.close()
                fileOutputStream?.flush()
                fileOutputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return file.absolutePath
    }

    private fun handlerSendImage(intent: Intent) {
        TODO("Not yet implemented")
    }

    private fun handlerSendText(intent: Intent) {
        TODO("Not yet implemented")
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