package com.wxsoft.fcare.core.domain.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.wxsoft.fcare.core.data.entity.Message
import com.wxsoft.fcare.core.data.remote.MessageApi

class MessageSourceFactory (
    private val api: MessageApi,
    private val id:String,
    private val size:Int
) : DataSource.Factory<Int, Message>() {

    val sourceLiveData = MutableLiveData<MessageSource>()

    override fun create(): DataSource<Int, Message> {
        val source = MessageSource(api,id,size)
        sourceLiveData.postValue(source)
        return source
    }
}