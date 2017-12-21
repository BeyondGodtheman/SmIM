package com.zhangye.im.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView

/**
 * 刷新数据的广播
 * Created by zhangye on 2017/12/15.
 */
class NotifyReceiver(var recycleView: RecyclerView, var isEnd: Boolean) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        recycleView.adapter.notifyDataSetChanged() //刷新数据
        if (isEnd) {
            recycleView.scrollToPosition(recycleView.adapter.itemCount - 1) //滚动到最后的位置
        }
    }
}