package com.zhangye.im.model

/**
 * Created by zhangye on 2017/12/18.
 */
class Notice() {


    //好友通知结果类
    class NoticeRoust(subType: IQType,
                      entity: EntityType,
                      operation: OperationType):IQ(subType,entity,operation)

    var payload = Payload()

    class Payload{


    }

}