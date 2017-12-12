package com.zhangye.im.utils

import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by zhangye on 2017/12/7.
 */
object DateFormat {

    //解析成时分
    fun formatHHmm(stampe: Long): String {
        val simpData = SimpleDateFormat("HH:mm", Locale.getDefault())
        return simpData.format(stampe)
    }
}