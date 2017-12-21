package com.zhangye.im.model

/**
 * 联系人
 * Created by 张野 on 2017/11/3.
 */
class Contacts(subType: IQType,
               entity: EntityType,
               operation: OperationType) : IQ(subType, entity, operation) {

    var payload = Payload()

    class Payload {
        var version = "-1"
        var addList = arrayListOf<Contact>()
        var removeList = arrayListOf<Contact>()

        data class Contact(
                var username: String,
                var tenantDomain: String,
                var nickname: String,
                var type: String,
                var status: String,
                var version: String
        )

        override fun toString(): String {
            return "Payload(version='$version', addList=$addList, removeList=$removeList)"
        }


    }

    override fun toString(): String {
        return "Contacts(payload=$payload)"
    }


}
