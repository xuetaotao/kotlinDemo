package com.jlpay.kotlindemo.daily.practice;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MyInnerBinding {
    public static void bind(Activity activity) {
//        activity.textView2 = activity.findViewById(R.id.textView2);

        try {
            Class bindingClass = Class.forName(activity.getClass().getCanonicalName() + "Binding");
            Constructor constructor = bindingClass.getDeclaredConstructor(activity.getClass());
            constructor.newInstance(activity);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
