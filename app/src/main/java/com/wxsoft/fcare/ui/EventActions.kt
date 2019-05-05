package com.wxsoft.fcare.ui

import android.view.View
import android.widget.ImageView

interface EventActions:EventAction<String>

interface  EventAction<T>{
    fun onOpen(t:T)
}

interface PhotoEventAction{
    //点击本地文件
    fun localSelected()
    //点击远程文件查看大图
    fun enlargeRemote(imageView: View, url:String)
    fun deleteRemote(url:String)
}

interface PlayVoiceEventAction{
    //点击播放语音文件
    fun play(imageView: ImageView,url:String)
}