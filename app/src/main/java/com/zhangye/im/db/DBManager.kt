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
                chat.state = it.getInt(it.getColumnIndex("State")) == 1
                chat.payload.content_type = it.getString(it.getColumnIndex("ContentType"))
                chat.type = it.getString(it.getColumnIndex("Type"))

                messageChats.add(chat)
            }
        }

        cursor?.close()
        return messageChats
    }

    /**
     * 存储会话列表
     */
    fun saveConverse(messageChat: Chat) {
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
        contentValues.put("Converse", messageChat.converse)
        db?.replace(dbConfig.converseName, null, contentValues)
    }

    /**
     * 查询会话列表
     */
    fun queryConverse(): ArrayList<Chat> {
        val converseChats = arrayListOf<Chat>()
        val sql = "SELECT * FROM ${dbConfig.converseName}"
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
                chat.state = it.getInt(it.getColumnIndex("State")) == 1
                chat.payload.content_type = it.getString(it.getColumnIndex("ContentType"))
                chat.type = it.getString(it.getColumnIndex("Type"))
                chat.converse = it.getString(it.getColumnIndex("Converse"))
                converseChats.add(chat)
            }
        }
        cursor?.close()
        return converseChats
    }


    /**
     * 查询所有联系人方法
     */
    fun queryContacts(): ArrayList<Contacts.Payload.Contact> {
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
                        it.getString(it.getColumnIndex("Version"))

                ))
            }
        }
        cursor?.close()
        return contacts
    }


    /**
     * 查询联系人
     */
    fun queryContact(username: String): Contacts.Payload.Contact? {
        val sql = "SELECT * FROM ${dbConfig.contactName} where Username=?"

        val cursor = db?.rawQuery(sql, arrayOf(username))

        cursor?.let {
            if (it.moveToNext()) {
                return Contacts.Payload.Contact(
                        it.getString(it.getColumnIndex("Username")),
                        it.getString(it.getColumnIndex("TenantDomain")),
                        it.getString(it.getColumnIndex("Nickname")),
                        it.getString(it.getColumnIndex("Type")),
                        it.getString(it.getColumnIndex("Status")),
                        it.getString(it.getColumnIndex("Version"))
                )
            }


        }
        cursor?.close()

        return null
    }


    /**
     * 存储好友请求
     */
    fun saveAddFriend(addfriend: IQ.IQAddFriend) {
        val contentValues = ContentValues()
        contentValues.put("Type", addfriend.type)
        contentValues.put("Subtype", addfriend.subType)
        contentValues.put("MessageId", addfriend.messageId)
        contentValues.put("From_", addfriend.from)
        contentValues.put("To_", addfriend.to)
        contentValues.put("Entity", addfriend.action.entity)
        contentValues.put("Operation", addfriend.action.operation)
        contentValues.put("Receiver", addfriend.payload.receiver)
        contentValues.put("Reason", addfriend.payload.reason)
        db?.replace(dbConfig.addFriendName, null, contentValues)
    }

    /**
     * 查询好友请求
     */
    fun queryAddFriend(): ArrayList<IQ.IQAddFriend> {
        val addFriends = arrayListOf<IQ.IQAddFriend>()
        val sql = "SELECT * FROM ${dbConfig.addFriendName} ORDER BY Id ASC"
        val cursor = db?.rawQuery(sql, null)
        cursor?.let {
            while (it.moveToNext()) {
                val addFriend = IQ.IQAddFriend()
                addFriend.type = it.getString(it.getColumnIndex("Type"))
                addFriend.subType = it.getString(it.getColumnIndex("Subtype"))
                addFriend.messageId = it.getString(it.getColumnIndex("MessageId"))
                addFriend.from = it.getString(it.getColumnIndex("From_"))
                addFriend.to = it.getString(it.getColumnIndex("To_"))
                addFriend.action.entity = it.getString(it.getColumnIndex("Entity"))
                addFriend.action.operation = it.getString(it.getColumnIndex("Operation"))
                addFriend.payload.receiver = it.getString(it.getColumnIndex("Receiver"))
                addFriend.payload.reason = it.getString(it.getColumnIndex("Reason"))
                addFriends.add(addFriend)
            }
        }
        cursor?.close()
        return addFriends
    }

    /**
     *  删除好友请求数据
     */
    fun deleteAddFriend(receiver: String) {
        db?.delete(dbConfig.addFriendName, "Receiver=?", arrayOf(receiver))
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
        if (contactList.size == 0) {
            return
        }
        //手动设置开始事务
        db?.beginTransaction()
        contactList.forEach {
            val contentValues = ContentValues()
            contentValues.put("Username", it.username)
            contentValues.put("TenantDomain", it.tenantDomain)
            contentValues.put("Nickname", it.nickname)
            contentValues.put("Type", it.type)
            contentValues.put("Status", it.status)
            contentValues.put("Version", it.version)
            db?.replace(dbConfig.contactName, null, contentValues)
            LogUtils.i("批量存储联系人信息：" + it.nickname)
        }
        db?.setTransactionSuccessful()// 设置事务处理成功，不设置会自动回滚不提交
        db?.endTransaction() // 处理完成
    }

    /**
     * 批量删除联系人
     */
    fun deleteContact(contactList: ArrayList<Contacts.Payload.Contact>) {
        if (contactList.size == 0) {
            return
        }
        //手动设置开始事务
        db?.beginTransaction()
        contactList.forEach {
            db?.delete(dbConfig.contactName, "Username = ?", arrayOf(it.username))
            LogUtils.i("批量删除联系人信息：" + it.username)
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