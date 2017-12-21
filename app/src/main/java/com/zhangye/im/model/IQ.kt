package com.zhangye.im.model

import com.zhangye.im.SMClient

/**
 * IQ查询基类
 * Created by zhangye on 2017/11/10.
 */
open class IQ(mSubType: IQType,
              entity: EntityType,
              operation: OperationType) : Message() {
    init {
        type = Type.IQ.type
        subType = mSubType.type
    }


    var action = Action(entity.type, operation.type)


    data class Action(
            var entity: String,
            var operation: String
    )

    override fun toString(): String {
        return "IQ(subType=$subType, action=$action) ${super.toString()}"
    }


    //查询联系人
    class IQContacts : IQ(IQType.GET, EntityType.ROSTER, OperationType.LIST) {

        var payload = PayLoad()


        class PayLoad {
            var version = SMClient.getInstance().prefrencesManager.getContactVersion()

        }

        override fun toString(): String {
            return "IQContacts(payload=$payload) ${super.toString()}"
        }

    }

    //发送好友请求
    class IQAddFriend : IQ(IQType.SET, EntityType.ROSTER, OperationType.ADD) {
        var payload = PayLoad()

        class PayLoad {
            var receiver = ""
            var reason = ""
        }

        override fun equals(other: Any?): Boolean {
            if (other is IQAddFriend && payload.receiver == other.payload.receiver) {
                return true
            }

            return false
        }

        override fun hashCode(): Int {
            return payload.hashCode()
        }
    }

}


