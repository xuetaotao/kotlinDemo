package com.example.mvvm.httpUtils


// 用户个人信息
data class UserInfoBody(
    val coinCount: Int, // 总积分
    val rank: Int, // 当前排名
    val userId: Int,
    val username: String
)