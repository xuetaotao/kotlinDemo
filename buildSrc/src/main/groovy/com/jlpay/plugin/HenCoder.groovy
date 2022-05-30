package com.jlpay.plugin

import com.android.build.gradle.internal.plugins.AppPlugin
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

//import com.android.build.gradle.AppPlugin

class HenCoder implements Plugin<Project> {

    private Project mProject = null

    @Override
    public void apply(Project target) {
        //这个Project指的就是我们这个工程，就是当前项目
        mProject = target;
        //判断引用该插件的gradle文件中是否引用了 'com.android.application' 插件
        def hasAppPlugin = target.getPlugins().hasPlugin(AppPlugin.class)
//        if (!hasAppPlugin) {
//            throw new GradleException("this plugin need use with android application plugin")
//        }

        //创建扩展对象(.class可以省略), create方法就是创建扩展
//        def extension = target.extensions.create("hencoder", HenCoderExtension.class)
        HenCoderExtension extension = target.extensions.create("hencoder", HenCoderExtension)
        target.afterEvaluate(new Action<Project>() {
            @Override
            void execute(Project project) {
                //与下面等效
                HenCoderExtension ext = target.getExtensions().findByType(HenCoderExtension.class)
                println(ext.getName())
            }
        })
        target.afterEvaluate {
            //扔物线：在阶段之间插入代码，先执行扩展的初始化
            //享学：配置阶段，解析完（即执行完） build.gradle 之后回调执行
            println("Hello ${extension.name}!")
        }
//        def transform = new HenCoderTransform()
//        def baseExtension = target.extensions.getByType(BaseExtension)
//        baseExtension.registerTransform(transform)
    }
}