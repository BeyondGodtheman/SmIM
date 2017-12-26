package com.zhangye.im.utils

/**
 * 网络请求URL配置
 * Created by zhangye on 2017/11/2.
 */
object Constants {
    var DEBUG = true
    val HOST = if (DEBUG) "http://10.10.100.120:8080/" else ""
    val LOGIN = "app_manage/user/login"
    val FQDN_NAME = "@shoumeiapp.com"
    val LOGOUT = 103 //code":103,"message":"需要先登录，才能发消息"
    val NET_FILE = 100 //自定义字段，网络不可用
    /*登录存储相关*/
    val IM_LOGIN_INFO = "im_login_info"
    val IM_LOGIN = "im_login"
    val BROADCAST_NEW_MESSAGE = "com.sm.new_msg"     //新消息列表
    val BROADCAST_NEW_CONTACT = "com.sm.new_contact" //联系人列表
    val BROADCAST_NEW_CONVERSE = "com.sm.new_converse" //会话列表
    val BROADCAST_NEW_FRIEND = "com.sm.new_friend" //好友请求列表

    /*联系人版本*/
    val CONTACT_VERSION = "contact_version"

    val FILE_INVALID = 401
}