package com.wxsoft.fcare

import android.app.Notification
import cn.jiguang.share.android.api.JShareInterface
import cn.jiguang.share.android.api.PlatformConfig
import cn.jpush.android.api.BasicPushNotificationBuilder
import cn.jpush.android.api.JPushInterface
import com.baidu.mapapi.SDKInitializer
//import com.squareup.leakcanary.LeakCanary
import com.wxsoft.fcare.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class App : DaggerApplication() {


    override fun onCreate() {
        super.onCreate()

//        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            LeakCanary.install(this)
//        }

        if (BuildConfig.DEBUG) {
            JPushInterface.setDebugMode(true)
            JShareInterface.setDebugMode(true)
        }

        JPushInterface.init(this)
        val builder = BasicPushNotificationBuilder(this)
        builder.notificationDefaults = (Notification.DEFAULT_SOUND
                or Notification.DEFAULT_VIBRATE
                or Notification.DEFAULT_LIGHTS)  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder)


        val config = PlatformConfig().apply {
            setWechat("wx9f163d591186cfac", "c93e41a9676c998d865389acfec27548")
        }
        JShareInterface.init(this, config)

        SDKInitializer.initialize(this)

    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}