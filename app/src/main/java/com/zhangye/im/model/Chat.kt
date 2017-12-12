package com.zhangye.im.model


/**
 * 消息数据
 * Created by 张野 on 2017/11/2.
 */
class Chat {
    var timestamp = System.currentTimeMillis()

    var content_type = ContentType.TEXT_PLAIN

    var content = ""

    var state = false

    override fun toString(): String {
        return "Chat(timestamp=$timestamp, content_type=$content_type, content='$content')"
    }
}