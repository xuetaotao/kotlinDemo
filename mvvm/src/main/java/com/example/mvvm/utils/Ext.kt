package com.example.mvvm.utils

import android.app.Activity
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.webkit.WebView
import com.example.mvvm.R
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient

/**
 * 得到一个自定义的AgentWebView
 * Created by jzh on 2020-12-28.
 */
fun getAgentWebView(
    url: String,
    activity: Activity,
    webContent: ViewGroup,
    layoutParams: ViewGroup.LayoutParams,
    webView: WebView,
    webViewClient: com.just.agentweb.WebViewClient?,
    webChromeClient: com.just.agentweb.WebChromeClient?,
    indicatorColor: Int
): AgentWeb {
    return AgentWeb.with(activity)
        .setAgentWebParent(webContent, 1, layoutParams)//传入AgentWeb 的父控件
        .useDefaultIndicator(indicatorColor, 2)// 使用默认进度条
        .setWebView(webView)
        .setWebViewClient(webViewClient)
        .setWebChromeClient(webChromeClient)
        .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
        .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
        .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
        .interceptUnkownUrl()
        .createAgentWeb()//
        .ready()
        .go(url)
}


/**
 * 获取旋转动画
 * @param fromDegress X轴顺时针转动到fromDegrees为旋转的起始点，
 * @param toDegress X轴顺时针转动到toDegrees为旋转的结束点
 */
fun getRotateAnimation(fromDegress: Float, toDegress: Float): Animation {
    val rotateAnimation = RotateAnimation(
        fromDegress,
        toDegress,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    rotateAnimation.fillAfter = true
    rotateAnimation.duration = 2000
    rotateAnimation.repeatCount = Animation.INFINITE
    rotateAnimation.repeatMode = Animation.RESTART
    rotateAnimation.interpolator = LinearInterpolator()
    return rotateAnimation
}