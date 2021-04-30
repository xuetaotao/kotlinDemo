package com.jlpay.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class HenCoder implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        //创建扩展对象(.class可以省略)，target是当前项目
//        def extension = target.extensions.create("hencoder", HenCoderExtension.class)
        def extension = target.extensions.create("hencoder", HenCoderExtension)
        target.afterEvaluate {//在阶段之间插入代码，先执行扩展的初始化
            println("Hello ${extension.name}!")
        }
//        def transform = new HenCoderTransform()
//        def baseExtension = target.extensions.getByType(BaseExtension)
//        baseExtension.registerTransform(transform)
    }
}