package com.jlpay.plugin


import com.android.build.gradle.internal.plugins.AppPlugin
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

//import com.android.build.gradle.AppPlugin

/**
 * 自定义插件应用场景：
 * 多用于字节码插桩的场景（在这里获取class文件）
 *
 * groovy中支持写java代码
 */
class HenCoderPlugin implements Plugin<Project> {

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
                //这两行与下面等效
                HenCoderExtension ext = target.getExtensions().findByType(HenCoderExtension.class);
                println(ext.getName());
                //下面有问题，待解决。Caused by: org.gradle.api.UnknownDomainObjectException: Extension of type 'AppExtension' does not exist. Currently registered extension types: [ExtraPropertiesExtension, DefaultArtifactPublicationSet, ReportingExtension, SourceSetContainer, JavaPluginExtension, HenCoderExtension]
                //获取到android的所有变体，findByType与getByType都可以，代码里最终都是调同一方法
//                AppExtension android = project.getExtensions().getByType(AppExtension.class);
//                DomainObjectSet<ApplicationVariant> applicationVariants = android.getApplicationVariants();
//                for (ApplicationVariant applicationVariant : applicationVariants) {
//                    String name = applicationVariant.getName();
//                    if (name.contains("debug") && !ext.getDebugOn()) {
//                        return;
//                    }
//                }
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