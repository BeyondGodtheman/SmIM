package com.zhangye.im.utils

import android.util.Log
import com.cocosh.shmstore.utils.LogUtils
import okhttp3.*
import java.util.concurrent.TimeUnit

/**
 * 网络请求类
 * Created by zhangye on 2017/11/2.
 */
object HttpUtil {
    private val httpClient = OkHttpClient.Builder()
            .addInterceptor(LoggerInterceptor("网络请求"))
            .connectTimeout(10000L, TimeUnit.MILLISECONDS)
            .readTimeout(10000L, TimeUnit.MILLISECONDS)
            //其他配置
            .build()

    fun postJson(json: String, url: String): Call {
        val requestBody = buildJson(json)
        val request = getBuilder(Constants.HOST + url).post(requestBody).build()
        return httpClient.newCall(request)
    }


    fun get(params: Map<String, String>, url: String): Call {
        val request = getBuilder(Constants.HOST + buildUrlParams(url, params)).get().build()
        return httpClient.newCall(request)
    }

    /**
     * 生成提交JSON数据结构
     *
     * @param json 传递的数据
     */
    private fun buildJson(json: String): RequestBody {
        val jsonType = MediaType.parse("application/json")
        return RequestBody.create(jsonType, json)
    }


    /**
     * 生成请求Builder
     */
    private fun getBuilder(url: String): Request.Builder {
        return Request.Builder()
                .headers(getHeaders())
                .url(url)
    }


    /**
     * Http头信息
     */
    private fun getHeaders(): Headers {
        val builder = Headers.Builder()
        builder.add("PlatForm", "Android" + android.os.Build.VERSION.RELEASE)
        builder.add("Accept", "application/json")
//        builder.add("X-SMEDIA-TOKEN",UserManager.getLogin().entity.loginToken)
        return builder.build()
    }




    /**
     * 拼接URL参数的方法
     *
     * @return 参数拼接
     */
    private fun buildUrlParams(url: String, params: Map<String, String>?): String {
        val result = StringBuilder(url)
        if (params != null) {
            val urlParams = StringBuilder("?")
            var index = 0
            for ((key, value) in params) {

                if (index != 0) {
                    urlParams.append("&")
                }
                urlParams.append(key).append("=").append(value)
                index++
            }
            LogUtils.i("URL参数：" + urlParams.toString())
            result.append(urlParams.toString())
        }

        return result.toString()
    }


    fun getClient(): OkHttpClient = httpClient
}