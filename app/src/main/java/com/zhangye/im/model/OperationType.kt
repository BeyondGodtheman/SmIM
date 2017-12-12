package com.zhangye.im.model

/**
 * Created by zhangye on 17-12-5.
 */
enum class OperationType(val type: String) {
    ADD("add"),//增加对象
    REMOVE("remove"), //删除对象
    LIST("list"), //列表
    DETAIL("detail"),//单个对象详情
    ANSWER("answer"), //对应于roster/add的用户审批。
}