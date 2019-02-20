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
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DisChargeViewModel @Inject constructor(private val api: DischargeApi,
                                             private val enumApi:DictEnumApi,
                                             private val patientApi: PatientApi,
                                             override val sharedPreferenceStorage: SharedPreferenceStorage,
                                             override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title= "诊断"
    override val clickableTitle: String
        get() = "保存"
    override val clickable: LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }

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
    private val loadDiagnosisResult = MediatorLiveData<Response<DisChargeDiagnosis>>()
    val des:LiveData<List<Dictionary>>
    private val loadDesResult = MediatorLiveData<List<Dictionary>>()

    val commitResult = MediatorLiveData<Resource<Response<String>>>()

    init {
        clickable = clickResult.map { it }
        data = loadDiagnosisResult.map { it?.result ?: DisChargeDiagnosis()  }
        des = loadDesResult.map { it ?: emptyList()  }
    }

    private fun loadDisChargeInfo(){


        disposable.add(

            patientApi.getOne(patientId).flatMapMaybe {
                when (it.result?.diagnosisCode) {
                    /**
                     * 胸痛
                     */
                    "215-1" -> enumApi.loadDict4Diagnosis()
                    /**
                     * 卒中
                     */
                    "215-2" -> enumApi.loadApoplexyResultItems()
                    //                /**
//                 * 创伤
//                 */
//                "215-3"->enumApi.loadDict4Diagnosis()
//                /**
//                 * 孕产妇
//                 */
//                "215-4"->enumApi.loadDict4Diagnosis()
//                /**
//                 * 新生儿
//                 */
//                "215-5"->enumApi.loadDict4Diagnosis()
                    /**
                     * 其他类暂时不考虑
                     */
                    else -> throw IllegalStateException("Unknown diagnosis code $it")
                }}.zipWith(api.getOtDiagnosis(patientId))
                    .subscribeOn(Schedulers.io())
                    .doOnSuccess { zip ->
                        zip.first.firstOrNull { it.id == zip.second.result?.diagnosisCode }?.checked = true
                    }.observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        loadDesResult.value=it.first
                        loadDiagnosisResult.value=it.second
                    })
    }



    override fun click() {


        data.value?.let {
            when {
                it.diagnosisCode.isEmpty()->{
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

    fun select(dictionary: Dictionary){
        val dic=des.value?.firstOrNull { it.checked }
        dic?.checked=false
        if(dic==dictionary) {
            data.value?.diagnosisCode=""
            data.value?.diagnosisName=""
        }else {
            dictionary.checked = true

            data.value?.diagnosisCode= dictionary.id
            data.value?.diagnosisName=dictionary.itemName
        }
    }

}