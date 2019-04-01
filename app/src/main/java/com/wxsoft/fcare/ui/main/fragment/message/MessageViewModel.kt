package com.wxsoft.fcare.ui.main.fragment.patients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.PatientsCondition
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.domain.repository.message.IMessageRepository
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.core.utils.switchMap
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.EventActions
import java.util.*
import javax.inject.Inject


class MessageViewModel @Inject constructor(private val repository: IMessageRepository,
                                           override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon) {


    val userId = MediatorLiveData<String>().apply {
        value=account.id
    }

    private val patientResult = userId.map{
        repository.getMessages(it,1,10)
    }

    val messages = patientResult.switchMap {
        it.pagedList
    }

    val networkState = patientResult.switchMap {
        it.networkState
    }

}