package com.jlpay.kotlindemo.study_java;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射学习
 * https://blog.csdn.net/sjh66655/article/details/110467429
 */
public class ReflectActivity extends AppCompatActivity {

    private String TAG = ReflectActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getCLass();
        getConstructors();
        getFields();
        getMethods();
    }


    /**
     * 获取成员方法并调用：
     * <p>
     * 1.批量的：
     * public Method[] getMethods():获取所有"公有方法"；（包含了父类的方法也包含Object类）
     * public Method[] getDeclaredMethods():获取所有的成员方法，包括私有的(不包括继承的)
     * 2.获取单个的：
     * public Method getMethod(String name,Class<?>... parameterTypes):
     * 参数：
     * name : 方法名；
     * Class ... : 形参的Class类型对象
     * public Method getDeclaredMethod(String name,Class<?>... parameterTypes)
     * <p>
     * 调用方法：
     * Method --> public Object invoke(Object obj,Object... args):
     * 参数说明：
     * obj : 要调用方法的对象；
     * args:调用方式时所传递的实参；
     * )
     */
    private void getMethods() {
        //1.加载Class对象
        try {
            Class clazz = Class.forName("com.jlpay.kotlindemo.study_java.Student");
            //2.获取所有公有方法 （包含了父类的方法也包含Object类）
            Log.e(TAG, "***************获取所有的<公有>方法*******************");
            Method[] clazzMethods = clazz.getMethods();
            for (Method method : clazzMethods) {
                Log.e(TAG, method.toString());
            }

            Log.e(TAG, "***************获取所有的方法，包括私有的*******************");
            Method[] clazzDeclaredMethods = clazz.getDeclaredMethods();
            for (Method method : clazzDeclaredMethods) {
                Log.e(TAG, method.toString());
            }

            Log.e(TAG, "***************获取公有的show1()方法*******************");
            try {
                Method show1Method = clazz.getMethod("show1", String.class);
                Log.e(TAG, show1Method.toString());
                //实例化一个Student对象
                Object instance = clazz.getConstructor().newInstance();
                if (!show1Method.isAccessible()) {
                    show1Method.setAccessible(true);
                }
                show1Method.invoke(instance, "刘德华");
                //如果是静态方法，第一个参数传null就行，也可以传实例对象

            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "***************获取私有的show4()方法******************");
            try {
                Method show4Method = clazz.getDeclaredMethod("show4", int.class);
                Log.e(TAG, show4Method.toString());
                //实例化一个Student对象
                Object newInstance = clazz.getConstructor().newInstance();
                show4Method.setAccessible(true);
                Object result = show4Method.invoke(newInstance, 8);
                Log.e(TAG, "show4MethodResult: " + result);

            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取成员变量并调用：
     * <p>
     * 1.批量的
     * 1).Field[] getFields():获取所有的"公有字段"
     * 2).Field[] getDeclaredFields():获取所有字段，包括：私有、受保护、默认、公有；
     * 2.获取单个的：
     * 1).public Field getField(String fieldName):获取某个"公有的"字段；
     * 2).public Field getDeclaredField(String fieldName):获取某个字段(可以是私有的)
     * <p>
     * 设置字段的值：
     * Field --> public void set(Object obj,Object value):
     * 参数说明：
     * 1.obj:要设置的字段所在的对象；
     * 2.value:要为字段设置的值；
     */
    private void getFields() {
        //1.加载Class对象
        try {
            Class clazz = Class.forName("com.jlpay.kotlindemo.study_java.Student");
            //2.获取字段
            Log.e(TAG, "************获取所有公有的字段属性********************");
            //该方法能获取到父类的公有字段
            Field[] clazzFields = clazz.getFields();
            for (Field field : clazzFields) {
                Log.e(TAG, field.toString());
            }

            Log.e(TAG, "************获取当前类(不包括父类)所有的字段属性(包括私有、受保护、默认的)********************");
            Field[] clazzDeclaredFields = clazz.getDeclaredFields();
            for (Field field : clazzDeclaredFields) {
                Log.e(TAG, field.toString());
            }

            Log.e(TAG, "*************获取公有字段**并调用***********************************");
            try {
                Field name = clazz.getField("name");
                Log.e(TAG, "nameField:" + name);
                //获取一个对象,产生Student对象--》Student stu = new Student();
                Object instance = clazz.getConstructor().newInstance();
                name.set(instance, "刘德华");
                Student student = (Student) instance;
                Log.e(TAG, "验证姓名：" + student.name);

            } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "**************获取私有字段****并调用********************************");
            try {
                Field phoneNum = clazz.getDeclaredField("phoneNum");
                Log.e(TAG, "phoneNumFields: " + phoneNum);
                Object newInstance = clazz.getConstructor().newInstance();
                phoneNum.setAccessible(true);
                phoneNum.set(newInstance, "18888889999");
                Object o = phoneNum.get(newInstance);
                Log.e(TAG, "getFields: " + o);
                Student newStudent = (Student) newInstance;
                Log.e(TAG, "电话: " + newStudent);

            } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过Class对象可以获取某个类中的：构造方法、成员变量、成员方法；并访问成员；
     * 1.获取构造方法：
     * 1).批量的方法：
     * public Constructor[] getConstructors()：所有"公有的"构造方法
     * public Constructor[] getDeclaredConstructors()：获取所有的构造方法(包括私有、受保护、默认、公有)
     * <p>
     * 2).获取单个的方法，并调用：
     * public Constructor getConstructor(Class... parameterTypes):获取单个的"公有的"构造方法：
     * public Constructor getDeclaredConstructor(Class... parameterTypes):获取"某个构造方法"可以是私有的，或受保护、默认、公有；
     * <p>
     * 调用构造方法：
     * Constructor-->newInstance(Object... initargs)
     */
    private void getConstructors() {
        try {
            //1.加载Class对象
            Class clazz = Class.forName("com.jlpay.kotlindemo.study_java.Student");
            //2.获取所有公有构造方法
            Log.e(TAG, "**********************所有公有构造方法*********************************");
            //获取所有"公有的(public)"构造方法，不包括父类
            Constructor[] clazzConstructors = clazz.getConstructors();
            for (Constructor c : clazzConstructors) {
                Log.e(TAG, String.valueOf(c));
            }

            Log.e(TAG, "************所有的构造方法(包括：私有、受保护、默认、公有)***************");
            Constructor[] clazzDeclaredConstructors = clazz.getDeclaredConstructors();
            for (Constructor constructor : clazzDeclaredConstructors) {
                Log.e(TAG, String.valueOf(constructor));
            }

            Log.e(TAG, "*****************获取公有、无参的构造方法*******************************");
            try {
                Constructor clazzConstructor = clazz.getConstructor();
                Log.e(TAG, String.valueOf(clazzConstructor));
                //1>、因为是无参的构造方法所以类型是一个null,不写也可以：这里需要的是一个参数的类型，切记是类型
                //2>、返回的是描述这个无参构造函数的类对象

                //调用构造方法
                Object instance = clazzConstructor.newInstance();
                Log.e(TAG, "instance: " + instance.getClass().getSimpleName());
                Student student = (Student) instance;

            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "******************获取私有构造方法，并调用*******************************");
            try {
//                Constructor clazzDeclaredConstructor = clazz.getDeclaredConstructor(char.class);
                Constructor clazzDeclaredConstructor = clazz.getDeclaredConstructor(int.class);
                Log.e(TAG, String.valueOf(clazzDeclaredConstructor));
                //1>、无参的构造方法所以类型是一个null,不写也可以：这里需要的是一个参数的类型，切记是类型
                //调用构造方法
                clazzDeclaredConstructor.setAccessible(true);
                Object instance = clazzDeclaredConstructor.newInstance(8);
                Log.e(TAG, "instance: " + instance.getClass().getSimpleName());

            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Class对象的三种方式
     */
    private void getCLass() {
        //1. Object ——> getClass();
        JavaListNode javaListNode = new JavaListNode();
//        Class<? extends JavaListNode> aClass = javaListNode.getClass();
        Class bClass = javaListNode.getClass();
        Log.e(TAG, "getCLassWay: bClass:" + bClass.getName());

        // 2 任何数据类型（包括基本数据类型）都有一个“静态”的class属性
        Class dClass = JavaListNode.class;
        Log.e(TAG, "getCLassWay: dClass:" + dClass.getName());
        boolean result = bClass == dClass;
        Log.e(TAG, "getCLassWay: " + result);

        //3 通过Class类的静态方法：forName（String  className）(常用)
        try {
            Class<?> eClass = Class.forName("com.jlpay.kotlindemo.study_java.JavaListNode");
            Log.e(TAG, "getCLassWay: eClass" + eClass.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //4 获取父类的class
        // getSuperClass():获取父类，返回值为：BaseDao
        // getGenericSuperClass:获取带泛型参数的父类，返回值为：BaseDao<Employee, String>
        // Type的子接口：ParameterizedType 的 getActualTypeArguments()获取泛型参数的数组
        Class superclass = bClass.getSuperclass();
    }

    /**
     * 获取注解
     */
    private void getAnnotation() {
        //TODO
    }

    /**
     * 反射创建数组
     */
    private void createArray() {
        String[] instance = (String[]) Array.newInstance(String.class, 10);
    }
}
