package com.wxsoft.fcare.ui.outcome.outcomecheck

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.OutComeCheck
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DischargeApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OutComeCheckViewModel @Inject constructor(private val api: DischargeApi,
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
            loadOutComeCheck()
        }

    val data: LiveData<OutComeCheck>
    private val loadDataResult = MediatorLiveData<Response<OutComeCheck>>()
    val saveResult:LiveData<String>
    private val loadSaveResult = MediatorLiveData<String>()

    init {
        data = loadDataResult.map { it?.result ?:OutComeCheck("").apply { patientId = this@OutComeCheckViewModel.patientId } }
        saveResult = loadSaveResult.map { it }
    }


    private fun loadOutComeCheck(){
        disposable.add(
            api.getOutcheck(patientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    loadDataResult.value=it
                }
        )
    }

    override fun error(throwable: Throwable){
        messageAction.value= Event(throwable.message?:"未知错误")
    }


    fun save(){
        disposable.add(api.saveOutChek(data.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::saveResult,::error))
    }
    private fun saveResult(response: Response<String>){
        loadSaveResult.value = response.result
    }




}