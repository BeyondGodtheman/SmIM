package com.zhangye.im.ui

import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.zhangye.im.receiver.NotifyReceiver

/**
 * 父类
 * Created by zhangye on 2017/12/15.
 */
abstract class BaseActivity : AppCompatActivity() {
    lateinit var notifyReceiver: NotifyReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        notifyReceiver = NotifyReceiver(registRecycleView(), notifyIsEnd())
        val intentFilter = IntentFilter(flag())
        registerReceiver(notifyReceiver, intentFilter)
    }

    //刷新数据
    abstract fun notifyIsEnd(): Boolean

    //注册刷新的列表
    abstract fun registRecycleView(): RecyclerView

    //类型标签
    abstract fun flag(): String


    abstract fun initView()

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notifyReceiver)
    }
}