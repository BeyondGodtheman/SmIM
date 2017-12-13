package com.zhangye.im.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.model.Chat
import com.zhangye.im.model.MessageType
import com.zhangye.im.ui.ChatActivity
import com.zhangye.im.utils.DateFormat

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

    override fun onBindViewHolder(holder: ConverseAdapter.ViewHolder?, position: Int) {
        if (list[position].subType == MessageType.CHAT.type) {

            userName = if (list[position].from == SMClient.getInstance().userManager.getFqdnName()) {
                list[position].to
            } else {
                list[position].from
            }
            holder?.tvName?.text = SMClient.getInstance().webSocketManager.getContactList().first { it.username == userName.split("@")[0] }.nickname
            holder?.tvContent?.text = list[position].payload.content
            holder?.tvTime?.text = DateFormat.formatHHmm(list[position].payload.timestamp)


            holder?.itemView?.setOnClickListener {
                val intent = Intent(holder.itemView.context, ChatActivity::class.java)
                intent.putExtra("userName", userName)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName = itemView.findViewById<TextView>(R.id.tv_name)
        var tvContent = itemView.findViewById<TextView>(R.id.tv_content)
        var tvTime = itemView.findViewById<TextView>(R.id.tv_time)
    }

}