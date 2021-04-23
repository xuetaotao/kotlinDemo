package com.jlpay.kotlindemo.application

import com.tencent.tinker.loader.app.TinkerApplication
import com.tencent.tinker.loader.shareutil.ShareConstants

/**
 * 这个类集成TinkerApplication类，这里面不做任何操作，所有Application的代码都会放到ApplicationLike继承类当中
 * 参数解析
 * 参数1：tinkerFlags 表示Tinker支持的类型 dex only、library only or all suuport，default: TINKER_ENABLE_ALL
 * 参数2：delegateClassName Application代理类 这里填写你自定义的ApplicationLike
 * 参数3：loaderClassName Tinker的加载器，使用默认即可
 * 参数4：tinkerLoadVerifyFlag 加载dex或者lib是否验证md5，默认为false
 */
class SampleApplication() : TinkerApplication(
    ShareConstants.TINKER_ENABLE_ALL,
    "com.jlpay.kotlindemo.application.MyApplication",
    "com.tencent.tinker.loader.TinkerLoader",
    false
) {
}


//另一种Kotlin写法
//class SampleApplication : TinkerApplication {
//    constructor() : super(
//        ShareConstants.TINKER_ENABLE_ALL,
//        "com.jlpay.kotlindemo.application.MyApplication",
//        "com.tencent.tinker.loader.TinkerLoader",
//        false
//    )
//}