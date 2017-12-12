package com.zhangye.im.model

/**
 * 查询类型
 * Created by zhangye on 2017/11/10.
 */
enum class QueryType(var query: String) {
    QUERY_ROSTER("QUERY_ROSTER"), //查询通讯录
    QUERY_OFFLINE_MESSAGES("QUERY_OFFLINE_MESSAGES") //查询离线消息
}