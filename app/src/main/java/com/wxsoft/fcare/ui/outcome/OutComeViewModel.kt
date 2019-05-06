package com.wxsoft.fcare.ui.outcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.chest.OutCome
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DischargeApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.core.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OutComeViewModel @Inject constructor(private val api: DischargeApi,
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
            loadOutCome()
        }

    val data:LiveData<OutCome>
    private val loadDiagnosisResult = MediatorLiveData<Response<OutCome>>()
    private val des:LiveData<List<Dictionary>>
    private val loadDesResult = MediatorLiveData<List<Dictionary>>()

    val commitResult = MediatorLiveData<Resource<Response<String>>>()

    init {
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



    fun click() {
        data.value?.let {
            when(it.outcomeCode){
                "11-1"->{
                    if(it.handTime.isNullOrEmpty()){
                        messageAction.value=Event("出院时间未选择")
                        return
                    }

                    if(it.resultCode.isEmpty()){
                        messageAction.value=Event("治疗结果未选择")
                        return
                    }

                }
                "11-2"-> {
                    if (it.handTime.isNullOrEmpty()) {
                        messageAction.value = Event("离开本院大门未选择")
                        return
                    }

                    if (it.hospitalName.isEmpty()) {
                        messageAction.value = Event("请选择医院")
                        return
                    }

                    if (it.pci && it.operationTime.isNullOrEmpty()) {
                        messageAction.value = Event("请选择介入手术时间")
                        return
                    }
                }
                "11-3"->{
                    if (it.admissionDept.isEmpty()) {
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

    fun selectString(code:String){
        when(code){
            "1-1" ->{data.value?.outDapt = !data.value?.outDapt!!}
            "1-2" ->{data.value?.outAceiorarb = !data.value?.outAceiorarb!!}
            "1-3" ->{data.value?.outStatins = !data.value?.outStatins!!}
            "1-4" ->{data.value?.outRetardant = !data.value?.outRetardant!!}
        }

    }

}