package com.zhangye.im.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.cocosh.shmstore.utils.LogUtils
import com.zhangye.im.SMClient
import com.zhangye.im.model.*


/**
 * 聊天记录管理类
 * Created by zhangye on 2017/11/10.
 */
class DBManager {

    private var db: SQLiteDatabase? = null
    private lateinit var dbConfig: DBConfig


    /**
     * 存储聊天记录
     */
    fun saveMessage(messageChat: Chat) {
        val contentValues = ContentValues()
        contentValues.put("MessageId", messageChat.messageId)
        contentValues.put("Subtype", messageChat.subType)
        contentValues.put("From_", messageChat.from)
        contentValues.put("Timestamp", messageChat.payload.timestamp)
        contentValues.put("To_", messageChat.to)
        contentValues.put("Content", messageChat.payload.content)
        contentValues.put("State", messageChat.state)
        contentValues.put("ContentType", messageChat.payload.content_type)
        contentValues.put("Type", messageChat.type)
        db?.insert(dbConfig.chatTabName, null, contentValues)
    }


    /**
     * 批量存储聊天记录
     */
    fun saveMessage(chatList: List<Chat>) {
        //手动设置开始事务
        db?.beginTransaction()
        chatList.forEach {
            val contentValues = ContentValues()
            contentValues.put("MessageId", it.messageId)
            contentValues.put("Subtype", it.subType)
            contentValues.put("From_", it.from)
            contentValues.put("Timestamp", it.payload.timestamp)
            contentValues.put("To_", it.to)
            contentValues.put("Content", it.payload.content)
            contentValues.put("State", it.state)
            contentValues.put("ContentType", it.payload.content_type)
            contentValues.put("Type", it.type)
            db?.insert(dbConfig.chatTabName, null, contentValues)
            LogUtils.i("批量存储聊天信息：" + it.payload.content)
        }
        db?.setTransactionSuccessful()// 设置事务处理成功，不设置会自动回滚不提交
        db?.endTransaction() // 处理完成
    }


    /**
     * 查询聊天记录
     */
    fun queryMessage(username: String, page: Int): ArrayList<Chat> {
        val messageChats = arrayListOf<Chat>()
        val sql = "SELECT * FROM ${dbConfig.chatTabName} WHERE From_='$username' OR To_='$username'"
        val cursor = db?.rawQuery(sql, null)
        cursor?.let {
            while (it.moveToNext()) {
                val chat = Chat()
                chat.messageId = it.getString(it.getColumnIndex("MessageId"))
                chat.subType = it.getString(it.getColumnIndex("Subtype"))
                chat.from = it.getString(it.getColumnIndex("From_"))
                chat.payload.timestamp = it.getString(it.getColumnIndex("Timestamp")).toLong()
                chat.to = it.getString(it.getColumnIndex("To_"))
                chat.payload.content = it.getString(it.getColumnIndex("Content"))
                chat.state = it.getString(it.getColumnIndex("State")).toBoolean()
                chat.payload.content_type = it.getString(it.getColumnIndex("ContentType"))
                chat.type = it.getString(it.getColumnIndex("Type"))

                messageChats.add(chat)
            }
        }

        cursor?.close()
        return messageChats
    }


    /**
     * 查询所有联系人方法
     */
    fun queryContact(): ArrayList<Contacts.Payload.Contact> {
        val contacts = arrayListOf<Contacts.Payload.Contact>()
        val sql = "SELECT * FROM ${dbConfig.contactName}"
        val cursor = db?.rawQuery(sql, null)
        cursor?.let {
            while (it.moveToNext()) {
                contacts.add(Contacts.Payload.Contact(
                        it.getString(it.getColumnIndex("Username")),
                        it.getString(it.getColumnIndex("TenantDomain")),
                        it.getString(it.getColumnIndex("Nickname")),
                        it.getString(it.getColumnIndex("Type")),
                        it.getString(it.getColumnIndex("Status")),
                        it.getInt(it.getColumnIndex("VserSion"))

                ))
            }
        }

        cursor?.close()
        return contacts
    }

    fun updateContact(nikename: String) {
//        db?.update(dbConfig.contactName, )
    }


    /**
     * 更新消息状态
     */
    fun updateChatState(messageId: String) {
        val content = ContentValues()
        content.put("State", true)
        db?.update(dbConfig.chatTabName, content, "MessageId=?", arrayOf(messageId))
    }


    /**
     * 批量保存联系人
     */
    fun saveContact(contactList: ArrayList<Contacts.Payload.Contact>) {
        //手动设置开始事务
        db?.beginTransaction()
        contactList.forEach {
            val contentValues = ContentValues()
            contentValues.put("Username", it.username)
            contentValues.put("TenantDomain", it.tenantDomain)
            contentValues.put("Nickname", it.nickname)
            contentValues.put("Type", it.type)
            contentValues.put("Status", it.status)
            contentValues.put("VserSion", it.version)
            db?.insert(dbConfig.chatTabName, null, contentValues)
            LogUtils.i("批量存储联系人信息：" + it.nickname)
        }
        db?.setTransactionSuccessful()// 设置事务处理成功，不设置会自动回滚不提交
        db?.endTransaction() // 处理完成
    }


    fun initDb(mdbConfig: DBConfig) {
        dbConfig = mdbConfig
        db = SmImDbHelper(SMClient.getInstance().context, dbConfig).writableDatabase
    }

    fun initDb() {
        initDb(DBConfig())
    }

    fun close() {
        db?.close()
        db = null
    }
}