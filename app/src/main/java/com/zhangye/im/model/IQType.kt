package com.zhangye.im.model

/**
 * IQ枚举类型
 * Created by zhangye on 2017/11/10.
 */
enum class IQType(var type: String) {
    SET("set"), //修改
    GET("get"), //查询
    RESULT("result") //结果
}