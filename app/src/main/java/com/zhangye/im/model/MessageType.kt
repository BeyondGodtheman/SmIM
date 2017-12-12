package com.zhangye.im.model

/**
 * 消息类型
 * Created by 张野 on 2017/11/2.
 */
enum class MessageType(val type: String) {
    CHAT("chat"), //個人消息
    GROUPCHAT("groupchat"), //羣組消息
    NOTICE("notice"), //系統消息
}