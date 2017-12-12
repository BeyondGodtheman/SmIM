package com.zhangye.im.model

/**
 *
 * Created by zhangye on 17-12-5.
 */
enum class EntityType(val type: String) {
    ROSTER("roster"),//通讯录
    GROUP("group"), //群组
    OFFLINE("offline"), //离线消息
    USER("user"), //用户
    MEMBER("member"), //群成员
}