package com.zhangye.im.model

import com.zhangye.im.SMClient
import com.zhangye.im.utils.CodeUtils


/**
 * 通讯发送消息的数据实体类
 * Created by 张野 on 2017/11/2.
 */
open class BaseMessage {
    var type = Type.MESSAGE.type
    var messageId = CodeUtils.createUUID12char()
    var from = SMClient.getInstance().userManager.getFqdnName()
    var to = SMClient.getInstance().userManager.getCmName()

    override fun toString(): String {
        return "BaseMessage(type=$type, messageId='$messageId', from='$from', to='$to')"
    }

}