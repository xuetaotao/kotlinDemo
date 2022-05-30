package com.jlpay.lib_processor;

import com.jlpay.lib_annotations.MyBindView;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class MyBindingProcessor extends AbstractProcessor {

    //生成代码的工具
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    //处理注解的过程, 在编译的时候干活
    //如果没有在任何地方使用注解，这个函数是不会工作的
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
//        System.out.println("MyBindingProcessor is Run");

        //测试代码：测试生成一个 java文件
//        ClassName className = ClassName.get("com.jlpay.kotlindemo.ui.main.hencoder", "TestIO");
//        TypeSpec buildClass = TypeSpec.classBuilder(className).build();
//        try {
//            JavaFile.builder("com.jlpay.kotlindemo.ui.main.hencoder", buildClass)
//                    .build().writeTo(filer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //正式代码，使用 javapoet 生成
        for (Element element : roundEnvironment.getRootElements()) {
            String packageStr = element.getEnclosingElement().toString();//获取包名
            String classStr = element.getSimpleName().toString();//获取类名
            ClassName className = ClassName.get(packageStr, classStr + "Binding");//拼接要生成的类名
            MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
//                    .addParameter(String[].class,"test")
                    .addParameter(ClassName.get(packageStr, classStr), "activity");
            boolean hasBinding = false;

            for (Element enclosedElement : element.getEnclosedElements()) {
                //获取当前类下面的属性
                if (enclosedElement.getKind() == ElementKind.FIELD) {
                    MyBindView myBindView = enclosedElement.getAnnotation(MyBindView.class);
                    if (myBindView != null) {
                        hasBinding = true;
                        constructorBuilder.addStatement("activity.$N = activity.findViewById($L)",
                                enclosedElement.getSimpleName(), myBindView.value());
                    }
                }
            }

            TypeSpec buildClass = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC)//修改权限
                    .addMethod(constructorBuilder.build())
                    .build();

            if (hasBinding) {
                try {
                    JavaFile.builder(packageStr, buildClass).build().writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(MyBindView.class.getCanonicalName());
    }
}