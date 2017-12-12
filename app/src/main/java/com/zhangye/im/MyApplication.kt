package com.zhangye.im

import android.app.Application
import com.squareup.leakcanary.LeakCanary

/**
 * Created by zhangye on 2017/11/6.
 */
class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //初始化首煤SDK
        SMClient.getInstance().init(instance)

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        // Normal app init code...

    }
}