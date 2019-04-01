package com.wxsoft.fcare.core.domain.repository.message

import androidx.annotation.MainThread
import androidx.paging.LivePagedListBuilder
import com.wxsoft.fcare.core.data.entity.Message
import com.wxsoft.fcare.core.data.remote.MessageApi
import com.wxsoft.fcare.core.domain.repository.Listing
import com.wxsoft.fcare.core.domain.source.MessageSourceFactory
import com.wxsoft.fcare.core.utils.map

class PageKeyMessageRepository constructor(private val api: MessageApi):IMessageRepository {
    @MainThread
    override fun getMessages(id:String, index:Int, size:Int): Listing<Message> {

        val factory= MessageSourceFactory(api,id,size)
        val livePagedList = LivePagedListBuilder(factory, size)
            .build()

        return Listing(
            pagedList = livePagedList,
            networkState = factory.sourceLiveData.map {
                it.networkState.value?:false
            },
            retry = {
                //                factory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                factory.sourceLiveData.value?.invalidate()
            }
        )
    }

}