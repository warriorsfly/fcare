package com.wxsoft.fcare.di

import android.content.ClipboardManager
import android.content.Context
import android.net.wifi.WifiManager
import com.wxsoft.fcare.App
import com.wxsoft.fcare.data.prefs.PreferenceStorage
import com.wxsoft.fcare.data.prefs.SharedPreferenceStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providesPreferenceStorage(context: Context): PreferenceStorage =
            SharedPreferenceStorage(context)

    @Provides
    fun providesWifiManager(context: Context): WifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    fun providesClipboardManager(context: Context): ClipboardManager =
            context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE)
                    as ClipboardManager





}