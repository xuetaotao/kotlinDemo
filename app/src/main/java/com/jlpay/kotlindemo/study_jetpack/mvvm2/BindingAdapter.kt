package com.jlpay.kotlindemo.study_jetpack.mvvm2

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class BindingAdapter {

    companion object {

        /**
         * 注意到ImageView的标签内声明了一个url属性，并且和data内的image的数据进行了绑定。然而ImageView并没有这个属性，这时就需要用到Databinding的自定义属性了
         * 编写一个BindingAdapter类用来声明自定义属性，并使用@BindingAdapter注解来让编译器知道你的属性名。
         * （注：这里要在app:build.gradle下声明：apply plugin: 'kotlin-kapt'）
         * 在方法中来对属性值进行处理，这里使用了Glide来进行网络图片的加载
         */
        @JvmStatic
        @BindingAdapter("url")
        fun setImageUrl(imageView: ImageView, url: String) {
            Glide.with(imageView.context).load(url).into(imageView)
        }
    }

}