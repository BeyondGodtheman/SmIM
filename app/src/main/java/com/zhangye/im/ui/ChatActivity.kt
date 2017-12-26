package com.zhangye.im.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import com.cocosh.shmstore.utils.LogUtils
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.adapter.ChatAdapter
import com.zhangye.im.model.Emoj
import com.zhangye.im.utils.Constants
import com.zhangye.im.utils.PermissionsManager
import com.zhangye.im.widget.ChatExtendMenu
import com.zhangye.im.widget.ChatInputMenu
import com.zhangye.im.widget.VoiceRecorderView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.layout_simple_title.*

/**
 * 聊天界面
 * Created by zhangye on 2017/11/3.
 */
class ChatActivity : BaseActivity() {

    val names = arrayOf("图片")
    val icons = arrayOf(R.drawable.chat_image_selector)
    val ids = arrayOf(1)

    override fun notifyIsEnd(): Boolean = true

    override fun registRecycleView(): RecyclerView = recycleView

    override fun flag(): String = Constants.BROADCAST_NEW_MESSAGE


    override fun initView() {
        setContentView(R.layout.activity_chat)

        SMClient.getInstance().pathUtil.initDirs(this) //初始化存储路径

        val userName = intent.getStringExtra("userName") ?: ""
        val fqdnName = if (!userName.contains("@")) "$userName${Constants.FQDN_NAME}" else userName
        val nickName = intent.getStringExtra("nickName") ?: ""
        tv_title.text = nickName

        LogUtils.i("和 $fqdnName 聊天")

        recycleView.layoutManager = LinearLayoutManager(this)
//        (recycleView.layoutManager as LinearLayoutManager).stackFromEnd = true
        val adapter = ChatAdapter(SMClient.getInstance().webSocketManager.getMessageList(fqdnName))
        recycleView.adapter = adapter
        recycleView.scrollToPosition(recycleView.adapter.itemCount - 1)


        tv_back.setOnClickListener {
            finish()
        }

        input_menu.initMenu(null) //初始化菜单
        input_menu.setChatInputMenuListener(object : ChatInputMenu.ChatInputMenuListener {
            override fun onSendMessage(content: String) {

                //空消息事件拦截a
                if (content.isBlank()) {
                    return
                }

                SMClient.getInstance().webSocketManager.sendMessage(fqdnName, content)

            }

            override fun onBigExpressionClicked(emojicon: Emoj) {
            }

            override fun onPressToSpeakBtnTouch(v: View, event: MotionEvent): Boolean {
                return recorderView.onPressToSpeakBtnTouch(v, event, object : VoiceRecorderView.VoiceRecorderCallback {
                    override fun onVoiceRecordComplete(voiceFilePath: String, voiceTimeLength: Int) {

                    }

                })

            }

        })

        /**
         * 初始化菜单
         */
        names.forEachIndexed { index, _ ->
            input_menu.registerExtendMenuItem(names[index], icons[index], ids[index], object : ChatExtendMenu.ChatExtendMenuItemClickListener {
                override fun onClick(itemId: Int, view: View) {
                    LogUtils.i("id")
                }
            })

        }


    }

    override fun onBackPressed() {
        if (input_menu.onBackPressed()) {
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionsManager.instance.notifyPermissionsChange(permissions, grantResults)
    }
}