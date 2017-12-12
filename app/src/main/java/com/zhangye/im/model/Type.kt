package com.zhangye.im.model

/**
 * 消息类型
 * Created by zhangye on 17-12-4.
 */
enum class Type(val type: String) {

    IQ("iq"), //查询
    MESSAGE("message"), //消息
    PRESENT("present"), //出席
    ACK("ack"),
    ERROR("error")
}