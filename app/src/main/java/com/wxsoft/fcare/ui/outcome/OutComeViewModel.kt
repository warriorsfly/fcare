package com.wxsoft.fcare.ui.outcome

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.DisChargeDiagnosis
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.chest.OutCome
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.DischargeApi
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OutComeViewModel @Inject constructor(private val api: DischargeApi,
                                             override val sharedPreferenceStorage: SharedPreferenceStorage,
                                             override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title: String=""
        get() = "患者转归"
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
            loadOutCome()
        }

    val data:LiveData<OutCome>
    private val loadDiagnosisResult = MediatorLiveData<Response<OutCome>>()
    val des:LiveData<List<Dictionary>>
    private val loadDesResult = MediatorLiveData<List<Dictionary>>()

    val commitResult = MediatorLiveData<Resource<Response<String>>>()

    init {
        clickable = clickResult.map { it }
        data = loadDiagnosisResult.map { it?.result ?: OutCome(patientId=patientId,createrId = account.id)  }
        des = loadDesResult.map { it ?: emptyList()  }
    }

    private fun loadOutCome(){
        disposable.add(
            api.getOt(patientId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                loadDiagnosisResult.value=it
            }
        )
    }



    override fun click() {
        data.value?.let {
            when(it.outcomeCode){
                "11-1"->{
                    if(it.handTime.isNullOrEmpty()){
                        messageAction.value=Event("出院时间未选择")
                        return
                    }

                    if(it.resultCode.isNullOrEmpty()){
                        messageAction.value=Event("治疗结果未选择")
                        return
                    }

                }
                "11-2"-> {
                    if (it.handTime.isNullOrEmpty()) {
                        messageAction.value = Event("离开本院大门未选择")
                        return
                    }

                    if (it.hospitalName.isNullOrEmpty()) {
                        messageAction.value = Event("请选择医院")
                        return
                    }

                    if (it.pci && it.operationTime.isNullOrEmpty()) {
                        messageAction.value = Event("请选择介入手术时间")
                        return
                    }
                }
                "11-3"->{
                    if (it.admissionDept.isNullOrEmpty()) {
                        messageAction.value = Event("请选择接诊科室")
                        return
                    }


                }
                "11-4"->{
                    if (it.deadAt.isNullOrEmpty()) {
                        messageAction.value = Event("请输入死亡时间")
                        return
                    }
                }
            }

            disposable.add(api.saveOt(
                it).toResource().subscribe {result->
                commitResult.value=result
            }
            )
        }


    }

}