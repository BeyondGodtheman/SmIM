package com.zhangye.im.model

/**
 * 附加subType的实体类
 * Created by zhangye on 2017/12/13.
 */
open class Message : BaseMessage() {
    var subType = MessageType.NOTICE.type

}