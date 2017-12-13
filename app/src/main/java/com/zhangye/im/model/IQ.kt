package com.zhangye.im.model

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
    class IQContacts(subType: IQType,
                     entity: EntityType,
                     operation: OperationType) : IQ(subType, entity, operation) {

        var payload = PayLoad()


        class PayLoad {
            var version = "-1"

        }

        override fun toString(): String {
            return "IQContacts(payload=$payload) ${super.toString()}"
        }

    }

}


