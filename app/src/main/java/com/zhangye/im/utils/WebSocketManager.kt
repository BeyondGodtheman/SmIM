package com.zhangye.im.utils

import android.content.Intent
import com.cocosh.shmstore.utils.LogUtils
import com.google.gson.Gson
import com.zhangye.im.MyApplication
import com.zhangye.im.SMClient
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
    private val messageMap = HashMap<String, ArrayList<Chat>>()
    private val messageQueue = LinkedBlockingQueue<Chat>() //消息列队
    private val converseList = arrayListOf<Chat>() //会话列表
    private val contactList = arrayListOf<Contacts.Payload.Contact>()

    val gJson = Gson()
    var connListener: OnConnListener? = null //连接IM回调
    var contactListener: OnContactListener? = null //获取联系人回调
    /**
     * 建立WebSocket连接
     */
    fun connect(mConnListener: OnConnListener?) {
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

                if (t is ConnectException) {
                    LogUtils.i("连接IM服务器失败" + t)
                }
                if (t is SocketTimeoutException) {
                    LogUtils.i("连接IM服务器超时" + t)
                    connect(connListener)
                }

                if (t is EOFException) {
                    LogUtils.i("服务器连接断开" + t)
                    connect(connListener)
                }



                LogUtils.i("连接异常:" + t)
                connListener?.onError(0, t.toString())
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

                    if (message.subType == MessageType.CHAT.type) {
                        val messageChat = gJson.fromJson(messageStr, Chat::class.java)
                        addMessage(messageChat)
                        addConverse(messageChat)
                    }
                }

                /**
                 * 查询
                 */
                if (message.type == Type.IQ.type) {
                    val iq = gJson.fromJson(messageStr, IQ::class.java)

                    //联系人查询结果
                    if (iq.action.entity == EntityType.ROSTER.type) {
                        val contacts = gJson.fromJson(messageStr, Contacts::class.java)
                        LogUtils.i("联系人数据：$contacts")
                        contactList.clear()
                        contactList.addAll(contacts.payload.addList)
                        contactList.removeAll(contacts.payload.removeList)
                        notifyMessage()
                    }

                    //查询离消息结果
                    if (iq.action.entity == EntityType.OFFLINE.type) {
                        val offLine = gJson.fromJson(messageStr, Offlines::class.java)
//                        SMClient.getInstance().dbManager.saveMessage(offLine)
                        LogUtils.i(offLine.toString())
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
                    //服务器接收到消息返回的ACK
                    val ack = gJson.fromJson(messageStr, Ack::class.java)
                    val msg = messageQueue.peek()
                    //ACK为当前消息列队所发送的message
                    if (msg.messageId == ack.messageId) {
                        messageMap[msg.to]?.first { msg.messageId == it.messageId }?.state = true //修改发送状态
                        SMClient.getInstance().dbManager.updateChatState(msg.messageId)
                    }
                    messageQueue.take()
                    notifyMessage()
                    isSend = false
                    LogUtils.i("消息发送成功，从消息列队移除：" + message)
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
     * 验证失败断开连接
     */
    fun exitWebSocket() {
        webSocket?.close(1000, "logout")
        webSocket?.cancel()
        messageMap.clear()
        isConnection = false
        isOnline = false
        connListener = null
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
     * 发送消息
     */
    fun sendMessage(userName: String, message: String) {
        if (isConnection && isOnline) {
            val chat = Chat()
            chat.to = userName
            chat.payload.content = message

            if (messageMap[userName] == null) {
                messageMap.put(userName, ArrayList())
            }

            messageMap[userName]?.add(chat)
            addConverse(chat)
            SMClient.getInstance().dbManager.saveMessage(chat) //保存消息到数据库
            messageQueue.put(chat)
            notifyMessage()
        } else {
            LogUtils.i("离线状态无法发送")
        }
    }

    /**
     * 查询联系人
     */
    fun queryContact() {
        if (isConnection && isOnline) {
            val iQ = IQ.IQContacts(IQType.GET, EntityType.ROSTER, OperationType.LIST)
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
    fun addRouste() {

    }

    /**
     * 将最新消息放入会话列表
     */
    private fun addConverse(messageChat: Chat) {
        if (converseList.contains(messageChat)) {
            converseList[converseList.indexOf(messageChat)] = messageChat
        } else {
            converseList.add(messageChat)
        }
        notifyMessage()
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
        messageMap[nickName]?.let {
            return it
        }

        val msgList = SMClient.getInstance().dbManager.queryMessage(nickName, 0)
        messageMap.put(nickName, msgList)
        return msgList
    }


    fun getConverseList(): ArrayList<Chat> = converseList

    fun getContactList():ArrayList<Contacts.Payload.Contact> = contactList


    private fun notifyMessage() {
        val intent = Intent(Constants.BROADCAST_NEW_MESSAGE)
        MyApplication.instance.sendBroadcast(intent)
    }

    /**
     * 添加消息并刷新
     */
    fun addMessage(messageChat: Chat) {
        SMClient.getInstance().dbManager.saveMessage(messageChat) //保存消息到数据库
        val username = messageChat.from
        if (messageMap[username] == null) {
            val messageList = ArrayList<Chat>()
            messageList.add(messageChat)
            messageMap.put(username, messageList)
        } else {
            messageMap[username]?.add(messageChat)
        }
        notifyMessage()
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