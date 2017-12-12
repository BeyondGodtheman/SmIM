package com.zhangye.im.model

/**
 * 消息包
 * Created by zhangye on 2017/11/2.
 */
class Offlines(subType: IQType,
               entity: EntityType,
               operation: OperationType) : IQ(subType, entity, operation) {


    var otherList: ArrayList<Orther> = arrayListOf()


    class Orther(subType: IQType,
                 entity: EntityType,
                 operation: OperationType) : IQ(subType, entity, operation) {

        var payload = Payload()

        class Payload {
            var answer = false
            var isApply = false
            var groupCode = ""
            var memberList = arrayListOf<String>()

            override fun toString(): String {
                return "Payload(answer=$answer, isApply=$isApply, groupCode='$groupCode', memberList=$memberList)"
            }


        }

        override fun toString(): String {
            return "Orther(payload=$payload) ${super.toString()}"
        }


    }

    override fun toString(): String {
        return "Offlines(otherList=$otherList) ${super.toString()}"
    }


}
