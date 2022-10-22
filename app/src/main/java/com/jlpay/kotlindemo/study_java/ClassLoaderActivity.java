package com.jlpay.kotlindemo.study_java;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;
import com.jlpay.kotlindemo.daily.MainActivity;

import dalvik.system.PathClassLoader;

/**
 * ClassLoader的学习
 */
public class ClassLoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classloader);
    }

    public void onClick(View view) {

    }

    private void testClassLoader() throws ClassNotFoundException {
        ClassLoader classLoader2 = Application.class.getClassLoader();//BootClassLoader
        ClassLoader classLoader1 = AppCompatActivity.class.getClassLoader();//PathClassLoader
        ClassLoader classLoader = Activity.class.getClassLoader();//BootClassLoader
        ClassLoader classLoader3 = MainActivity.class.getClassLoader();//PathClassLoader
        ClassLoader classLoader4 = getClassLoader();//PathClassLoader
        Log.e("LibTestJavaActivity", classLoader2.toString() + "\n\n" + classLoader1.toString() + "\n\n" +
                classLoader.toString() + "\n\n" + classLoader3.toString() + "\n\n" + classLoader4.toString());
        Class<?> classLoaderActivity = classLoader4.loadClass("ClassLoaderActivity");
    }

    private void relatedClass() {
        //Class中会包含一个ClassLoader，就是加载这个Class的ClassLoader
        Class<? extends ClassLoaderActivity> aClass = this.getClass();
        PathClassLoader pathClassLoader;
        //获取程序的PathClassLoader对象
        pathClassLoader = (PathClassLoader) getClassLoader();
    }
}
