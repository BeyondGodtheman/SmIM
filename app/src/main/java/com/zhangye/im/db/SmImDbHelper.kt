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
        val contact = "CREATE TABLE IF NOT EXISTS  ${dbConfig.contactName} (Id integer primary key, Username text, TenantDomain text, Nickname text, Type text, Status text, VserSion text);"
        db.execSQL(contact)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dbConfig.dbUpdateListener?.onUpgrade(db, oldVersion, newVersion)
    }
}
