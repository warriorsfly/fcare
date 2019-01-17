package com.wxsoft.fcare.core.domain.repository.patients

import android.arch.paging.LivePagedListBuilder
import android.support.annotation.MainThread
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.domain.repository.Listing
import com.wxsoft.fcare.core.domain.source.PatientSourceFactory
import com.wxsoft.fcare.utils.map
import java.util.concurrent.Executor
import javax.inject.Inject

class PageKeyPatientRepository constructor(private val api: PatientApi):
    IPatientRepository {

    @MainThread
    override fun getPatients(key: String, pageSize: Int): Listing<Patient> {

        val factory=PatientSourceFactory(api,key)
        val livePagedList = LivePagedListBuilder(factory, pageSize)
            .build()

        val refreshState =factory.sourceLiveData.map {

        }
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