package com.zhangye.im.ui

import android.os.Bundle
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
class ConverseFragment : BaseFragment() {

    lateinit var convertView: View
    lateinit var recycleView: RecyclerView


    override fun notifyIsEnd(): Boolean = false

    override fun registRecycleView(): RecyclerView = recycleView


    override fun flag(): String = Constants.BROADCAST_NEW_CONVERSE


    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        convertView = inflater.inflate(R.layout.layout_recycler_view, container, false)

        recycleView = convertView.findViewById(R.id.recycleView)
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.adapter = ConverseAdapter(SMClient.getInstance().webSocketManager.getConverseList())

        return convertView
    }


}