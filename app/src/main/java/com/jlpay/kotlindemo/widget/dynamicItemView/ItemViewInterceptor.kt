package com.jlpay.kotlindemo.widget.dynamicItemView

import com.jlpay.kotlindemo.bean.DynamicLayout

interface ItemViewInterceptor {

    /**
     * 分为以下几种布局：
     * 左侧           右侧                                      结果
     * TextView      EditText(输入类型分：String，Double...)     EditText的输入值
     * TextView      TextView(请选择下拉框，可选内容由后台下发)     TextView上现实的选择内容
     * TextView      TextView(省市区/行业，跳转其他页面获取返回值)   TextView上从其他页面返回的值
     * TextView      CheckBox                                  Boolean
     * TextView      TextView+Button(日历选择)                  TextView上现实的选择内容
     * TextView      EditText+Button(开户行搜索接口)                 EditText的输入值
     * TextView      RadioButton(结算周期)                      Boolean
     *
     * 上面           下面                                                     结果
     * TextView      ImageView(上传身份证接口，OCR接口，返回结果填充下面信息)       上传接口返回的ImageId
     * TextView      RadioButton                                             Boolean
     *
     *
     * 特例:
     * 1.借记卡封顶
     *
     */

    fun inflateView(dynamicLayout: DynamicLayout)

    fun getResult(): String

    //回显
    fun setResult(result: String)

//    fun getResultAdvance(): T //TODO  结果返回

    fun getViewId(): String

    fun setViewId(viewId: String)
}