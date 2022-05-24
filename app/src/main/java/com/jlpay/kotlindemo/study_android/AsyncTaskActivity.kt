package com.jlpay.kotlindemo.study_android

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection

class AsyncTaskActivity : AppCompatActivity() {

    private lateinit var tvShow: TextView
    private lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async_task)
        tvShow = findViewById(R.id.tv_show)
        progressbar = findViewById(R.id.progressbar)
    }

    fun downLoad(view: View) {
        //必须在UI线程创建AsyncTask实例
        //必须在UI线程调用AsyncTask的execute()方法
        val downLoadTask = DownLoadTask(this, progressbar)
        downLoadTask.execute(URL("https://www.baidu.com"))
    }

    inner class DownLoadTask(
        context: Context,
        progressBar: ProgressBar
    ) : AsyncTask<URL, Int, String>() {
        //AsyncTask的三个参数意义：Params：启动任务执行的输入参数的类型
        //Progress：后台任务完成的进度值的类型
        //Result：后台执行任务完成后返回结果的类型

        private var hasRead = 0//定义记录已经读取行的数量

        //后台线程将要完成的任务
        override fun doInBackground(vararg params: URL): String? {
            val stringBuilder: StringBuilder = StringBuilder()
            try {
                val urlConnection: URLConnection = params[0].openConnection()
                val bufferedReader =
                    BufferedReader(InputStreamReader(urlConnection.getInputStream(), "utf-8"))
                var line: String?

                //Kotlin不提倡将赋值运算符当做表达式，所以会禁止此举动
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(line + "\n")
//                    hasRead++
//                    publishProgress(hasRead)//更新任务的进度
//                }

                do {
                    line = bufferedReader.readLine()
                    if (line != null) {
                        stringBuilder.append(line + "\n")
                        hasRead++
                        publishProgress(hasRead)
                    } else {
                        break
                    }
                } while (true)

                return stringBuilder.toString()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        //当doInBackground完成后，系统会自动调用onPostExecute方法，并将doInBackground方法的返回值传给该方法
        override fun onPostExecute(result: String?) {
            tvShow.text = result
            progressbar.visibility = View.INVISIBLE
        }

        //该方法在执行后台耗时操作前被调用，通常该方法用于完成一些初始化的准备工作，比如在界面上显示进度条等
        override fun onPreExecute() {
            progressbar.visibility = View.VISIBLE
            progressbar.progress = 0
            progressbar.max = 100
        }

        //调用publishProgress()方法更新任务的进度后，会触发该方法
        override fun onProgressUpdate(vararg values: Int?) {
            tvShow.text = "已经读取了" + values[0] + "行"
            progressbar.progress = values[0]!!
        }
    }
}