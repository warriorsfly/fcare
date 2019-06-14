package com.wxsoft.fcare.ui.main.fragment.patients.searchpatients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.EventActions
import javax.inject.Inject

class SearchPatientsViewModel  @Inject constructor(private val patientApi: PatientApi,
                                                   override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                   override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon), EventActions {
    private val _detailAction= MutableLiveData<Event<String>>()
    val detailAction: LiveData<Event<String>>
        get() = _detailAction
    override fun onOpen(t: String) {
        _detailAction.value= Event(t)
    }

    val patients: LiveData<List<Patient>>
    private val loadPatientsResult = MediatorLiveData<Resource<Response<List<Patient>>>>()


    init {
        patients = loadPatientsResult.map { (it as? Resource.Success)?.data?.result?: emptyList() }
    }

    fun showPatients(name: String): Boolean {
        patientApi.searchPatients(name,account.id).toResource()
            .subscribe {
                loadPatientsResult.value = it
            }
        return true
    }


}