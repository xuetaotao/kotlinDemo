package com.jlpay.plugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.gradle.internal.impldep.org.apache.commons.codec.digest.DigestUtils
import com.google.common.collect.FluentIterable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * ASM结合Transform的使用，实现字节码插桩
 */
class AsmTransform extends Transform {

    /**
     * Transform的名称，也就是被转化为 ，就取决于这里
     * @return
     */
    @Override
    String getName() {
        return "Asm"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        //只处理引入这个插件的模块下的class文件
        //即自己写的代码，比如app模块引入了这个插件，就是app模块下的自己写的代码
        return TransformManager.PROJECT_ONLY
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//        super.transform(transformInvocation)
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()
        //清理文件
        outputProvider.deleteAll()

        Collection<TransformInput> inputs = transformInvocation.inputs
        inputs.each {
            //因为getScopes方法指定了PROJECT_ONLY，所以我们就只用管引入插件模块的class文件
            //所有目录的输入，即自己写的代码，比如app模块引入了这个插件，就是app模块下的自己写的代码
            def dirInputs = it.directoryInputs
            dirInputs.each {
                String dirName = it.name
                File src = it.getFile()
                println("目录:" + src)
                String md5Name = DigestUtils.md5Hex(src.absolutePath)
                //我们自定义的 Transform 是第一个执行的 Transform，要指定输出(outputProvider)
                File dest = outputProvider.getContentLocation(dirName + md5Name, it.contentTypes,
                        it.scopes, Format.DIRECTORY)
                //字节码插桩 TODO 暂时还没有吸收掌握
                processInject(src, dest)
            }
        }
    }

    void processInject(File src, File dest) {
        String dir = src.getAbsolutePath();
        FluentIterable<File> allFiles = FileUtils.getAllFiles(src);
        for (File file : allFiles) {
            FileInputStream fis = new FileInputStream(file);
            //插桩
            ClassReader cr = new ClassReader(fis);
            // 写出器
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            //分析，处理结果写入cw
            cr.accept(new ClassInjectTimeVisitor(cw, file.getName()), ClassReader.EXPAND_FRAMES);

            byte[] newClassBytes = cw.toByteArray();
            String absolutePath = file.getAbsolutePath();
            String fullClassPath = absolutePath.replace(dir, "");
            File outFile = new File(dest, fullClassPath);
            FileUtils.mkdirs(outFile.getParentFile());
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(newClassBytes);
            fos.close();
        }
    }
}