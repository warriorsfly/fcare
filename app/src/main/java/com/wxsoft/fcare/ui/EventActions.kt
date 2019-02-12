package com.wxsoft.fcare.ui

import android.view.View

interface EventActions:EventAction<String>

interface  EventAction<T>{
    fun onOpen(t:T)
}

interface EmrEventAction{
    fun onNew(type:String)

    fun onOpen(type:String,id:String="")
}

interface PhotoEventAction{
    //点击本地文件
    fun localSelected()
    //点击远程文件查看大图
    fun enlargeRemote(imageView: View, url:String)
}


interface CommitEventAction{
    //点击本地文件
    fun commit(any: Any,type:Int=0)
}