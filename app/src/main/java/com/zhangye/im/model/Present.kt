package com.zhangye.im.model

import com.zhangye.im.SMClient

/**
 * 出席
 * Created by zhangye on 2017/11/2.
 */
open class Present : BaseMessage() {
    init {
        type = Type.PRESENT.type

    }

    var payload = Payload()


    class Payload {
        var token = SMClient.getInstance().userManager.getToken()
        override fun toString(): String {
            return "Payload(token='$token')"
        }

    }


    override fun toString(): String {
        return "Present(payload=$payload) ${super.toString()}"
    }

}