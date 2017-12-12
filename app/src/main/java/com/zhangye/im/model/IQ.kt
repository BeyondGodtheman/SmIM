package com.zhangye.im.model


/**
 * IQ查询基类
 * Created by zhangye on 2017/11/10.
 */
open class IQ(subType: IQType,
              entity: EntityType,
              operation: OperationType) : BaseMessage() {
    init {
        type = Type.IQ.type
    }

    var subType = subType.type

    var action = Action(entity.type, operation.type)


    data class Action(
            var entity: String,
            var operation: String
    )

    override fun toString(): String {
        return "IQ(subType=$subType, action=$action) ${super.toString()}"
    }


}