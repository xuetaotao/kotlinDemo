package com.jlpay.kotlindemo.jetpack_study.mvc

//负责具体事件的做事，处理数据和逻辑，做完后通知View
class Model {
    val view: View = View()
    var data = 1

    fun processData() {
        data++
        view.showData(data)
    }
}

//负责展示和通知事件
class View {

    val controller: Controller = Controller()

    fun showData(data: Int) {
        //打印 data
    }

    fun clicked() {
        controller.onViewClicked()
    }
}

//负责把具体事件做一个分发
class Controller {

    val model: Model = Model()

    fun onViewClicked() {
        model.processData()
    }
}