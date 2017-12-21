package com.zhangye.im.model

/**
 * 消息包
 * Created by zhangye on 2017/11/2.
 */
class Offlines(subType: IQType,
               entity: EntityType,
               operation: OperationType) : IQ(subType, entity, operation) {


    var payload = Payload()

    class Payload {
        var otherList: ArrayList<OrtherList> = arrayListOf()
        var chatList = arrayListOf<ChatList>()


        class ChatList {
            var peer = ""
            var messageList = ArrayList<Chat>()
            override fun toString(): String {
                return "ChatList(peer='$peer', messageList=$messageList)"
            }
        }


        class OrtherList(subType: IQType,
                         entity: EntityType,
                         operation: OperationType) : IQ(subType, entity, operation) {

            var payload = ""

        }

    }
}
