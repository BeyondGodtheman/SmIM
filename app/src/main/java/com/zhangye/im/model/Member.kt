package com.zhangye.im.model

/**
 * Created by zhangye on 2017/12/14.
 */
class Member {
    var answer = false
    var isApply = false
    var groupCode = ""
    var memberList = arrayListOf<String>()

    override fun toString(): String {
        return "Member(answer=$answer, isApply=$isApply, groupCode='$groupCode', memberList=$memberList)"
    }


}