package com.jlpay.kotlindemo.study_android

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jlpay.kotlindemo.R

class AssetAndRawResActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assets_raw)
    }


    fun assetsStart(view: View) {
        val assets: AssetManager = assets
        try {
            val openFd: AssetFileDescriptor = assets.openFd("douzhe.mp3")
            val mediaPlayer: MediaPlayer = MediaPlayer()
            mediaPlayer.reset()//这里可以不加
//            mediaPlayer.setDataSource(openFd.fileDescriptor)//有点问题
            mediaPlayer.setDataSource(openFd.fileDescriptor, openFd.startOffset, openFd.length)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun rawStart(view: View) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.banyan)
        mediaPlayer.start()
    }

    fun getAssetsFile(view: View) {
        val tv_assets_file: TextView = findViewById(R.id.tv_assets_file)
        //获取assets目录下所有文件
        val list: Array<String>? = assets.list("")
        val stringBuilder = StringBuilder()
        stringBuilder.append("获取assets目录下所有文件如下：\n\n")
        if (list != null && list.isNotEmpty()) {
            for (i in list) {
                stringBuilder.append(i + "\n\n")
            }
        }
        tv_assets_file.text = stringBuilder.toString()
    }

    /**
     * ImageDecoder学习
     */
    fun imageDecoder(view: View) {
        val tv_image_info: TextView = findViewById(R.id.tv_image_info)
        val iv_image: ImageView = findViewById(R.id.iv_image)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            //1.创建ImageDecoder.Source对象
            val createSource: ImageDecoder.Source =
                ImageDecoder.createSource(resources, R.mipmap.baotu)
            //2.执行decodeDrawable方法获取Drawable对象
            try {
                val decodeDrawable: Drawable = ImageDecoder.decodeDrawable(createSource,
                    object : ImageDecoder.OnHeaderDecodedListener {
                        override fun onHeaderDecoded(
                            decoder: ImageDecoder,
                            info: ImageDecoder.ImageInfo,
                            source: ImageDecoder.Source,
                        ) {
                            //通过info参数获取被解码的图片信息
                            ("图片原始宽度：" + info.size.width + "\n" + "图片原始高度：" + info.size.height).also {
                                tv_image_info.text = it
                            }
                            //设置图片解码之后的缩放大小
                            decoder.setTargetSize(info.size.width / 2, info.size.height / 2)
                        }
                    })
                iv_image.setImageDrawable(decodeDrawable)
                //当使用ImageDecoder解码GIF、WEBP等动画图片时，程序将会返回一个AnimatedImageDrawable对象，为了开始执行动画，则执行其start方法
                //如果decodeDrawable是AnimatedImageDrawable的实例，则执行动画
                if (decodeDrawable is AnimatedImageDrawable) {
                    (decodeDrawable as AnimatedImageDrawable).start()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            Toast.makeText(this, "暂无ImageDecoder类", Toast.LENGTH_SHORT).show()
        }
    }


}