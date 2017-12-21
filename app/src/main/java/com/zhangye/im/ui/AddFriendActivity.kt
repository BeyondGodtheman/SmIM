package com.zhangye.im.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.`interface`.OnAddFriendListener
import com.zhangye.im.adapter.AddFriendAdapter
import com.zhangye.im.utils.Constants
import kotlinx.android.synthetic.main.activity_add_friend.*

/**
 * 添加好友页面
 * Created by zhangye on 2017/12/18.
 */
class AddFriendActivity : BaseActivity() {

    override fun notifyIsEnd(): Boolean = false

    override fun registRecycleView(): RecyclerView = recycleView

    override fun flag(): String = Constants.BROADCAST_NEW_FRIEND

    override fun initView() {
        setContentView(R.layout.activity_add_friend)

        btn_send.setOnClickListener({
            SMClient.getInstance().webSocketManager.addFriend(edt_username.text.toString(),
                    edt_msg.text.toString(), object : OnAddFriendListener {
                override fun onAddFriendResult(result: String) {
                    runOnUiThread {
                        Toast.makeText(this@AddFriendActivity, result, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        })
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = AddFriendAdapter(SMClient.getInstance().webSocketManager.getAddFriendList())
    }
}