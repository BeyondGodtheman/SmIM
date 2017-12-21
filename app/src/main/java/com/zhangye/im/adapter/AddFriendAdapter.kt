package com.zhangye.im.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.cocosh.shmstore.utils.LogUtils
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.model.IQ

/**
 * 好友请求适配器
 * Created by zhangye on 2017/12/20.
 */
class AddFriendAdapter(var addFriends: ArrayList<IQ.IQAddFriend>) : RecyclerView.Adapter<AddFriendAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AddFriendAdapter.ViewHolder {
        val view = View.inflate(parent?.context, R.layout.item_add_friend, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = addFriends.size

    override fun onBindViewHolder(holder: AddFriendAdapter.ViewHolder?, position: Int) {
        holder?.tvName?.text = addFriends[position].payload.receiver
        holder?.tvContent?.text = addFriends[position].payload.reason

        holder?.btn_Yes?.tag = position
        holder?.btn_Yes?.setOnClickListener {
            LogUtils.i("点击同意了")
            SMClient.getInstance().webSocketManager.answer(addFriends[it.tag as Int].payload.receiver, true)
        }
        holder?.btn_No?.tag = position
        holder?.btn_No?.setOnClickListener {
            LogUtils.i("点击拒绝了")
            SMClient.getInstance().webSocketManager.answer(addFriends[it.tag as Int].payload.receiver, false)
            SMClient.getInstance().dbManager.deleteAddFriend(addFriends[it.tag as Int].payload.receiver)
            addFriends.removeAt(it.tag as Int)
            notifyDataSetChanged()
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivPhoto = itemView.findViewById<ImageView>(R.id.iv_photo)
        var tvName = itemView.findViewById<TextView>(R.id.tv_name)
        var tvContent = itemView.findViewById<TextView>(R.id.tv_content)
        var btn_Yes = itemView.findViewById<Button>(R.id.btn_yes)
        var btn_No = itemView.findViewById<Button>(R.id.btn_no)
    }

}