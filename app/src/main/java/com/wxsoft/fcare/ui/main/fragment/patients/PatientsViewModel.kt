package com.wxsoft.fcare.ui.main.fragment.patients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.domain.repository.patients.IPatientRepository
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.EventActions
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.core.utils.switchMap
import javax.inject.Inject

class PatientsViewModel @Inject constructor(private val repository: IPatientRepository,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon),EventActions {

    private val _detailAction= MutableLiveData<Event<String>>()
    val detailAction: LiveData<Event<String>>
        get() = _detailAction
    override fun onOpen(t: String) {

        _detailAction.value=Event(t)
    }


    private val patientName = MediatorLiveData<String>()

    private val patientResult = patientName.map{
        repository.getPatients(PatientsCondition("","2019-03-01 01:30:00","2019-03-08 12:30:00","",1,10))
    }

    val patients = patientResult.switchMap {
        it.pagedList
    }

    val networkState = patientResult.switchMap {
        it.networkState
    }

    fun showPatients(name: String): Boolean {
        patientName.value = name
        return true
    }

    fun onSwipeRefresh(): Boolean {
        showPatients(patientName.value?:"")
        return true
    }
}