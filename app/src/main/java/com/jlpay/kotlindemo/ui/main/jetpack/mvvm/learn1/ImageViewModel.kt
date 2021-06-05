package com.jlpay.kotlindemo.ui.main.jetpack.mvvm.learn1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jlpay.kotlindemo.net.BaseObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * ViewModel：
 *
 * 逻辑处理类，负责数据处理和View层与Repository层的交互。
 *
 * ViewModel通过数据仓库Repository获取数据来源，处理来自View的事件命令，同时更新数据
 *
 * <p>
 * 1.首先这个类要继承自android.arch.lifecycle.ViewModel这个类，以便在创建时与View层的生命周期相关联
 */
class ImageViewModel() : ViewModel() {

    //用来存放图片信息，以便当信息发生变化时及时通知View层来更新界面
    private var mImage: MutableLiveData<Data<Image>> = MutableLiveData<Data<Image>>()

    //这个变量来负责数据访问
    private var mRepository: ImageRepository = ImageRepository()

    //这个变量来记录当前的图片页码
    private var idx: Int = 0

    //为mImage添加了getter方法以便View层可以对其进行观察与响应
    fun getImage(): MutableLiveData<Data<Image>> {
        return mImage
    }

    //loadImage,nextImage和previousImage这三个方法分别对应图片的加载，下一张和上一张，
    // 并且内部通过访问mRepository的方法来完成数据的访问，又对返回的数据进行判断处理并触发mImage的setValue方法来对数据进行更新
    fun loadImage() {
        mRepository.getImage("js", idx, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<ImageBean>() {
                override fun onSuccess(data: ImageBean) {
                    mImage.value = Data(data.images[0], null)
                }

                override fun onError(msg: String?, code: String?) {
                    mImage.value = Data(null, msg)
                }
            })
    }

    fun nextImage() {
        mRepository.getImage("js", ++idx, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<ImageBean>() {
                override fun onSuccess(data: ImageBean) {
                    mImage.value = Data(data.images[0], null)
                }

                override fun onError(msg: String?, code: String?) {
                    mImage.value = Data(null, msg)
                    idx--
                }
            })
    }

    fun previousImage() {
        if (idx <= 0) {
            mImage.value = Data(null, "已经是第一个了")
            return
        }
        mRepository.getImage("js", --idx, 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<ImageBean>() {
                override fun onSuccess(data: ImageBean) {
                    mImage.value = Data(data.images[0], null)
                }

                override fun onError(msg: String?, code: String?) {
                    mImage.value = Data(null, msg)
                    idx++
                }
            })
    }
}