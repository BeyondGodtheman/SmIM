package com.zhangye.im.model

/**
 * 是否同意好友请求
 * Created by zhangye on 2017/12/20.
 */
class Answer : IQ(IQType.SET, EntityType.ROSTER, OperationType.ANSWER) {
    val payload = Payload()

    class Payload {
        var receiver = ""
        var answer = false
    }
}