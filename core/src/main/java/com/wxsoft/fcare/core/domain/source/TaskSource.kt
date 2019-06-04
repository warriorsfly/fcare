package com.wxsoft.fcare.core.domain.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map

class TaskSource constructor(
    private val api: TaskApi,
    private val requestitem: PatientsCondition
) : PageKeyedDataSource<Int, Task>() {
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Task>) {
        //ignore
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Task>) {

//        if(params.key==null) return
        requestitem.pageIndex = params.key
        api.getTasks(requestitem)
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        callback.onResult(it.data.items,if(it.data.hasNextPage)params.key+1 else null)
                        totalCount.value=it.data.totalCount
                    }
                }
            }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Task>) {
        requestitem.pageIndex = 1
        api.getTasks(requestitem)
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        callback.onResult(it.data.items,null,if(it.data.hasNextPage)  it.data.pageIndex+1 else null)
                        totalCount.value=it.data.totalCount
                    }
                }
            }
    }

    val networkState : LiveData<Boolean>
    val totalCount=MediatorLiveData<Int>()
    private val loadPatientResult= MediatorLiveData<Resource<Response<PagedList<Task>>>>()

    init {
        networkState=loadPatientResult.map {
            it== Resource.Loading
        }
    }


}