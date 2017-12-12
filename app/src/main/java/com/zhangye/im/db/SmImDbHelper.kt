package com.zhangye.im.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * IM数据库创建
 * Created by 张野 on 2017/11/7.
 */
class SmImDbHelper(context: Context, private var dbConfig: DBConfig) : SQLiteOpenHelper(context, dbConfig.mDbName, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        //聊天记录表
        val message = "CREATE TABLE IF NOT EXISTS  ${dbConfig.chatTabName} (Id integer primary key, Message_code text, Sender_username text, Timestamp text, Room_code text, Content text, State NUMERIC);"
        db.execSQL(message)
        //联系人表
        val contact = "CREATE TABLE IF NOT EXISTS  ${dbConfig.contactName} (Id integer primary key, UserContactCode text, Status text, Type text, Room_code text, NickName text);"
        db.execSQL(contact)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dbConfig.dbUpdateListener?.onUpgrade(db, oldVersion, newVersion)
    }
}
