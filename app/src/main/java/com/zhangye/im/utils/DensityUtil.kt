package com.zhangye.im.utils

import android.content.Context

/**
 * Created by zhangye on 2017/12/11.
 */
object DensityUtil {
    fun dip2px(var0: Context, var1: Float): Int {
        val var2 = var0.resources.displayMetrics.density
        return (var1 * var2 + 0.5f).toInt()
    }

    fun px2dip(var0: Context, var1: Float): Int {
        val var2 = var0.resources.displayMetrics.density
        return (var1 / var2 + 0.5f).toInt()
    }

    fun sp2px(var0: Context, var1: Float): Int {
        val var2 = var0.resources.displayMetrics.scaledDensity
        return (var1 * var2 + 0.5f).toInt()
    }

    fun px2sp(var0: Context, var1: Float): Int {
        val var2 = var0.resources.displayMetrics.scaledDensity
        return (var1 / var2 + 0.5f).toInt()
    }
}