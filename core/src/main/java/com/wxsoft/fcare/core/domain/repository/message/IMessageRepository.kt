package com.wxsoft.fcare.core.domain.repository.message

import com.wxsoft.fcare.core.data.entity.Message
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.domain.repository.Listing

interface IMessageRepository {
    fun getMessages(id:String, index:Int, size:Int): Listing<Message>
}