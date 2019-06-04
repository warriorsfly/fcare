package com.wxsoft.fcare.core.domain.repository.tasks

import androidx.annotation.MainThread
import androidx.paging.LivePagedListBuilder
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.domain.repository.Listing
import com.wxsoft.fcare.core.domain.repository.patients.IPatientRepository
import com.wxsoft.fcare.core.domain.source.PatientSourceFactory
import com.wxsoft.fcare.core.domain.source.TaskSourceFactory
import com.wxsoft.fcare.core.utils.map

class PageKeyTaskRepository constructor(private val api: TaskApi):
    ITaskRepository {
    @MainThread
    override fun getTasks(item: PatientsCondition): Listing<Task> {

        val factory= TaskSourceFactory(api,item)
        val livePagedList = LivePagedListBuilder(factory, item.pageSize)
            .build()

        return Listing(
            pagedList = livePagedList,
            networkState = factory.source.networkState,
            retry = {
                //                factory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                factory.source.invalidate()
            },
            totalCount = factory.source.totalCount
        )
    }

}