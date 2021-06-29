// IPet.aidl
package com.jlpay.remotecustomservice;

// Declare any non-default types here with import statements
import com.jlpay.remotecustomservice.Person;
import com.jlpay.remotecustomservice.Pet;

/**
 * 步骤1. 定义自定义数据类型，创建一个 .aidl 文件声明刚才定义的类型，注意与具体业务 .aidl 文件的不同；新建一个AIDL接口文件，导入自定义类型数据
 * 步骤2. 在新建AIDL文件里定义Service需要与Activity进行通信的内容（方法），并进行编译（Make Project）
 * 步骤3：在Service子类中实现AIDL中定义的接口方法，并定义生命周期的方法（onCreat、onBind()、blabla）
 * 步骤4：在AndroidMainfest.xml中注册服务 & 声明为远程服务
 */
interface IPet {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    //定义一个Person对象作为传入参数
    //在AIDL接口中定义方法时，需要指定形参的传递模式。对于Java或者Kotlin来说，一般采用的都是传入参数的方式，因此上面指定为in 模式
    List<Pet> getPets(in Person owner);
}

//Android需要AIDL来定义远程接口，AIDL接口语法与Java接口语法很相似
//AIDL中支持以下的数据类型
//1. 基本数据类型
//2. String 和CharSequence
//3. List 和 Map ,List和Map 对象的元素必须是AIDL支持的数据类型;
//4. AIDL自动生成的接口（需要导入-import）
//5. 实现android.os.Parcelable 接口的类（需要导入-import)
