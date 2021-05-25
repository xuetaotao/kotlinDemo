package com.jlpay.kotlindemo.ui.main.rxjava

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import org.jetbrains.annotations.NotNull

/**
 * 实现了 LifecycleObserver 接口的类可以和实现了 LifecycleOwner 接口的类无缝工作，因为 LifecycleOwner 可以提供一个 Lifecycle 对象，
 * 而 LifecycleObserver 就正需要对这个 Lifecycle 对象进行监听呢。
 * LifecycleOwner 是从特定的类（比如 Activity 或者 Fragment 等）中抽象出来的Lifecycle 的持有者
 *
 * 参考：AutoDispose解决RxJava内存泄漏
 * https://blog.csdn.net/qq_32534441/article/details/105142073?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_baidulandingword-1&spm=1001.2101.3001.4242
 * https://juejin.cn/post/6844903784166998023
 */
interface IPresenter : LifecycleObserver {

    //使用Google官方Lifecycle组件
    // 使用注解  @OnLifecycleEvent 来表明该方法需要监听指定的生命周期事件
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(@NotNull owner: LifecycleOwner)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(@NotNull owner: LifecycleOwner)

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onLifecycleChanged(@NotNull owner: LifecycleOwner, @NotNull event: Lifecycle.Event)

//    fun subscribe()

//    fun unSubscribe()
}