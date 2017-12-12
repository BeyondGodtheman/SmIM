package com.zhangye.im.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.`interface`.OnLoginListener
import com.zhangye.im.utils.UserManager
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 登录页面
 * Created by zhangye on 2017/11/2.
 */
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener(this)
        val username = "zhangye"
        edt_username.setText(username)
    }


    override fun onClick(v: View) {
        when (v.id) {
            btn_login.id -> {
                //登录服务器 获取webSocket信息
                val username = edt_username.text.toString()
                val password = edt_password.text.toString()
                SMClient.getInstance().userManager.login(username, password, object : OnLoginListener {
                    override fun onLoginResult(isLogin: Boolean, message: String) {
                        runOnUiThread({
                            if (isLogin) {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                            }
                        })

                    }
                })
            }
        }
    }

}