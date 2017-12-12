package com.zhangye.im.`interface`

import com.zhangye.im.model.Contacts

/**
 * 获取联系人接口
 * Created by zhangye on 2017/11/13.
 */
interface OnContactListener {
    fun onContact(contacts: Contacts)
}