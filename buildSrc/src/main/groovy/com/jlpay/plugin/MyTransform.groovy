package com.jlpay.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager

/**
 * Transform是Android官方插件提供给开发者在项目构建阶段
 * 由class到dex转换之前修改class文件的一套api。
 * 目前的典型应用就是字节码插桩技术
 *
 * 所有的 Transform 都会被转化为 Gradle中的 Task 任务
 * 我们自定义的 Transform 是第一个执行的 Transform
 */
class MyTransform extends Transform {

    /**
     * Transform的名称，也就是被转化为 ，就取决于这里
     * @return
     */
    @Override
    String getName() {
        def name = "test"
        return name
    }

    /**
     * Transform处理内容
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        //处理所有的class集合
        return TransformManager.CONTENT_CLASS
    }

    /**
     * Transform的作用域
     * 这个插件能够接收哪些范围的处理内容(就是上面的方法，比如这里就是class)
     * 这个方法里的参数就制定了可以处理的范围是：
     * App自己的class文件，项目下的其他模块，app引用的第三方库，即
     * Scope.PROJECT, Scope.SUB_PROJECTS, Scope.EXTERNAL_LIBRARIES
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 是否增量
     * @return true 增量，即只处理有修改的地方
     */
    @Override
    boolean isIncremental() {
        return true
    }

    /**
     * 处理过程，我们自定义的 Transform 是第一个执行的 Transform
     * 这个 Transform 的输出会作为下一个 Transform 的输入
     * 如果我们没有指定输出(outputProvider)，就会默认没有我们这个 Transform
     * @param transformInvocation
     * @throws TransformException* @throws InterruptedException* @throws IOException
     */
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//        super.transform(transformInvocation)
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        //如果isIncremental返回false，即每次构建都当作第一次构建，没有所谓的增量构建，那么可以加上下面这步
//        outputProvider.deleteAll()
        Collection<TransformInput> inputs = transformInvocation.inputs
        inputs.each {
            //jarInputs和dirInputs合起来就是所有要打包进apk的class文件
            //所有jar包的输入，就包括项目下的其他模块，和第三方库的依赖
            def jarInputs = it.jarInputs
            jarInputs.each {

            }
            //所有目录的输入，即自己写的代码，比如app模块引入了这个插件，就是app模块下的自己写的代码
            def dirInputs = it.directoryInputs
            dirInputs.each {
                def changedFiles = it.changedFiles
                changedFiles.entrySet().each {
                    println(it.key.name + it.value.name)
                }
            }
        }
    }
}