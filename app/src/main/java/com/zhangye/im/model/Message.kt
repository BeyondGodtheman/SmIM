package com.zhangye.im.model

/**
 * 附带message的消息体
 * Created by zhangye on 2017/11/10.
 */
open class Message<T> : BaseMessage() {

    init {
        type = Type.MESSAGE.type
    }

    var subtype = MessageType.NOTICE
    var playload:T? = null

    override fun toString(): String {
        return "Message(subType=$subtype, playload='$playload') ${super.toString()}"
    }

}