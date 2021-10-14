package com.jlpay.kotlindemo.ui.main.dailytest

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.github.barteksc.pdfviewer.PDFView
import com.jlpay.kotlindemo.R
import com.jlpay.kotlindemo.bean.getMimeType
import com.jlpay.kotlindemo.ui.base.Constants
import com.jlpay.kotlindemo.ui.utils.MediaUtils
import java.io.*

/**
 * Android接收从其他App传送来的数据:
 * https://www.w3cschool.cn/android_training_course/android_training_course-1nwj27ei.html
 */
class PdfViewerActivity : AppCompatActivity() {

    final val TAG = PdfViewerActivity::class.java.simpleName
    lateinit var pdf_view: PDFView
    lateinit var imageView: ImageView

    //通过SAF访问其他目录文件
    val getPdf = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != RESULT_OK) {
            return@registerForActivityResult
        } else {
            it.data?.data?.let { uri ->
                pdf_view.fromUri(uri).load()

                //测试一：默认开启分区存储的情况下，无法在外部存储根目录创建文件夹及文件（手动开启则可以）
//                val fileDir =
//                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "111hhh")
//                if (!fileDir.exists()) {
//                    fileDir.mkdirs()
//                }
//                val file1 =
//                    File(fileDir, "ss.pdf")

                //测试二：默认开启分区存储的情况下，无法在外部存储根目录直接创建文件（手动开启则可以）
//                val file1 =
//                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "ss.pdf")

                //测试三：默认开启分区存储的情况下，可以在外部存储 APP私有目录下直接创建文件夹和文件
                val file1 =
                    File(Constants.FILE_SAVE_DIR + "ss.pdf")

                //复制通过SAF方式选中的文件，到目标目录下，验证通过SAF可以访问外部存储的其他目录（系统或其他应用创建的目录）
                val fileOutputStream = FileOutputStream(file1)
                contentResolver.openInputStream(uri)?.let { it1 ->
                    MediaUtils.copy(it1, fileOutputStream)
                }
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
        Log.e(TAG, "onCreate: ")

        pdf_view = findViewById(R.id.pdf_view)
        imageView = findViewById(R.id.imageView)

        receivePdfFile(intent)
    }

    /**
     * 在该Activity的实例已经存在于Task和Back stack中(或者通俗的说可以通过按返回键返回
     * 到该Activity )时,当使用intent来再次启动该Activity的时候,如果此次启动不创建该
     * Activity的新实例,则系统会调用原有实例的onNewIntent()方法来处理此intent
     *
     * 这里若不添加保存操作的话，若用户在操作前已经进入了该页面，也就是 onCreate操作已经执行了
     * 那么就没办法再接收 Intent 携带过来的数据进行保存操作了
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e(TAG, "onNewIntent:${intent?.type}\t${intent?.action}")
        intent?.let {
            receivePdfFile(it)
        } ?: Toast.makeText(this, "onNewIntent:intent为null", Toast.LENGTH_SHORT).show()
    }

    private fun receivePdfFile(intent: Intent) {
        //有个问题，每次切到后台再返回来就又会重新再保存一次，
        //这是因为 onCreate 方法里的调用，每次切到后台都会执行 onDestroy，然后再切回来又会重新执行onCreate方法里的调用
        //所以这里为了避免保存多次文件不一样，采取了覆盖写入的方案，但是只是占用存储减少了，每次仍然会有写入的操作过程
        when (val action = intent.action) {
            //用其他应用打开：
            Intent.ACTION_VIEW -> {
                openFileByOtherApp(intent)
            }
            //关联到[handlerSendFile]方法：发送到其他
            //对应的intent-filter： <action android:name="android.intent.action.SEND" />
            Intent.ACTION_SEND -> {
                handlerSendFile(intent)
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                Toast.makeText(this, "暂未处理的$action", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 其他APP选择(如微信)：用其他应用打开，选择了我们的APP打开文件，走到这里
     * 据的获取方式与(发送到其他不同)：这里的数据是包裹在 Intent的 mData(Uri)里的
     */
    private fun openFileByOtherApp(intent: Intent) {
        val data = intent.data
        data?.let {
            Log.e(TAG, "openFileByOtherApp#getMimeType: ${getMimeType(data.toString())}")
            Log.e(TAG, "openFileByOtherApp:${intent.type} ")
            Log.e(TAG,
                "openFileByOtherApp:data.path:${data.path} ")//  /external_files/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/标签支付(1).pdf

            val splitPath = data.path?.split("/")

            if ("application/pdf" == intent.type) {
                pdf_view.fromUri(it).load()
                val fileName = splitPath?.get(splitPath.size - 1) ?: "未知名文件.pdf"
                val savePdf = saveReceiveFile(it, fileName)
                Toast.makeText(this, "文件已保存到:$savePdf", Toast.LENGTH_SHORT).show()
            } else {
                val fileName = splitPath?.get(splitPath.size - 1) ?: "未知名文件.ofd"
                val savePdf = saveReceiveFile(it, fileName)
                Toast.makeText(this, "文件已保存到:$savePdf", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 注：该功能暂时被屏蔽，如果需要使用，需要打开AndroidManifest中的 SEND配置，并关闭 VIEW配置和scheme配置
     * 使用下面注释中的配置
     *
     * 其他APP选择(如微信)：发送到其他，选择了发送到我们的APP，走到这里
     * 数据的获取方式与(用其他应用打开不同)：这里的数据是包裹在 Intent的 mExtras(Bundle)里的
     *
     * 对应的 intent-filter 配置如下：
     *  <action android:name="android.intent.action.SEND" /> //接收单个 //"android.intent.action.SEND_MULTIPLE"是连续接收多个
     *  注意：如果与 <action android:name="android.intent.action.VIEW" />一起使用，经测试，SEND会被屏蔽调
     *  <category android:name="android.intent.category.DEFAULT" />
     *  <data android:mimeType="text/plain" />  //"image/这里是个*"/  "application/pdf"、
     */
    private fun handlerSendFile(intent: Intent) {
        val mimeType = intent.type
        Log.e(TAG, "handlerSendFile: $mimeType")
        //TODO 由于无法知道其他程序发送过来的数据内容是文本还是其他类型的数据，若数据量巨大，则需要大量处理时间，因此我们应避免在UI线程里面去处理那些获取到的数据
        when (mimeType) {
            "text/plain" -> {//微信发送文本文件，测试OK
//                val content = intent.getStringExtra(Intent.EXTRA_TEXT)//测试发现微信发送过来的是Intent.EXTRA_STREAM
                val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                uri?.let { uriSend ->
                    val bufferedReader =
                        BufferedReader(InputStreamReader(contentResolver.openInputStream(uriSend)))
                    var content: String? = ""
                    var lineContent: String? = null
                    while (bufferedReader.readLine().also { lineContent = it } != null) {
                        content += lineContent + "\n"
                    }
                    bufferedReader.close()
                    Toast.makeText(this, "文件内容是:$content", Toast.LENGTH_SHORT).show()
                }
            }
            "image/*" -> {//未找到测试方法，未测试
                val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                uri?.let {
                    try {
                        val openInputStream = contentResolver.openInputStream(it)
                        val bitmap = BitmapFactory.decodeStream(openInputStream)
                        imageView.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            "application/pdf" -> {//微信发送pdf文件，测试OK
                val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                uri?.let {
                    pdf_view.fromUri(it).load()
                    val savePdf = saveReceiveFile(it, "sendFile.pdf")
                    Toast.makeText(this, "文件已保存到:$savePdf", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    /**
     * @param uri 发送或者其他应用打开传递过来的文件uri
     * @param mimeType 用来确定要保存文件的后缀名是 .pdf还是 .ofd
     */
    private fun saveReceiveFile(uri: Uri, fileName: String): String {
        val file = File(Constants.FILE_SAVE_DIR + fileName)

        //////////////////////////////////////单纯为 chooseOtherApp 方法用到的 receiveUri 赋值/////////////
        val uriPdfFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(this, "com.jlpay.kotlindemo.FileProvider", file)
        } else {
            Uri.fromFile(file)
        }
        receiveUri = uriPdfFile
        //////////////////////////////////////////////////////////////////////////////////////////////

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


    /**
     * 测试File类的 list() 和 listFiles() 方法
     */
    private fun testFileMethods() {
        val testFile = getExternalFilesDir(null)
        val list: Array<String>? = testFile?.list()
        val listFiles: Array<File>? = testFile?.listFiles()
        list?.forEach {
            Log.e(TAG, "saveReceiveFile: list:${it}")//audio、file、image、video、apk、res
        }
        println()
        listFiles?.forEach {
            Log.e(TAG,
                "saveReceiveFile: listFiles:${it}")// /storage/emulated/0/Android/data/com.jlpay.kotlindemo/files/audio 等等
        }
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

    var receiveUri: Uri? = null
    fun chooseOtherApp(view: View) {
        testFileMethods()

        receiveUri?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setDataAndType(it, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ")
    }
}