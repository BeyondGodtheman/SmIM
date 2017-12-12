package com.zhangye.im.model

/**
 * 登录结果数据
 * Created by 张野 on 2017/11/2.
 */
data class Login(var code: String,
                 var success: Boolean,
                 var message: String,
                 var entity: Entity) {


    data class Entity(var username: String,
                      var fqdnName:String,
                      var cmUrl: String,
                      var cmFqdn:String,
                      var loginToken: String)

}