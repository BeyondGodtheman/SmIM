package com.zhangye.im.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.model.Chat
import com.zhangye.im.model.BaseMessage
import com.zhangye.im.model.Message
import com.zhangye.im.utils.DateFormat
import com.zhangye.im.utils.UserManager

/**
 * 聊天适配器
 * Created by 张野 on 2017/11/6.
 */
class ChatAdapter(private var list: ArrayList<Message<Chat>>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val SEND = 1
    private val RECIEVE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val view = if (viewType == RECIEVE) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_receive_message, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.item_send_message, parent, false)
        }
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.tv_name?.text = ("${list[position].from}  ${DateFormat.formatHHmm(list[position].playload?.timestamp!!)}")
        holder?.tv_message?.text = list[position].playload?.content

        if(getItemViewType(position) == SEND){
            holder?.iv_avatar?.setImageResource(R.mipmap.default_head)
        }else{
            holder?.iv_avatar?.setImageResource(R.mipmap.rt_head)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView = itemView.findViewById(R.id.tv_name)
        var iv_avatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        var tv_message: TextView = itemView.findViewById(R.id.tv_message)
    }


    override fun getItemViewType(position: Int): Int {
        if (list[position].from == SMClient.getInstance().userManager.getSenderUsername()) {
            return SEND
        }
        return RECIEVE
    }

    fun addItem(messageChat: Message<Chat>) {
        list.add(messageChat)
        notifyDataSetChanged()
    }
}