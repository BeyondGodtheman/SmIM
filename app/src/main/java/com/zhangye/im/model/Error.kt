package com.zhangye.im.model

/**
 * 错误数据模型
 * Created by 张野 on 2017/11/6.
 */
class Error(var error_code: Int, var error_message: String) : BaseMessage() {
    init {
        type = Type.ERROR.type
    }

    lateinit var payload: Payload


    data class Payload(var code: String, var message: String)

    override fun toString(): String {
        return "Error(error_code=$error_code, error_message='$error_message', payload=$payload) ${super.toString()}"
    }
}