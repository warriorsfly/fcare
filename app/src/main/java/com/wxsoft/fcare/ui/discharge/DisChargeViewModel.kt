package com.wxsoft.fcare.ui.discharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.DisChargeDiagnosis
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.DischargeApi
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DisChargeViewModel @Inject constructor(private val api: DischargeApi,
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
            loadDisChargeInfo()
        }

    val data:LiveData<DisChargeDiagnosis>
    private val loadDiagnosisResult = MediatorLiveData<DisChargeDiagnosis>()
    val des:LiveData<List<Dictionary>>
    private val loadDesResult = MediatorLiveData<List<Dictionary>>()

    val commitResult = MediatorLiveData<Resource<Response<String>>>()

    init {
        data = loadDiagnosisResult.map { it?: DisChargeDiagnosis()  }
        des = loadDesResult.map { it ?: emptyList()  }
    }

    private fun loadDisChargeInfo(){
        disposable.add(api.getOtDiagnosis(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadOtDiagnosis,::error))
    }

    private fun loadOtDiagnosis(response:Response<DisChargeDiagnosis>){
        loadDiagnosisResult.value=response.result?.apply { haveLoaded() }
    }

    override fun error(throwable: Throwable){
        messageAction.value= Event(throwable.message?:"未知错误")
    }


    fun click() {
        data.value?.let {
            when {
                it.diagnosis==null->{
                    messageAction.value= Event("诊断未选择")
                    return
                }
                it.diagnosisTime.isNullOrEmpty()->{
                    messageAction.value=Event("诊断时间未选择")
                    return
                }

                (it.days?:0f)<=0f ->{
                    messageAction.value=Event("住院天数未输入")
                    return
                }

            }
            if (it.id.isEmpty()) {
                it.createrId = account.id
                it.createrName = account.trueName
                it.patientId = patientId
            }
            disposable.add(api.saveOtDiagnosis(it).toResource()
                .subscribe { result ->
                    when (result) {
                        is Resource.Success -> {
                            commitResult.value = result
                            messageAction.value = Event(result.data.msg)
                        }
                        is Error -> {
                            messageAction.value = Event(result.message ?: "")
                        }
                    }
                })
        }
    }

//    fun select(dictionary: Dictionary){
//        val dic=des.value?.firstOrNull { it.checked }
//        dic?.checked=false
//        if(dic==dictionary) {
//            data.value?.diagnosisCode=""
//            data.value?.diagnosisName=""
//        }else {
//            dictionary.checked = true
//
//            data.value?.diagnosisCode= dictionary.id
//            data.value?.diagnosisName=dictionary.itemName
//        }
//    }

}