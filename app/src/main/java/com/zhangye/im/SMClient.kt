package com.zhangye.im

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UploadManager
import com.zhangye.im.`interface`.OnContactListener
import com.zhangye.im.db.DBManager
import com.zhangye.im.utils.PreferencesManager
import com.zhangye.im.utils.UserManager
import com.zhangye.im.utils.WebSocketManager
import com.zhangye.im.widget.RudenessScreenHelper




/**
 * 首媒IM客户端
 * Created by zhangye on 17-11-29.
 */
class SMClient private constructor() {
    lateinit var context: Context
    val webSocketManager = WebSocketManager()//webSocket管理
    val prefrencesManager = PreferencesManager() //存储管理
    val dbManager = DBManager() //SQLite管理类
    val userManager = UserManager()//用户管理类
    lateinit var uploadManager: UploadManager
    companion object {
        fun getInstance() = SingleClient.client
    }


    private object SingleClient {
        @SuppressLint("StaticFieldLeak")
        val client = SMClient()
    }


    //初始化方法
    fun init(context: Application) {
        this.context = context.applicationContext
        RudenessScreenHelper(context, 1080f).activate()
        //初始化七牛
        uploadManager = UploadManager()
    }


    fun close(){
        webSocketManager.exitWebSocket()
        dbManager.close()
    }
}