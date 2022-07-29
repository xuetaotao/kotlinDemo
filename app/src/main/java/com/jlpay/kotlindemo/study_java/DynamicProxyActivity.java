package com.jlpay.kotlindemo.study_java;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jlpay.kotlindemo.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 动态代理
 */
public class DynamicProxyActivity extends AppCompatActivity {

    private static final String TARGET_INTENT = "target_intent";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_proxy);

        hookAMS();
        hookHandler();
    }

    public void dynamicProxyDemo(View view) {
//        Toast.makeText(this, "DynamicProxyActivity", Toast.LENGTH_SHORT).show();
        launchUnRegisterActivity();
    }

    /**
     * 启动一个未注册的Activity
     * 插件化的核心思想
     */
    public void launchUnRegisterActivity() {
        Intent intent = new Intent();
        //方式一
        //注意：ComponentName构造方法的第二个参数(String cls)必须是全包名
//        Log.e("TAG", "launchUnRegisterActivity: " + this.getPackageName());//com.jlpay.kotlindemo
//        intent.setComponent(new ComponentName("com.jlpay.kotlindemo",
//                "com.jlpay.kotlindemo.study_android.MediaPlayerActivity"));
        //方式二
//        intent.setComponent(new ComponentName(this, MediaPlayerActivity.class));

        //启动未注册的Activity
        //直接启动报错如下：
        //java.lang.reflect.InvocationTargetException
        //android.content.ActivityNotFoundException: Unable to find explicit activity class {com.jlpay.kotlindemo.study_android/UnRegisterActivity}; have you declared this activity in your AndroidManifest.xml?
        //因此我们要使用Hook，就是使用反射或者动态代理等技术，实现改变原有执行流程的目的，欺骗AMS启动未注册的这个Activity
        intent.setComponent(new ComponentName("com.jlpay.kotlindemo",
                "com.jlpay.kotlindemo.study_android.UnRegisterActivity"));

        startActivity(intent);
    }

    //第一个hook点
    //在AMS检测Intent之前，将Intent中要启动的未注册的Activity换成代理Activity(已注册的)
    //使用动态代理生成静态接口IActivityTaskManager的对象
    public void hookAMS() {
        try {
            Class<?> activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
            Field iActivityTaskManagerSingletonField = activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
            iActivityTaskManagerSingletonField.setAccessible(true);
            //获取实例对象，因为是静态，直接传Null
            Object singleton = iActivityTaskManagerSingletonField.get(null);

            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            Method getMethod = singletonClass.getMethod("get");
            //获取当前实例对象的mInstance属性的值，是IActivityTaskManager类型
            Object mInstance = getMethod.invoke(singleton);

            //这里的话只适配了Android10以后(ATMS)，Android10之前使用的是AMS
            Class<?> iActivityTaskManagerClass = Class.forName("android.app.IActivityTaskManager");

            Object mInstanceProxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityTaskManagerClass}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if ("startActivity".equals(method.getName())) {
                                int index = -1;
                                // 获取 Intent 参数在 args 数组中的index值
                                for (int i = 0; i < args.length; i++) {
                                    if (args[i] instanceof Intent) {
                                        index = i;
                                        break;
                                    }
                                }
                                // 生成代理proxyIntent -- 孙悟空（代理）的Intent
                                Intent proxyIntent = new Intent();
                                // 这个包名是宿主的
                                proxyIntent.setClassName("com.jlpay.kotlindemo",
                                        "com.jlpay.kotlindemo.study_android.MediaPlayerActivity");
                                // 原始Intent能丢掉吗？不能，需要保存原始的Intent对象
                                Intent intent = (Intent) args[index];
                                proxyIntent.putExtra(TARGET_INTENT, intent);
                                // 使用proxyIntent替换数组中的Intent
                                args[index] = proxyIntent;
                            }
                            // 执行原来流程
                            return method.invoke(mInstance, args);
                        }
                    });

            // 用代理的对象替换系统的对象
            mInstanceField.set(singleton, mInstanceProxy);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //第二个hook点
    //在AMS检测之后，在Activity生命周期（OnCreate，onStart）执行之前，再将代理Activity替换回来变成未注册的Activity
    //使用反射
    public void hookHandler() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            //获取当前属性的真实值
            Object activityThread = sCurrentActivityThreadField.get(null);
            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            final Handler mH = (Handler) mHField.get(activityThread);

            Class<Handler> handlerClass = Handler.class;
            Field mCallbackField = handlerClass.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            mCallbackField.set(mH, new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    switch (msg.what) {
                        //ActivityThread.Handler.EXECUTE_TRANSACTIONL
                        case 159:
                            //final ClientTransaction transaction = (ClientTransaction) msg.obj;
                            // 获取 List<ClientTransactionItem> mActivityCallbacks 对象
                            try {
                                Class<?> clientTransactionClass = msg.obj.getClass();
                                Field mActivityCallbacksField = clientTransactionClass.getDeclaredField("mActivityCallbacks");
                                mActivityCallbacksField.setAccessible(true);
                                List mActivityCallbacks = (List) mActivityCallbacksField.get(msg.obj);

                                for (int i = 0; i < mActivityCallbacks.size(); i++) {
                                    // 打印 mActivityCallbacks 的所有item:
                                    //android.app.servertransaction.WindowVisibilityItem
                                    //android.app.servertransaction.LaunchActivityItem

                                    // 如果是 LaunchActivityItem，则获取该类中的 mIntent 值，即 proxyIntent
                                    if (mActivityCallbacks.get(i).getClass().getName()
                                            .equals("android.app.servertransaction.LaunchActivityItem")) {
                                        Object launchActivityItem = mActivityCallbacks.get(i);
                                        Class<?> launchActivityItemClass = launchActivityItem.getClass();
                                        Field mIntentField = launchActivityItemClass.getDeclaredField("mIntent");
                                        mIntentField.setAccessible(true);
                                        Intent proxyIntent = (Intent) mIntentField.get(launchActivityItem);
                                        // 获取启动插件的 Intent，并替换回来
                                        if (proxyIntent != null) {
                                            Intent intent = proxyIntent.getParcelableExtra(TARGET_INTENT);
                                            if (intent != null) {
                                                mIntentField.set(launchActivityItem, intent);
                                            }
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    //让这里返回false，在这里修改msg替换Intent
                    //这样的话不会影响原来流程中handleMessage方法的执行
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
