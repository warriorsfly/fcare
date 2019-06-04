package com.wxsoft.fcare.core.domain.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.remote.TaskApi

class TaskSourceFactory (
    api: TaskApi,
    item: PatientsCondition
) : DataSource.Factory<Int, Task>() {

    val source = TaskSource(api,item)
    override fun create(): DataSource<Int, Task> {

        return source
    }
}