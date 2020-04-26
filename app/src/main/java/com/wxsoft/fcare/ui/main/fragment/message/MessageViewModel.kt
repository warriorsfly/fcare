package com.wxsoft.fcare.ui.main.fragment.patients

import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.MessageApi
import com.wxsoft.fcare.core.domain.repository.message.IMessageRepository
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.core.utils.switchMap
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MessageViewModel @Inject constructor(private val repository: IMessageRepository,
                                           private val messageApi: MessageApi,
                                           override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon) {


    val userId = MediatorLiveData<String>().apply {
        value=account.id
    }

    private val patientResult = userId.map{
        repository.getMessages(it,1,20)
    }

    val messages = patientResult.switchMap {
        it.pagedList
    }

    val networkState = patientResult.switchMap {
        it.networkState
    }
    val totolCount = patientResult.switchMap {
        it.totalCount
    }

    fun onSwipeRefresh(): Boolean {
        userId.value = account.id
        return true
    }

    fun ignoreMessage(id:String){
        disposable.add(messageApi.ignoreMessage(id,account.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::ignoreMessageResult,::error))
    }
    private fun ignoreMessageResult(response: Response<String>){
        messageAction.value = Event(response.msg)
        userId.value = account.id
    }


}