package com.zhangye.im.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.zhangye.im.SMClient
import com.zhangye.im.model.Login

/**
 * 存储信息工具类
 * Created by zhangye on 2017/11/2.
 */
class PreferencesManager {
    private lateinit var preferences: SharedPreferences

    private fun createPreferences(name: String): SharedPreferences {
        preferences = SMClient.getInstance().context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return preferences
    }


    // 保存IM登录信息
    fun login(json: String) {
        createPreferences(Constants.IM_LOGIN).edit().putString(Constants.IM_LOGIN_INFO, json).apply()
    }


    // 获取IM登录信息
    fun getLogin(): Login? {
        val json = createPreferences(Constants.IM_LOGIN).getString(Constants.IM_LOGIN_INFO, "")
        if (json.isBlank()) {
            return null
        }
        val gJson = Gson()
        return gJson.fromJson(json, Login::class.java)
    }


    // 删除IM登录信息
    fun clearLogin() {
        createPreferences(Constants.IM_LOGIN).edit().remove(Constants.IM_LOGIN_INFO).apply()
    }

    //设置联系人版本号
    fun setContactVersion(varsion: String) {
        createPreferences(SMClient.getInstance().userManager.getFqdnName()).edit().putString(Constants.CONTACT_VERSION, varsion).apply()
    }

    //获取当前联系人版本号
    fun getContactVersion(): String = createPreferences(Constants.IM_LOGIN).getString(Constants.CONTACT_VERSION, "-1")

}