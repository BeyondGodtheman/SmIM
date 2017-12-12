package com.zhangye.im.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * 网络状态工具类
 * Created by zhangye on 2017/11/8.
 */
class NetUtil {

    /** 检查是否有网络  */
    fun isNetworkAvailable(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info?.isAvailable ?: false
    }

    /** 检查是否是WIFI  */
    fun isWifi(context: Context): Boolean {
        val info = getNetworkInfo(context)
        if (info != null) {
            if (info.type == ConnectivityManager.TYPE_WIFI)
                return true
        }
        return false
    }

    /** 检查是否是移动网络  */
    fun isMobile(context: Context): Boolean {
        val info = getNetworkInfo(context)
        if (info != null) {
            if (info.type == ConnectivityManager.TYPE_MOBILE)
                return true
        }
        return false
    }

    private fun getNetworkInfo(context: Context): NetworkInfo? {

        val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /** 检查SD卡是否存在  */
    fun checkSdCard(): Boolean {
        return android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED
    }
}