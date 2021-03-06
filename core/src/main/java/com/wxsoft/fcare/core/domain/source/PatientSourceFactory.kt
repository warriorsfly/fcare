package com.wxsoft.fcare.core.domain.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.remote.PatientApi

class PatientSourceFactory (
    private val api: PatientApi,
    private val item:PatientsCondition
) : DataSource.Factory<Int, Patient>() {

    val sourceLiveData = MutableLiveData<PatientSource>()

    override fun create(): DataSource<Int, Patient> {
        val source = PatientSource(api,item)
        sourceLiveData.postValue(source)
        return source
    }
}