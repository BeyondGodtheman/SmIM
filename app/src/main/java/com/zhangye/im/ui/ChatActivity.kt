package com.zhangye.im.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.cocosh.shmstore.utils.LogUtils
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.adapter.ChatAdapter
import com.zhangye.im.model.Chat
import com.zhangye.im.model.Emoj
import com.zhangye.im.model.Message
import com.zhangye.im.utils.Constants
import com.zhangye.im.widget.ChatInputMenu
import kotlinx.android.synthetic.main.activity_chat.*

/**
 * 聊天界面
 * Created by zhangye on 2017/11/3.
 */
class ChatActivity : AppCompatActivity() {

    lateinit var messageReceiver: MessageReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val userName = intent.getStringExtra("userName")
        val tenantDomain = intent.getStringExtra("tenantDomain")

        messageReceiver = MessageReceiver()
        val intentFilter = IntentFilter(Constants.BROADCAST_NEW_MESSAGE)
        registerReceiver(messageReceiver, intentFilter)

        recycleView.layoutManager = LinearLayoutManager(this)
//        val adapter = ChatAdapter(SMClient.getInstance().webSocketManager.getMessageList(userName))
        val adapter = ChatAdapter(ArrayList())
        recycleView.adapter = adapter
        recycleView.scrollToPosition(recycleView.adapter.itemCount - 1)

        input_menu.initMenu(null)
        input_menu.setChatInputMenuListener(object : ChatInputMenu.ChatInputMenuListener {
            override fun onSendMessage(content: String) {
                LogUtils.i("发送的消息:$content")

                if (content.isBlank()) {
                    return
                }

                val message = SMClient.getInstance().webSocketManager.sendMessage("$userName@$tenantDomain", content)
                message?.let {
                    adapter.addItem(it)
                }

//                val messageChatMe = Message<Chat>()
//                messageChatMe.playload = Chat()
//                messageChatMe.playload?.content = content
//                adapter.addItem(messageChatMe)
//
//                val messageChat = Message<Chat>()
//                messageChat.from = "小美"
//                messageChat.playload = Chat()
//                messageChat.playload?.content = "我知道了!"
//                adapter.addItem(messageChat)

//                LogUtils.i("发送消息:" + edt_msg.text.toString())
            }

            override fun onBigExpressionClicked(emojicon: Emoj) {
            }

            override fun onPressToSpeakBtnTouch(v: View, event: MotionEvent): Boolean {
                return false
            }

        })


        //发送消息事件
//        edt_msg.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_SEND) {
//                if (edt_msg.text.toString().isBlank()) {
//                    Toast.makeText(this, "不能发送空消息！", Toast.LENGTH_SHORT).show()
//                    return@setOnEditorActionListener true
//                }
//                LogUtils.i("发送消息:" + edt_msg.text.toString())
////            SMClient.getInstance().webSocketManager.sendMessage(userName, edt_msg.text.toString())
//                val messageChatMe = Message<Chat>()
//                messageChatMe.playload = Chat()
//                messageChatMe.playload?.content = edt_msg.text.toString()
//                adapter.addItem(messageChatMe)
//
//                val messageChat = Message<Chat>()
//                messageChat.from = "小美"
//                messageChat.playload = Chat()
//                messageChat.playload?.content = "我知道了!"
//                adapter.addItem(messageChat)
//                edt_msg.text = null
//
//                return@setOnEditorActionListener true
//            }
//
//            return@setOnEditorActionListener false
//        }

//        btn_send.setOnClickListener({
//            if(edt_msg.text.toString().isBlank()){
//                Toast.makeText(this,"不能发送空消息！",Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            LogUtils.i("发送消息:" + edt_msg.text.toString())
////            SMClient.getInstance().webSocketManager.sendMessage(userName, edt_msg.text.toString())
//            val messageChatMe = Message<Chat>()
//            messageChatMe.playload = Chat()
//            messageChatMe.playload?.content = edt_msg.text.toString()
//            adapter.addItem(messageChatMe)
//
//            val messageChat = Message<Chat>()
//            messageChat.from = "小美"
//            messageChat.playload = Chat()
//            messageChat.playload?.content = "我知道了!"
//            adapter.addItem(messageChat)
//            edt_msg.text = null
//        })

    }

    inner class MessageReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            LogUtils.i("广播接受成功")
            LogUtils.i("集合长度：" + recycleView.adapter.itemCount)
            recycleView.adapter.notifyDataSetChanged()
            recycleView.scrollToPosition(recycleView.adapter.itemCount - 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(messageReceiver)
    }
}