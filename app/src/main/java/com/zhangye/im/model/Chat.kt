package com.zhangye.im.model

import com.zhangye.im.SMClient


/**
 * 消息数据
 * Created by 张野 on 2017/11/2.
 */
class Chat : Message() {

    init {
        subType = MessageType.CHAT.type
    }

    var state = false
    var payload = Payload()
    var converse = if (from != SMClient.getInstance().userManager.getFqdnName()) from else to

    class Payload {
        var timestamp = System.currentTimeMillis()

        var content_type = ContentType.TEXT_PLAIN.type

        var content = ""
        override fun toString(): String {
            return "Payload(timestamp=$timestamp, content_type='$content_type', content='$content')"
        }
    }


    override fun toString(): String {
        return "Chat(subType='$subType', state=$state, payload=$payload)"
    }

    override fun equals(other: Any?): Boolean {
        if (other is Chat) {
//
            if (converse == other.converse) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        var result = state.hashCode()
        result = 31 * result + payload.hashCode()
        result = 31 * result + converse.hashCode()
        return result
    }


}