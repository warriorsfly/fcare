package com.wxsoft.fcare.core.domain.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.wxsoft.fcare.core.data.entity.Message
import com.wxsoft.fcare.core.data.remote.MessageApi

class MessageSourceFactory (
    api: MessageApi,
    id:String,
    size:Int
) : DataSource.Factory<Int, Message>() {
    val source = MessageSource(api,id,size)
    override fun create(): DataSource<Int, Message> {
        return source
    }
}