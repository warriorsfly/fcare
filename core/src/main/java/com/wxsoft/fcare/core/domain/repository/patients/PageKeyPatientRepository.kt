package com.wxsoft.fcare.core.domain.repository.patients

import androidx.annotation.MainThread
import androidx.paging.LivePagedListBuilder
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.domain.repository.Listing
import com.wxsoft.fcare.core.domain.source.PatientSourceFactory

class PageKeyPatientRepository constructor(private val api: PatientApi):
    IPatientRepository {
    @MainThread
    override fun getPatients(item: PatientsCondition): Listing<Patient> {

        val factory=PatientSourceFactory(api,item)
        val livePagedList = LivePagedListBuilder(factory, item.pageSize)
            .build()


        return Listing(
            pagedList = livePagedList,
            networkState = factory.source.networkState,
            retry = {},
            refresh = {
                factory.source.invalidate()
            },
            totalCount = factory.source.totalCount
        )
    }

}