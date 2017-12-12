package com.zhangye.im.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.`interface`.OnConnListener
import com.zhangye.im.utils.Constants
import com.zhangye.im.utils.UserManager
import com.zhangye.im.utils.WebSocketManager
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 首页
 * Created by zhangye on 2017/11/13.
 */
class MainActivity : AppCompatActivity(), OnConnListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SMClient.getInstance().webSocketManager.connect(this)

        radio_group.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                group.getChildAt(0).id -> {
                    Toast.makeText(this, "你好1", Toast.LENGTH_SHORT).show()
                }

                group.getChildAt(1).id -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fl_content, ContactFragment()).commit()
                }

                group.getChildAt(2).id -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fl_content, MeFragment()).commit()
                }
            }
        }


    }

    override fun onSuccess() {
    }

    override fun onError(code: Int, msg: String) {

        if (code == Constants.TOKEN_ERROR) {
            SMClient.getInstance().userManager.loginOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        SMClient.getInstance().close()
    }


}