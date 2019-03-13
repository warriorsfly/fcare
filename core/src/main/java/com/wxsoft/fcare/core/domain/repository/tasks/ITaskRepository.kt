package com.wxsoft.fcare.core.domain.repository.tasks

import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.domain.repository.Listing

interface ITaskRepository {
    fun getTasks(item: PatientsCondition): Listing<Task>
}