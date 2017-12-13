package com.zhangye.im.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cocosh.shmstore.utils.LogUtils
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.adapter.ContactAdapter
import com.zhangye.im.utils.Constants
import kotlinx.android.synthetic.main.layout_recycler_view.view.*

/**
 *
 * Created by 张野 on 2017/11/3.
 */
class ContactFragment : Fragment(), View.OnClickListener {
    lateinit var convertView: View
    lateinit var contactReceiver: ContactReceiver
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        convertView = inflater.inflate(R.layout.layout_recycler_view, container, false)

        convertView.recycleView.layoutManager = LinearLayoutManager(activity)
        convertView.recycleView.adapter = ContactAdapter(SMClient.getInstance().webSocketManager.getContactList())
        SMClient.getInstance().getContact() //拉取联系人

        contactReceiver = ContactReceiver()
        val intentFilter = IntentFilter(Constants.BROADCAST_NEW_MESSAGE)
        context.registerReceiver(contactReceiver, intentFilter)
        return convertView
    }


    override fun onClick(v: View) {

    }


    inner class ContactReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            LogUtils.i("广播接受成功")
            LogUtils.i("集合长度：" + convertView.recycleView.adapter.itemCount)
            convertView.recycleView.adapter.notifyDataSetChanged()
            convertView.recycleView.scrollToPosition(convertView.recycleView.adapter.itemCount - 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        context.unregisterReceiver(contactReceiver)
    }
}