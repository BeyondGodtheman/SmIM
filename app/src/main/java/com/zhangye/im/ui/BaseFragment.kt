package com.zhangye.im.ui

import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhangye.im.receiver.NotifyReceiver

/**
 * Created by zhangye on 2017/12/15.
 */
abstract class BaseFragment : Fragment() {
    lateinit var notifyReceiver: NotifyReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = initView(inflater, container, savedInstanceState)
        notifyReceiver = NotifyReceiver(registRecycleView(), notifyIsEnd())
        val intentFilter = IntentFilter(flag())
        context.registerReceiver(notifyReceiver, intentFilter)
        return view
    }


    //刷新数据
    abstract fun notifyIsEnd(): Boolean

    abstract fun registRecycleView(): RecyclerView

    abstract fun flag(): String


    abstract fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    override fun onDestroy() {
        super.onDestroy()
        context.unregisterReceiver(notifyReceiver)
    }
}