package com.wxsoft.fcare.ui.patient

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val sharedPreferenceStorage: SharedPreferenceStorage,
    private val gson: Gson
) : BaseViewModel() {

    val patient:LiveData<Patient>

    private val loadPatientResult =MediatorLiveData<Response<Patient>>().apply {

        value=Response<Patient>(true).apply {
            result=Patient("")
        }
    }

    init {
        patient=loadPatientResult.map {
            it.result?:Patient("")
        }
    }
}