package com.wxsoft.fcare.core.domain.repository.patients

import androidx.paging.LivePagedListBuilder
import androidx.annotation.MainThread
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.domain.repository.Listing
import com.wxsoft.fcare.core.domain.source.PatientSourceFactory
import com.wxsoft.fcare.core.utils.map

class PageKeyPatientRepository constructor(private val api: PatientApi):
    IPatientRepository {
    @MainThread
    override fun getPatients(item: PatientsCondition): Listing<Patient> {

        val factory=PatientSourceFactory(api,item)
        val livePagedList = LivePagedListBuilder(factory, item.pageSize)
            .build()

        return Listing(
            pagedList = livePagedList,
            networkState = factory.sourceLiveData.map {
                it.networkState.value?:false
            },
            retry = {
//                factory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                factory.sourceLiveData.value?.invalidate()
            }
        )
    }

}