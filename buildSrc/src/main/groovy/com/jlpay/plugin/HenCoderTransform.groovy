package com.jlpay.plugin

//import com.android.build.api.transform.*
//import com.android.build.gradle.internal.pipeline.TransformManager
//import com.android.utils.FileUtils
//
////TODO 待解决：Transform等类引入不了
//ASM 简介
//https://www.jianshu.com/p/a85e8f83fa14
//看完这一篇，你也可以自如地掌握字节码插桩
//https://mp.weixin.qq.com/s/gEtsNEWPHsSt52D7J00pug
//
//class HenCoderTransform extends Transform {
//    @Override
//    String getName() {
//        return "hencoder"
//    }
//
//    @Override
//    Set<QualifiedContent.ContentType> getInputTypes() {
//        return TransformManager.CONTENT_CLASS
//    }
//
//    @Override
//    Set<? super QualifiedContent.Scope> getScopes() {
//        return TransformManager.SCOPE_FULL_PROJECT
//    }
//
//    @Override
//    boolean isIncremental() {
//        return false
//    }
//
//    @Override
//    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
////        super.transform(transformInvocation)
//        def inputs = transformInvocation.inputs
//        def outputProvider = transformInvocation.outputProvider
//        inputs.each {
//            it.jarInputs.each {
//                File dest = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.JAR)
//                FileUtils.copyFile(it.file, dest)
//            }
//            it.directoryInputs.each {
//                File dest = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.DIRECTORY)
//                FileUtils.copyDirectory(it.file, dest)
//            }
//        }
//    }
//}

//javassist、ASM(面向切面编程的工具)