package com.wxsoft.fcare.ui.workspace.notify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.NotificationApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class OneTouchCallingViewModel @Inject constructor(private val notificationApi: NotificationApi,
                                                   private val dicApi: DictEnumApi,
                                                   override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                   override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    val calls: LiveData<List<Dictionary>>
    private val loadCalls= MediatorLiveData<List<Dictionary>>()

    val callNumber: LiveData<String>
    private val loadCallNumber= MediatorLiveData<String>()

    val callResult: LiveData<Int>
    private val loadCallResult = MediatorLiveData<Int>()

    init {
        calls = loadCalls.map { it ?: emptyList()}
        callNumber = loadCallNumber.map { it }
        callResult = loadCallResult.map { it }
    }

    fun getCalls(){
        disposable.add(dicApi.loadCalls(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadCalls,::error))
    }

    private fun loadCalls(response:List<Dictionary>){
        loadCalls.value = response
    }


    fun call(item:Dictionary){
        loadCallNumber.value = item.itemName
        submitCall()
    }

    fun submitCall(){
        disposable.add(notificationApi.submitCallTime(patientId,account.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::saveCallResult,::error))
    }

    fun saveCallResult(response:Response<Int>){
        loadCallResult.value = response.result

    }

}