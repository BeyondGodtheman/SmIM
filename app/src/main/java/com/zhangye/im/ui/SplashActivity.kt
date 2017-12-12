package com.zhangye.im.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.utils.UserManager
import java.util.*

/**
 * 欢迎动画
 * Created by zhangye on 2017/11/16.
 */
class SplashActivity : AppCompatActivity() {

    val timer = Timer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        timer.schedule(object : TimerTask() {
            override fun run() {
                if (SMClient.getInstance().userManager.isLogin()) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }

                timer.cancel()
                finish()
            }
        }, 1000)

    }



}