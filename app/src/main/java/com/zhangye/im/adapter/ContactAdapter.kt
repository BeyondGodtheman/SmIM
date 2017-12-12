package com.zhangye.im.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zhangye.im.R
import com.zhangye.im.model.Contacts.Payload.Contact
import com.zhangye.im.ui.ChatActivity


/**
 * 联系人适配器
 * Created by zhangye on 2017/11/3.
 */
class ContactAdapter(private var contacts: ArrayList<Contact>) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.item_contact, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = contacts[position].nickname
        holder.tvCode.text = contacts[position].username
        holder.view.setOnClickListener({
            val intent = Intent(it.context, ChatActivity::class.java)
            intent.putExtra("userName", contacts[position].username)
            it.context.startActivity(intent)
        })
    }


    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tvName = view.findViewById<TextView>(R.id.tv_name)
        var tvCode = view.findViewById<TextView>(R.id.tv_code)
    }
}