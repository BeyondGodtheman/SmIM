package com.zhangye.im.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * IM数据库创建
 * Created by 张野 on 2017/11/7.
 */
class SmImDbHelper(context: Context, private var dbConfig: DBConfig) : SQLiteOpenHelper(context, dbConfig.mDbName, null, dbConfig.dbVersion) {

    override fun onCreate(db: SQLiteDatabase) {
        //聊天记录表
        val message = "CREATE TABLE IF NOT EXISTS  ${dbConfig.chatTabName} (Id integer primary key, MessageId text, Subtype text, From_ text, Timestamp text, To_ text, Content text, State NUMERIC, ContentType text,Type text);"
        db.execSQL(message)
        //联系人表
        val contact = "CREATE TABLE IF NOT EXISTS  ${dbConfig.contactName} (Id integer primary key, Username text, TenantDomain text, Nickname text, Type text, Status text, Version text,UNIQUE (Username));"
        db.execSQL(contact)
        //会话聊天表
        val converseChat = "CREATE TABLE IF NOT EXISTS  ${dbConfig.converseName} (Id integer primary key, MessageId text, Subtype text, From_ text, Timestamp text, To_ text, Content text, State NUMERIC, ContentType text,Type text, Converse text, UNIQUE (Converse));"
        db.execSQL(converseChat)

        //好友请求表
        val addFriend = "CREATE TABLE IF NOT EXISTS  ${dbConfig.addFriendName} (Id integer primary key, Type text, Subtype text,MessageId text,From_ text, To_ text, Entity text, Operation text, Receiver text, Reason text, UNIQUE (Receiver));"
        db.execSQL(addFriend)
        //会话好友请求表

//        "username":"batu",
//        "tenantDomain":"shoumeiapp.com",
//        "nickname":"巴图",
//        "type":"PERSIST",
//        "status":"ACTIVE",
//        "version":1
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dbConfig.dbUpdateListener?.onUpgrade(db, oldVersion, newVersion)
    }
}
