package com.zhangye.im.model

/**
 * Ack验证消息
 * Created by zhangye on 2017/11/2.
 */
class Ack : BaseMessage() {
    init {
        type = Type.ACK.type

    }
}