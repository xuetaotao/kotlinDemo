package com.example.mvvm.base

import android.util.Log
import com.example.mvvm.httpUtils.ResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRepository {

    suspend fun <T : Any> request(call: suspend () -> ResponseData<T>): ResponseData<T> {
        //apply:与run函数类似， 结合了let、with两个函数的作用，即：1.调用同一个对象的多个方法 / 属性时，可以省去对象名重复，直接调用方法名 / 属性即可(with);
        //2.定义一个变量在特定作用域内;3.统一做判空处理(let)
        //但区别在于返回值：run函数返回最后一行的值 / 表达式 ,apply函数返回传入的对象的本身
        return withContext(Dispatchers.IO) {
            call.invoke()
        }.apply {
            Log.d("接口返回数据----->", this.toString())
            //这儿可以对返回结果errorCode做一些特殊处理，比如token失效等，可以通过抛出异常的方式实现
            //例：当token失效时，后台返回errorCode 为 100，下面代码实现,再到baseActivity通过观察error来处理
            when (errorCode) {
                //一般0和200是请求成功，直接返回数据
                0, 200 -> this
                100, 401 -> throw TokenInvalidException(errorMsg)
                403 -> throw NoPermissionsException(errorMsg)
                404 -> throw NotFoundException(errorMsg)
                500 -> throw InterfaceErrException(errorMsg)
                504 -> throw TimeOutErrException(errorMsg)
                else -> throw Exception(errorMsg)
            }
        }
    }

    class TokenInvalidException(msg: String = "token失效，请重新登录") : Exception(msg)
    class NoPermissionsException(msg: String = "您没有操作权限，请联系管理员开通") : Exception(msg)
    class NotFoundException(msg: String = "请求的地址不存在") : Exception(msg)
    class InterfaceErrException(msg: String = "接口请求出错") : Exception(msg)
    class TimeOutErrException(msg: String = "连接超时") : Exception(msg)
}