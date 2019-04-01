package com.wxsoft.fcare.core.domain.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.wxsoft.fcare.core.data.entity.Message
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.remote.MessageApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map

class MessageSource constructor(
    private val api: MessageApi,
    private val id:String,
    private val size:Int
) : PageKeyedDataSource<Int, Message>() {
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Message>) {
        //ignore
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Message>) {

//        if(params.key==null) return
        val pageIndex = params.key
        api.loadMeasure(id,pageIndex,size)
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        callback.onResult(it.data.items,if(it.data.hasNextPage)params.key+1 else null)
                    }
                }
            }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Message>) {
        api.loadMeasure(id,1,size)
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        callback.onResult(it.data.items,null,if(it.data.hasNextPage)  it.data.pageIndex+1 else null)
                    }
                }
            }
    }

    val networkState : LiveData<Boolean>

    private val loadPatientResult= MediatorLiveData<Response<PagedList<Message>>>()

    init {
        networkState=loadPatientResult.map {
            it.success
        }
    }


}