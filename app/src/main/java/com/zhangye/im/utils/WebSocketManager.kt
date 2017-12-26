package com.zhangye.im.utils

import android.content.Intent
import com.cocosh.shmstore.utils.LogUtils
import com.google.gson.Gson
import com.zhangye.im.MyApplication
import com.zhangye.im.SMClient
import com.zhangye.im.`interface`.OnAddFriendListener
import com.zhangye.im.`interface`.OnConnListener
import com.zhangye.im.`interface`.OnContactListener
import com.zhangye.im.model.*
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.EOFException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread


/**
 * WebSocket连接管理类
 * Created by 张野 on 2017/11/2.
 */
class WebSocketManager {
    var webSocket: WebSocket? = null
    var isConnection = false
    var isOnline = false
    var isSend = false
    var reConntCount = 0 //重连次数
    //    private val messageMap = HashMap<String, ArrayList<Chat>>()
    private val messageQueue = LinkedBlockingQueue<Chat>() //消息列队
    private val converseList = arrayListOf<Chat>() //会话列表
    private val contactList = arrayListOf<Contacts.Payload.Contact>()
    private val addFriends = arrayListOf<IQ.IQAddFriend>()
    private val messageList = arrayListOf<Chat>() //消息列表

    val gJson = Gson()
    var connListener: OnConnListener? = null //连接IM回调
    var contactListener: OnContactListener? = null //获取联系人回调
    var addFriendListener: OnAddFriendListener? = null //添加好友回调
    /**
     * 建立WebSocket连接
     */
    fun connect(mConnListener: OnConnListener?) {
        if (SMClient.getInstance().userManager.getImUrl() == "") {
            LogUtils.i("连接地址为null")
            return
        }
        this.connListener = mConnListener
        val request = Request.Builder().url(SMClient.getInstance().userManager.getImUrl()).build()
        HttpUtil.getClient().newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(mWebSocket: WebSocket, response: Response) {
                LogUtils.i("连接IM服务器成功:${response.body()?.string()}")
                webSocket = mWebSocket
                isConnection = true
                onPresent()
            }

            override fun onFailure(webSocket: WebSocket?, t: Throwable, response: Response?) {
                isConnection = false
                isOnline = false

                if (t is ConnectException) {
                    connListener?.onError(0, "连接IM服务器失败")
                }
                if (t is SocketTimeoutException) {
                    if (reConntCount == 2) {
                        reConntCount = 0
                        connListener?.onError(Constants.LOGOUT, "重连失败，重新login")
                        return
                    }
                    reConntCount++
                    connect(connListener)
                    connListener?.onError(0, "连接IM服务器超时")
                }

                if (t is EOFException) {
                    connListener?.onError(0, "服务器连接断开")
                    connect(connListener)
                }


                if (t is NoRouteToHostException) {
                    connListener?.onError(Constants.LOGOUT, "服务器地址失效")
                    connect(connListener)
                }

                if (t is SocketException) {
                    connListener?.onError(Constants.NET_FILE, "网络连接异常")
                }

                LogUtils.i("连接异常:" + t)
            }

            override fun onMessage(webSocket: WebSocket?, messageStr: String) {
                LogUtils.i("收到IM服务器消息String:" + messageStr)
                val message = gJson.fromJson(messageStr, Message::class.java)

                /**
                 * 发生错误
                 */
                if (message.type == Type.ERROR.type) {
                    val error = gJson.fromJson(messageStr, Error::class.java)
                    LogUtils.i("收到错误消息：${error.payload.message}")
                    connListener?.onError(error.payload.code, error.payload.message)
                }


                /**
                 * 出席结果
                 */
                if (message.type == Type.PRESENT.type) {
                    isOnline = true
                    connListener?.onSuccess() //登录成功回调
                    //查询离线消息
                    queryOffLine()
                    //轮循发送的消息列队
                    loopChat()
                }

                /**
                 * 收到Msg消息
                 */
                if (message.type == Type.MESSAGE.type) {
                    //聊天消息
                    if (message.subType == MessageType.CHAT.type) {
                        val messageChat = gJson.fromJson(messageStr, Chat::class.java)
                        addMessage(messageChat)
                    }
                    //系统通知
                    if (message.subType == MessageType.NOTICE.type) {
                        val iQ = gJson.fromJson(messageStr, IQ::class.java)
                        //好友相关
                        if (iQ.action.entity == EntityType.ROSTER.type) {
                            if (iQ.action.operation == OperationType.ADD.type) {
                                //收到好友请求
                                val addFriend = gJson.fromJson(messageStr, IQ.IQAddFriend::class.java)
                                if (addFriends.contains(addFriend)) {
                                    addFriends.remove(addFriend)
                                }
                                addFriends.add(0, addFriend)
                                SMClient.getInstance().dbManager.saveAddFriend(addFriend)
                                notifyMessage(Constants.BROADCAST_NEW_FRIEND)
                            }
                        }
                    }
                }

                /**
                 * 查询
                 */
                if (message.type == Type.IQ.type) {
                    val iq = gJson.fromJson(messageStr, IQ::class.java)

                    //联系人
                    if (iq.action.entity == EntityType.ROSTER.type) {
                        //查询结果
                        if (iq.action.operation == OperationType.LIST.type) {
                            val contacts = gJson.fromJson(messageStr, Contacts::class.java)
                            //保存联系人版本号
                            SMClient.getInstance().prefrencesManager.setContactVersion(contacts.payload.version)
                            SMClient.getInstance().dbManager.saveContact(contacts.payload.addList)
                            SMClient.getInstance().dbManager.deleteContact(contacts.payload.removeList)
                            //更改内存中的联系人数据
                            contactList.removeAll(contacts.payload.removeList)
                            contactList.addAll(contacts.payload.addList)
                            notifyMessage(Constants.BROADCAST_NEW_CONTACT)
                        }

                        if (iq.action.operation == OperationType.ADD.type) {
                            LogUtils.i("好友请求发送成功")
                            addFriendListener?.onAddFriendResult("好友请求发送成功")
                            addFriendListener = null
                        }

                        if (iq.action.operation == OperationType.ANSWER.type) {

                            queryNetContact()
                        }
                    }

                    //查询离消息结果
                    if (iq.action.entity == EntityType.OFFLINE.type) {
//                        val offLine = gJson.fromJson(messageStr, Offlines::class.java)
//                        SMClient.getInstance().dbManager.saveMessage(offLine.payload.chatList[0].messageList)
//                        LogUtils.i("离线消息结果" + offLine.toString())
                    }
                }

                /**
                 * 发送ACK告知服务器消息以接收
                 */
                if (message.type != Type.ACK.type && message.type != Type.ERROR.type) {
                    //过滤出席及查询结果后发送ACK
                    LogUtils.i("发送ACK")
                    senAck(message.messageId)
                } else {
                    if (message.type == Type.ACK.type) {
                        //服务器接收到消息返回的ACK
                        val ack = gJson.fromJson(messageStr, Ack::class.java)
                        val msg = messageQueue.peek()
                        //ACK为当前消息列队所发送的message
                        if (msg.messageId == ack.messageId) {
                            messageList.first { msg.messageId == it.messageId }.state = true//修改发送状态
//                        messageMap[msg.to]?.first { msg.messageId == it.messageId }?.state = true
                            SMClient.getInstance().dbManager.updateChatState(msg.messageId)
                            messageQueue.take()
                            notifyMessage(Constants.BROADCAST_NEW_MESSAGE)
                            isSend = false
                            LogUtils.i("消息发送成功，从消息列队移除：" + message)

                        }
                    }

                }

            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString?) {


            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
                LogUtils.i("IM服务器关闭中......:" + reason)

            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String?) {
                LogUtils.i("IM服务器关闭:" + reason)

            }

        })
    }

    /**
     * 退出
     */
    fun exitWebSocket() {
        webSocket?.close(1000, "logout")
        webSocket?.cancel()
        isConnection = false
        isOnline = false
        connListener = null

        messageQueue.clear()//消息列队
        converseList.clear()//会话列表
        contactList.clear()//联系人列表

    }


    /**
     * 上线出席
     */
    fun onPresent() {
        if (isConnection) {
            val present = Present()
            val json = gJson.toJson(present)
            LogUtils.i("发送出席数据:" + json)
            webSocket?.send(json)
        } else {
            LogUtils.i("连接不存在")
        }
    }

    /**
     * 发送ACK,用于确认消息以接收
     */
    fun senAck(messageId: String) {
        val ack = Ack()
        ack.messageId = messageId
        webSocket?.send(gJson.toJson(ack))
    }


    /**
     * 发送文字消息
     */
    fun sendMessage(userName: String, message: String) {
        if (isConnection && isOnline) {
            val chat = Chat()
            chat.to = userName
            chat.payload.content = message
            addMessage(chat)
            messageQueue.put(chat)
        } else {
            LogUtils.i("离线状态无法发送")
        }
    }

    /**
     * 网络查询联系人
     */
    fun queryNetContact() {
        if (isConnection && isOnline) {
            val iQ = IQ.IQContacts()
            val json = gJson.toJson(iQ)
            webSocket?.send(json)
            LogUtils.i("拉取联系人信息:" + json)
        } else {
            LogUtils.i("连接异常或未出席成功")
        }
    }


    /**
     * 添加好友
     */
    fun addFriend(username: String, msg: String, addFriendListener: OnAddFriendListener) {
        this.addFriendListener = addFriendListener
        val iQ = IQ.IQAddFriend()
        iQ.payload.receiver = username + Constants.FQDN_NAME
        iQ.payload.reason = msg
        val json = gJson.toJson(iQ)
        LogUtils.i("发送好友请求：" + json)
        webSocket?.send(json)
    }

    /**
     * 是否同意好友请求
     */
    fun answer(fqdnName: String, isAnswer: Boolean) {
        val mAnswer = Answer()
        mAnswer.payload.receiver = fqdnName
        mAnswer.payload.answer = isAnswer
        val json = gJson.toJson(mAnswer)
        LogUtils.i("回复好友请求：" + json)
        webSocket?.send(json)
    }


    /**
     * 拉取离线消息
     */
    fun queryOffLine() {
        val iQ = IQ(IQType.GET, EntityType.OFFLINE, OperationType.LIST)
        val json = gJson.toJson(iQ)
        webSocket?.send(json)
        LogUtils.i("拉取离线信息:" + json)
    }


    /**
     * 获取消息集合
     */
    fun getMessageList(nickName: String): ArrayList<Chat> {
        messageList.clear()
        messageList.addAll(SMClient.getInstance().dbManager.queryMessage(nickName, 0))
        return messageList
    }

    /**
     * 获取好友请求集合
     */
    fun getAddFriendList(): ArrayList<IQ.IQAddFriend> {
        addFriends.clear()
        addFriends.addAll(SMClient.getInstance().dbManager.queryAddFriend())
        return addFriends
    }


    /**
     * 将最新消息放入会话列表
     */
    private fun addConverse(messageChat: Chat) {

        if (messageChat.from == SMClient.getInstance().userManager.getFqdnName()) {
            messageChat.converse = messageChat.to
        } else {
            messageChat.converse = messageChat.from
        }

        SMClient.getInstance().dbManager.saveConverse(messageChat)

        if (converseList.contains(messageChat)) {
            converseList[converseList.indexOf(messageChat)] = messageChat
        } else {
            converseList.add(messageChat)
        }
        notifyMessage(Constants.BROADCAST_NEW_CONVERSE)
    }


    /**
     * 查询会话记录
     */
    fun queryConvers() {
        converseList.clear()
        converseList.addAll(SMClient.getInstance().dbManager.queryConverse())
    }


    fun queryContacts() {
        contactList.clear()
        contactList.addAll(SMClient.getInstance().dbManager.queryContacts())
    }


    fun getConverseList(): ArrayList<Chat> {
        queryConvers() //本地查询会话列表
        return converseList
    }

    fun getContactList(): ArrayList<Contacts.Payload.Contact> {
        queryContacts()//本地查询联系人列表
        return contactList
    }


    private fun notifyMessage(flag: String) {
        val intent = Intent(flag)
        MyApplication.instance.sendBroadcast(intent)
    }

    /**
     * 存储消息并刷新
     */
    fun addMessage(messageChat: Chat) {
        SMClient.getInstance().dbManager.saveMessage(messageChat) //保存消息到数据库
        messageList.add(messageChat)
        notifyMessage(Constants.BROADCAST_NEW_MESSAGE)
        //加入会话中
        addConverse(messageChat)
    }


    /**
     * 轮循消息列队发送消息
     */
    fun loopChat() {
        thread {
            while (isConnection && isOnline) {
                //非阻塞状态发送消息
                if (!isSend) {
                    if (messageQueue.size > 0) {
                        val msg = gJson.toJson(messageQueue.peek())
                        LogUtils.i("发送消息：$msg")
                        webSocket?.send(msg)
                        isSend = true
                    }
                    //进入阻塞等待ACK
                }
            }
        }
    }
}