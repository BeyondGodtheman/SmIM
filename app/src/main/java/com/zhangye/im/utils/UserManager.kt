package com.zhangye.im.utils

import com.cocosh.shmstore.utils.LogUtils
import com.google.gson.Gson
import com.zhangye.im.SMClient
import com.zhangye.im.`interface`.OnLoginListener
import com.zhangye.im.model.Login
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * 登录信息管理类
 * Created by zhangye on 2017/11/2.
 */
class UserManager {
    private var login: Login? = null


    fun login(username: String, password: String, onLoginListener: OnLoginListener?) {
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)
        HttpUtil.postJson(json.toString(), Constants.LOGIN).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException) {
                LogUtils.i("网络请求失败：" + e)
                onLoginListener?.onLoginResult(false, e.localizedMessage)
            }

            override fun onResponse(call: Call?, response: Response) {
                val result = response.body()?.string()
                LogUtils.i("网络请求结果：" + result)
                if (response.isSuccessful && result != null) {
                    val gJson = Gson()
                    //内存存储单例登录信息
                    login = (gJson.fromJson(result, Login::class.java))

                    login?.let {
                        if (it.success) {
                            SMClient.getInstance().dbManager.initDb() //初始化数据库
                            //本地存储登录信息
                            SMClient.getInstance().prefrencesManager.login(result)
                            //建立连接
                            onLoginListener?.onLoginResult(true, it.message)
                        } else {
                            onLoginListener?.onLoginResult(false, it.message)
                        }
                    }
                }
            }
        })
    }


    //是否已登录
    fun isLogin(): Boolean {

        var isLogin = false

        login?.let {
            LogUtils.i("读取内存缓存登录状态")
            isLogin = it.success
        }

        SMClient.getInstance().prefrencesManager.getLogin()?.let {
            LogUtils.i("读取磁盘缓存登录状态")
            login = it
            isLogin = it.success
        }

        if (isLogin) {
            SMClient.getInstance().dbManager.initDb()
        }
        return isLogin
    }

    fun loginOut() {
        SMClient.getInstance().prefrencesManager.clearLogin()
        SMClient.getInstance().webSocketManager.exitWebSocket()
        SMClient.getInstance().dbManager.close()
        login = null
    }

    //获取用戶NAME
    fun getSenderUsername(): String = login?.entity?.username ?: ""

    //获取@类型的账户
    fun getFqdnName(): String = login?.entity?.fqdnName ?: ""

    //获取链接URL
    fun getImUrl(): String = login?.entity?.cmUrl ?: ""

    //获取认证TOKEN
    fun getToken(): String = login?.entity?.loginToken ?: ""

    //获取链接管理器NAME
    fun getCmName(): String = login?.entity?.cmFqdn ?: ""

}