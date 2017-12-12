package com.zhangye.im.`interface`

/**
 * WebSocket连接结果回调
 * Created by zhangye on 2017/11/2.
 */
interface OnConnListener {
    fun onSuccess()
    fun onError(code: Int, msg: String)
}