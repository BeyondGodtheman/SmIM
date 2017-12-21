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
import com.zhangye.im.widget.ChatExtendMenu
import com.zhangye.im.widget.ChatInputMenu
import kotlinx.android.synthetic.main.activity_chat.*

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

        val userName = intent.getStringExtra("userName")
        val tenantDomain = intent.getStringExtra("tenantDomain") ?: Constants.FQDN_NAME
        val fqdnName = if (!userName.contains("@")) "$userName@$tenantDomain" else userName

        LogUtils.i("和 $fqdnName 聊天")

        recycleView.layoutManager = LinearLayoutManager(this)
        val adapter = ChatAdapter(SMClient.getInstance().webSocketManager.getMessageList(fqdnName))
        recycleView.adapter = adapter
        recycleView.scrollToPosition(recycleView.adapter.itemCount - 1)

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
                return false
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
}