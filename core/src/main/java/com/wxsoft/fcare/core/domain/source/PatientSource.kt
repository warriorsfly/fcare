package com.wxsoft.fcare.core.domain.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map

class PatientSource constructor(
    private val api:PatientApi,
    private val requestitem: PatientsCondition
) : PageKeyedDataSource<Int, Patient>() {
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Patient>) {
        //ignore
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Patient>) {

//        if(params.key==null) return
        requestitem.pageIndex = params.key
        api.getPatients(requestitem)
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
        requestitem.pageIndex = 1
        api.getPatients(requestitem)
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