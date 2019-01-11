package com.wxsoft.fcare.ui

import android.net.Uri
import android.view.View
import com.luck.picture.lib.entity.LocalMedia

interface EventActions:EventAction<String>

interface  EventAction<T>{
    fun onOpen(t:T)
}

interface PhotoEventAction{
    //点击本地文件
    fun localSelected()
    //点击远程文件查看大图
    fun enlargeRemote(root: View, url:String)
}