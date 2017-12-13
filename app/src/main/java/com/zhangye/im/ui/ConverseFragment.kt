package com.zhangye.im.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.adapter.ConverseAdapter
import com.zhangye.im.utils.Constants

/**
 * 消息列表Fm
 * Created by zhangye on 2017/12/13.
 */
class ConverseFragment : Fragment() {
    lateinit var convertView: View
    lateinit var recycleView: RecyclerView
    lateinit var messageReceiver:MessageReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        convertView = inflater.inflate(R.layout.layout_recycler_view, container, false)

        recycleView = convertView.findViewById(R.id.recycleView)
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.adapter = ConverseAdapter(SMClient.getInstance().webSocketManager.getConverseList())

        messageReceiver = MessageReceiver()
        val intentFilter = IntentFilter(Constants.BROADCAST_NEW_MESSAGE)
        context.registerReceiver(messageReceiver, intentFilter)

        return convertView
    }


    inner class MessageReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            recycleView.adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        context.unregisterReceiver(messageReceiver)
    }
}