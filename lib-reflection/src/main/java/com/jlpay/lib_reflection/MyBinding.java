package com.jlpay.lib_reflection;

import android.app.Activity;

import java.lang.reflect.Field;

public class MyBinding {
    public static void bind(Activity activity) {
        for (Field field : activity.getClass().getDeclaredFields()) {
            MyBindView myBindView = field.getAnnotation(MyBindView.class);
            if (myBindView != null) {
                try {
                    field.setAccessible(true);
                    field.set(activity, activity.findViewById(myBindView.value()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
