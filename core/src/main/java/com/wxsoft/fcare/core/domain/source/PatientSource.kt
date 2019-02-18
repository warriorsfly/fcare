package com.wxsoft.fcare.core.domain.source

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.paging.PageKeyedDataSource
import android.arch.paging.PagedList
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map

class PatientSource constructor(
    private val api:PatientApi,
    private val name:String
) : PageKeyedDataSource<Int, Patient>() {
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Patient>) {
        //ignore
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Patient>) {

        if(params.key==null) return
        api.getPagedPatients(name,params.key,params.requestedLoadSize)
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{

                        callback.onResult(it.data.items,if(it.data.hasNextPage)params.key+1 else null)
                    }
                }
            }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Patient>) {

        api.getPagedPatients(name,1,params.requestedLoadSize)
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        callback.onResult(it.data.items,null,if(it.data.hasNextPage)  it.data.pageIndex+3 else null)
                    }
                }
            }
    }

    val networkState : LiveData<Boolean>

    private val loadPatientResult= MediatorLiveData<Resource<Response<PagedList<Patient>>>>()

    init {
        networkState=loadPatientResult.map {
            it==Resource.Loading
        }
    }

}