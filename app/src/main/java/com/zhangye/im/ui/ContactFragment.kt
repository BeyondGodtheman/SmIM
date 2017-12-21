package com.zhangye.im.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.adapter.ContactAdapter
import com.zhangye.im.utils.Constants
import kotlinx.android.synthetic.main.layout_contact.view.*

/**
 * 联系人列表
 * Created by 张野 on 2017/11/3.
 */
class ContactFragment : BaseFragment(), View.OnClickListener {
    lateinit var convertView: View

    override fun notifyIsEnd(): Boolean = false

    override fun registRecycleView(): RecyclerView = convertView.recycleView

    override fun flag(): String = Constants.BROADCAST_NEW_CONTACT

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        convertView = inflater.inflate(R.layout.layout_contact, container, false)
        convertView.recycleView.layoutManager = LinearLayoutManager(activity)
        convertView.recycleView.adapter = ContactAdapter(SMClient.getInstance().webSocketManager.getContactList())
        convertView.btn_add_friend.setOnClickListener {
            startActivity(Intent(activity, AddFriendActivity::class.java))
        }
        return convertView
    }


    override fun onClick(v: View) {

    }


}