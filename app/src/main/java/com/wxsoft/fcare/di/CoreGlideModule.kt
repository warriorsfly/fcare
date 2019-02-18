package com.wxsoft.fcare.di

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class YourAppGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //80MB缓存
        val memoryCacheSizeBytes:Long = 1024 * 1024 * 80
        builder.setMemoryCache( LruResourceCache(memoryCacheSizeBytes))
    }
}