package com.wxsoft.emergency.ui.main.fragment.patients

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.map
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.domain.repository.patients.IPatientRepository
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.utils.map
import com.wxsoft.fcare.utils.switchMap
import javax.inject.Inject

class PatientsViewModel @Inject constructor(private val repository: IPatientRepository,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon) {

    private val patientName = MediatorLiveData<String>()

    private val patientResult = patientName.map{
        repository.getPatients(it, 10)
    }

    val patients = patientResult.switchMap {
        it.pagedList
    }
    val networkState = patientResult.switchMap {
        it.networkState
    }

    fun showPatients(name: String): Boolean {
        if (patientName.value == name) {
            return false
        }
        patientName.value = name
        return true
    }
}