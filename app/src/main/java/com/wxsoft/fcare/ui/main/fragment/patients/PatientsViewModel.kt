package com.wxsoft.fcare.ui.main.fragment.patients

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel;
import com.wxsoft.fcare.data.NetFlowableResource
import com.wxsoft.fcare.data.entity.Patient
import com.wxsoft.fcare.data.entity.Response
import com.wxsoft.fcare.data.local.dao.PatientDao
import com.wxsoft.fcare.data.remote.PatientApi
import com.wxsoft.fcare.result.Event
import com.wxsoft.fcare.result.Resource
import com.wxsoft.fcare.ui.detail.EventActions
import com.wxsoft.fcare.utils.map
import io.reactivex.Flowable
import javax.inject.Inject

class PatientsViewModel @Inject constructor(val  api: PatientApi,
                                            val dao: PatientDao): ViewModel(), EventActions {

    val isLoading: LiveData<Boolean>
    val patients: LiveData<List<Patient>>
    private val _navigateToOperationAction = MutableLiveData<Event<String>>()
    private val loadPatientsResult= MediatorLiveData<Resource<List<Patient>>>()
    val navigateToOperationAction: LiveData<Event<String>>
        get() = _navigateToOperationAction

    init {

        load()
        isLoading=loadPatientsResult.map { it== Resource.Loading }

        patients = loadPatientsResult.map {

            var ps=(it as? Resource.Success)?.data ?: emptyList()

            if(ps.isNotEmpty()){
                for(p in ps){
                    if(ps.indexOf(p)%3==0){
                        p.statu="急救中"
                    }
                }
            }
            return@map ps


        }

    }

    fun onSwipeRefresh() {
        load()
    }

    private fun load() {

        object : NetFlowableResource<List<Patient>, Response<List<Patient>>>() {
            override fun loadFromDb(): Flowable<List<Patient>> {
                return dao.getAll()
            }

            override fun createCall(): Flowable<Response<List<Patient>>> {
                return api.patients()
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun saveCallResult(request: Response<List<Patient>>) {

                if (request.success && !request.result.isNullOrEmpty()) {
                    dao.insertAll(request.result!!)
                }
            }


        }.data.subscribe { loadPatientsResult.value = it }
    }

    override fun onOpen(id: String) {
        _navigateToOperationAction.value= Event(id)
    }


}