package com.zhangye.im.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.utils.UserManager
import com.zhangye.im.utils.WebSocketManager
import kotlinx.android.synthetic.main.fragment_me.view.*

/**
 * 个人资料Fragment
 * Created by zhangye on 2017/11/13.
 */
class MeFragment : Fragment(), View.OnClickListener {

    lateinit var convertView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        convertView = inflater.inflate(R.layout.fragment_me, container, false)
        convertView.btn_login_out.setOnClickListener(this)
        return convertView
    }

    override fun onClick(v: View) {
        when (v.id) {
            convertView.btn_login_out.id -> {
                SMClient.getInstance().userManager.loginOut() //退出登录
                startActivity(Intent(activity, LoginActivity::class.java))
                activity.finish()
            }
        }
    }
}