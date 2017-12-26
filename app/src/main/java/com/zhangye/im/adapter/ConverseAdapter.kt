package com.zhangye.im.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cocosh.shmstore.utils.LogUtils
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.model.Chat
import com.zhangye.im.model.IQ
import com.zhangye.im.model.Message
import com.zhangye.im.model.MessageType
import com.zhangye.im.ui.ChatActivity
import com.zhangye.im.utils.DateFormat
import com.zhangye.im.utils.SmileUtils

/**
 * 会话列表
 * Created by zhangye on 2017/12/13.
 */
class ConverseAdapter(var list: ArrayList<Chat>) : RecyclerView.Adapter<ConverseAdapter.ViewHolder>() {
    lateinit var userName: String
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ConverseAdapter.ViewHolder {
        val view = View.inflate(parent?.context, R.layout.item_converse, null)
        return ConverseAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConverseAdapter.ViewHolder, position: Int) {
        if (list[position].subType == MessageType.CHAT.type) {
            val chat = list[position]
            userName = chat.converse
            val contact = SMClient.getInstance().dbManager.queryContact(userName.split("@")[0])
            if (contact != null) {
                holder.tvName?.text = contact.nickname
            } else {
                holder.tvName?.text = "陌生人"
            }

            holder.tvContent?.text = SmileUtils.getSmiledText(holder.tvContent?.context!!,chat.payload.content)
            holder.tvTime?.text = DateFormat.formatHHmm(chat.payload.timestamp)

            holder.itemView?.tag = position
            holder.itemView?.setOnClickListener {
                val intent = Intent(holder.itemView.context, ChatActivity::class.java)

                intent.putExtra("userName",  contact?.username)
                intent.putExtra("nickName",  contact?.nickname)

                holder.itemView.context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivPhoto = itemView.findViewById<ImageView>(R.id.iv_photo)
        var tvName = itemView.findViewById<TextView>(R.id.tv_name)
        var tvContent = itemView.findViewById<TextView>(R.id.tv_content)
        var tvTime = itemView.findViewById<TextView>(R.id.tv_time)
    }

}