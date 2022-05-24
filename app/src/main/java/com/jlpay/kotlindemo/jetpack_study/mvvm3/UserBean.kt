package com.jlpay.kotlindemo.jetpack_study.mvvm3

/**
 * Kotlin实战指南十二：data class：
 * https://blog.csdn.net/zhaoyanjun6/article/details/94649274
 */
data class UserBean(
    val username: String,
    val nickname: String,
    val publicName: String,
    val token: String,
    val id: Long
)