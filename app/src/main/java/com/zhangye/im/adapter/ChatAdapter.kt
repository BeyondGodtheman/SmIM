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
import com.zhangye.im.utils.DateFormat
import com.zhangye.im.utils.SmileUtils

/**
 * 聊天适配器
 * Created by 张野 on 2017/11/6.
 */
class ChatAdapter(private var list: ArrayList<Chat>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvName.text = ("${list[position].from}  ${DateFormat.formatHHmm(list[position].payload.timestamp)}")
        val sapner = SmileUtils.getSmiledText(holder.itemView.context!!, list[position].payload.content)
        holder.tvMessage.setText(sapner, TextView.BufferType.SPANNABLE)

        if (getItemViewType(position) == SEND) {
            holder.ivAvatar.setImageResource(R.mipmap.default_head)

            if (list[position].state) {
                holder.ivFaild?.visibility = View.GONE
            } else {
                holder.ivFaild?.visibility = View.VISIBLE
            }

        } else {
            holder.ivAvatar.setImageResource(R.mipmap.rt_head)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_name)
        var ivAvatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        var tvMessage: TextView = itemView.findViewById(R.id.tv_message)
        var ivFaild: ImageView? = itemView.findViewById(R.id.iv_faild)
    }


    override fun getItemViewType(position: Int): Int {
        if (list[position].from == SMClient.getInstance().userManager.getFqdnName()) {
            return SEND
        }
        return RECIEVE
    }

    fun addItem(messageChat: Chat) {
        list.add(messageChat)
        notifyDataSetChanged()
    }
}