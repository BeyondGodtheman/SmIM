package com.zhangye.im.model

/**
 * 错误数据模型
 * Created by 张野 on 2017/11/6.
 */
class Error : BaseMessage() {
    init {
        type = Type.ERROR.type
    }

    lateinit var payload: Payload


    data class Payload(var code: Int, var message: String)

    override fun toString(): String {
        return "Error(payload=$payload) ${super.toString()}"
    }
}