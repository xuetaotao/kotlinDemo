// ICat.aidl
// 在新建的AIDL_Service1.aidl里声明需要与Activity进行通信的方法
package com.jlpay.remoteservice;

// Declare any non-default types here with import statements
/**
 * 步骤1. 新建一个AIDL文件
 * 步骤2. 在新建AIDL文件里定义Service需要与Activity进行通信的内容（方法），并进行编译（Make Project）
 * 步骤3：在Service子类中实现AIDL中定义的接口方法，并定义生命周期的方法（onCreat、onBind()、blabla）
 * 步骤4：在AndroidMainfest.xml中注册服务 & 声明为远程服务
 */
interface ICat {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getColor();
    double getWeight();
}

//Android需要AIDL来定义远程接口，AIDL接口语法与Java接口语法很相似
//AIDL中支持以下的数据类型
//1. 基本数据类型
//2. String 和CharSequence
//3. List 和 Map ,List和Map 对象的元素必须是AIDL支持的数据类型;
//4. AIDL自动生成的接口（需要导入-import）
//5. 实现android.os.Parcelable 接口的类（需要导入-import)

