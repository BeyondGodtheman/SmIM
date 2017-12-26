package com.zhangye.im.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.`interface`.OnConnListener
import com.zhangye.im.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_simple_title.*

/**
 * 首页
 * Created by zhangye on 2017/11/13.
 */
class MainActivity : AppCompatActivity(), OnConnListener {

    val fragments = arrayListOf<Fragment>()
    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SMClient.getInstance().webSocketManager.connect(this)

        fragments.add(ConverseFragment())
        fragments.add(ContactFragment())
        fragments.add(MeFragment())

        fragments.forEachIndexed { index, fragment ->
            supportFragmentManager.beginTransaction().add(R.id.fl_content, fragment).commit()
            if (index != 0) {
                hideFragment(index)
            }
        }


        tv_back.visibility = View.GONE

        radio_group.setOnCheckedChangeListener { group, checkedId ->

            hideFragment(index)

            when (checkedId) {
                group.getChildAt(0).id -> {
                    supportFragmentManager.beginTransaction().show(fragments[0]).commit()
                    index = 0
                }

                group.getChildAt(1).id -> {
                    supportFragmentManager.beginTransaction().show(fragments[1]).commit()
                    index = 1
                }

                group.getChildAt(2).id -> {
                    supportFragmentManager.beginTransaction().show(fragments[2]).commit()
                    index = 2
                }
            }
        }


    }

    override fun onSuccess() {
    }

    override fun onError(code: Int, msg: String) {

        if (code == Constants.LOGOUT) {
            SMClient.getInstance().userManager.loginOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
        runOnUiThread {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        SMClient.getInstance().close()
    }

    fun hideFragment(index: Int) {
        supportFragmentManager.beginTransaction().hide(fragments[index]).commit()
    }
}