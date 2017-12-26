package com.zhangye.im.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.model.Contacts.Payload.Contact
import com.zhangye.im.model.KeyValue
import com.zhangye.im.ui.AddFriendActivity
import com.zhangye.im.ui.ChatActivity


/**
 * 联系人适配器
 * Created by zhangye on 2017/11/3.
 */
class ContactAdapter(private var contacts: ArrayList<Contact>) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    var keyValues = ArrayList<KeyValue>()

    init {
        keyValues.add(KeyValue(R.mipmap.ic_launcher, "添加好友"))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View.inflate(parent.context, R.layout.item_contact, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = contacts.size + keyValues.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position < keyValues.size) {
            holder.ivPhoto.setImageResource(keyValues[position].key)
            holder.tvName.text = keyValues[position].value
            holder.view.setOnClickListener {
                it.context.startActivity(Intent(it.context, AddFriendActivity::class.java))
            }
        } else {
            val index = position - keyValues.size
            holder.tvName.text = contacts[index].nickname
            holder.tvCode.text = contacts[index].username
            holder.view.setOnClickListener({
                val intent = Intent(it.context, ChatActivity::class.java)
                if (SMClient.getInstance().userManager.getSenderUsername() == contacts[index].username) {
                    return@setOnClickListener
                }
                intent.putExtra("userName", contacts[index].username)
                intent.putExtra("nickName", contacts[index].nickname)
                it.context.startActivity(intent)
            })
        }
    }


    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var ivPhoto = view.findViewById<ImageView>(R.id.iv_photo)
        var tvName = view.findViewById<TextView>(R.id.tv_name)
        var tvCode = view.findViewById<TextView>(R.id.tv_code)
    }
}