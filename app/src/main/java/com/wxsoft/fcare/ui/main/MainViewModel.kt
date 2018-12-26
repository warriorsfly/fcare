package com.wxsoft.fcare.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.wxsoft.fcare.data.entity.Patient
import com.wxsoft.fcare.data.remote.PatientApi
import com.wxsoft.fcare.data.toResource
import com.wxsoft.fcare.result.Event
import com.wxsoft.fcare.result.Resource
import com.wxsoft.fcare.ui.detail.EventActions
import com.wxsoft.fcare.utils.map
import javax.inject.Inject


class MainViewModel @Inject constructor(val  api:PatientApi): ViewModel() , EventActions {


    private val _navigateToOperationAction = MutableLiveData<Event<String>>()
    val patient: LiveData<Patient>

    private val loadPatientResult= MediatorLiveData<Resource<Patient>>()

    init {

        patient = loadPatientResult.map {
            (it as? Resource.Success)?.data ?: Patient("")
        }

    }

    override fun onOpen(id: String) {
        _navigateToOperationAction.value=Event(id)
    }

    fun loadByRfid(rFid:String){
        api.getPatientByRFID(rFid).toResource()
            .subscribe { result ->

                if(result is Resource.Success  && result.data.success)
                loadPatientResult.value = Resource.Success(result.data.result?:Patient(""))
            }

    }

}