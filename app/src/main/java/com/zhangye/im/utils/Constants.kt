package com.zhangye.im.utils

/**
 * 网络请求URL配置
 * Created by zhangye on 2017/11/2.
 */
object Constants {
    var DEBUG = true
    val HOST = if (DEBUG) "http://10.10.100.136:8080/" else ""
    val LOGIN = "app_manage/user/login"
    val CONTACT = "app_manage/user/contact_list"
    val TOKEN_ERROR = 108 //token失效

    /*登录存储相关*/
    val IM_LOGIN_INFO = "im_login_info"
    val IM_LOGIN = "im_login"
    val BROADCAST_NEW_MESSAGE = "com.sm.new_msg"
}