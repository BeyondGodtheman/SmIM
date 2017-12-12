package com.zhangye.im.db

import android.database.sqlite.SQLiteDatabase
import com.zhangye.im.SMClient
import com.zhangye.im.utils.UserManager

/**
 * 数据库参数配置
 * Created by zhangye on 2017/11/10.
 */
class DBConfig {
    val mDbName = "${SMClient.getInstance().userManager.getSenderUsername()}.db" // 数据库名字
    val dbVersion = 1 // 数据库版本
    val chatTabName = "messageChat"
    val contactName = "contact"
    var dbUpdateListener: DbUpdateListener? = null

    interface DbUpdateListener {
        fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    }
}