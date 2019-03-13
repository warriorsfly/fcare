package com.wxsoft.fcare.core.domain.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.remote.TaskApi

class TaskSourceFactory (
    private val api: TaskApi,
    private val item: PatientsCondition
) : DataSource.Factory<Int, Task>() {

    val sourceLiveData = MutableLiveData<TaskSource>()

    override fun create(): DataSource<Int, Task> {
        val source = TaskSource(api,item)
        sourceLiveData.postValue(source)
        return source
    }
}