package com.jlpay.lib_reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MyBindView {
//    int id();//有默认值时可以不写 id = 1；没有默认值时必须写为如下格式：id = 2
//
//    String name();
//
//    String age() default "zhukai";

    int value();//特殊方法，不需要写 value = 12
}
