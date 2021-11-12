package com.jlpay.kotlindemo.net;

import android.util.Log;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * 解决三个问题：
 * 1.部分下游订阅使用 Consumer ，导致的不易管理，这次统一收归到 BaseObserver 管理
 * 2.部分Rxjava链是将几个网络请求串起来，并整理得出最后的数据，但是最后的数据并非继承 BResponse
 * 3.
 * <p>
 * 这里主要还是对网络请求返回的extends BResponse的数据进行处理
 * <p>
 * Rxjava 使用 : .map(new BaseFunction<AgentProtocolBean>(){}) //注意 {} 不要漏了，否则getType()方法就获取不到泛型 T
 *
 * @param <T>
 */
public class BaseFunction<T> implements Function<ResponseBody, T> {

    @Override
    public T apply(ResponseBody t) throws Exception {
        String responseBodyStr = t.string();
        String result = getOriginalResponseStr(responseBodyStr);
//        BResponse bResponse = GsonUtil.get(responseBodyStr, BResponse.class);
//        if (bResponse == null) {
//            throw new JSONException("解析异常");
//        }
//        if (bResponse.isOk()) {
//            Type type = getType();
//            if (type == null) {
//                throw new RuntimeException("数据解析异常");
//            }
//            T data = GsonUtil.get(responseBodyStr, type);
//            if (data == null) {
//                throw new RuntimeException("数据解析异常");
//            }
//            return data;
//
//        } else {
//            ErrorHandle.handleException(bResponse.getRet_msg(), bResponse.getRet_code());
//            throw new ExceptionHandle.JLException(bResponse.getRet_msg(), bResponse.getRet_code());
//        }
        return null;//临时加的，避免报错
    }


    /**
     * 获取报文的原始内容
     *
     * @param responseBodyStr 报文
     * @return 报文的原始内容
     */
    private String getOriginalResponseStr(String responseBodyStr) {
        return responseBodyStr;
    }


    /**
     * 获取父类的泛型参数
     * getGenericSuperclass() : 获得带有泛型的父类，返回值为：BaseDao<Employee, String>
     * ParameterizedType ： 参数化类型，即泛型
     * getActualTypeArguments()[] : 获取参数化类型的数组，泛型可能有多个
     * <p>
     * 6. 反射和泛型：
     * 6.1: getSuperClass():获取父类，返回值为：BaseDao
     * 6.2: getGenericSuperClass:获取带泛型参数的父类，返回值为：BaseDao<Employee, String>
     * 6.3：Type的子接口：ParameterizedType 的 getActualTypeArguments()获取泛型参数的数组
     *
     * @return
     */
    private Type getType() {
        Type genericSuperclass = getClass().getGenericSuperclass();//获取带泛型参数的父类，返回值为：BaseDao<Employee, String>
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("须传入指定类型");
        }
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            //Type的子接口：ParameterizedType 的 getActualTypeArguments()获取泛型参数的数组
            //泛型可能有多个，对应这里多个数组的值，此处默认只取第一个
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            //类型参数也有可能不是Class类型, 例如：BaseDao<BaseDao<User>>,获取到的就不是Class,而又是ParameterizedType，即嵌套的
            if (actualTypeArguments[0] instanceof Class) {
                return actualTypeArguments[0];
            }
        }
        return null;
    }

    /**
     * 获取接口的泛型参数
     */
    private void getInterfaceType(Object obj) {
//    private Type getInterfaceType(Object obj) {
//    private Class getInterfaceType(Object obj) {
        Type[] genericInterfaces = obj.getClass().getGenericInterfaces();
        //一个类可能实现多个接口,每个接口上定义的泛型类型都可取到
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                //泛型可能有多个，对应这里多个数组的值，此处默认只取第一个
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments[0] instanceof Class) {
//                    return (Class) actualTypeArguments[0];
                    Log.e("TAG", "getInterfaceType: " + ((Class<?>) actualTypeArguments[0]).getName());
                }
            }
        }

    }
}
