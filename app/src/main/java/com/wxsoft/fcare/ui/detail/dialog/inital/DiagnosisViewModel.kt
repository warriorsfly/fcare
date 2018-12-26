package com.wxsoft.fcare.ui.detail.dialog.inital

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.R
import com.wxsoft.fcare.data.entity.Account
import com.wxsoft.fcare.data.entity.Dictionary
import com.wxsoft.fcare.data.entity.Response
import com.wxsoft.fcare.data.entity.chest.InitialDiagnosis
import com.wxsoft.fcare.data.entity.chest.InitialDiagnosisDetail
import com.wxsoft.fcare.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.data.remote.DictEnumApi
import com.wxsoft.fcare.data.remote.InitialDiagnosisApi
import com.wxsoft.fcare.data.toResource
import com.wxsoft.fcare.data.toWxResource
import com.wxsoft.fcare.result.Resource
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class DiagnosisViewModel @Inject constructor(private val diagnosisApi: InitialDiagnosisApi,
                                             private val dictApi:DictEnumApi,
                                             gson: Gson,
                                             sharedPreferenceStorage: SharedPreferenceStorage): ViewModel() {

    private val account:Account
    var patientId:String=""
    set(value) {
        field=value
        loadDiagnosis()
    }
    private val loadDiagnosisResult=MediatorLiveData<Resource<Response<InitialDiagnosis?>>>()
    private val saveDiagnosisResult=MediatorLiveData<Resource<String>>()
    private val selectDiagnosisResult=MediatorLiveData<Boolean>()
    private val savableDiagnosisResult=MediatorLiveData<Boolean>()
    private val load4DiagnosisResult=MediatorLiveData<Resource<List<Dictionary>>>()
    private val load27DiagnosisResult=MediatorLiveData<Resource<List<Dictionary>>>()
    val second:LiveData<Boolean>
    val liveData:LiveData<String>
    val savable:LiveData<Boolean>
    val liveDiagnosis:LiveData<Response<InitialDiagnosis?>>
    val diagnosis4Type:LiveData<List<Dictionary>>
    val diagnosis27Type:LiveData<List<Dictionary>>
    init {

        diagnosis4Type=load4DiagnosisResult.map {   (it as? Resource.Success)?.data?: emptyList()}
        diagnosis27Type=load27DiagnosisResult.map {   (it as? Resource.Success)?.data?: emptyList()}
        savable=savableDiagnosisResult.map { it }
        second=selectDiagnosisResult.map { it}
        liveData=saveDiagnosisResult.map {   (it as? Resource.Success)?.data?: ""}
        val userInfo=sharedPreferenceStorage.userInfo!!
        account=gson.fromJson(userInfo,Account::class.java)
        liveDiagnosis=loadDiagnosisResult.map {
            (it as? Resource.Success)?.data?: Response(false)
        }
//        load4Diagnosis()
//        load27Diagnosis()
    }

    private fun loadDiagnosis() {
        dictApi.loadDict4Diagnosis().toResource().doAfterSuccess {
            dictApi.loadDict27Diagnosis().toResource().doAfterSuccess {
                diagnosisApi.getDiagnosis(patientId).toResource().subscribe {

                    loadDiagnosisResult.value = it
                    if (it is Resource.Success) {
                        (load4DiagnosisResult.value as? Resource.Success)?.data?.forEach { item ->
                            item.checked = item.itemCode == it.data.result?.diagnosis_Code
                        }
                        (load27DiagnosisResult.value as? Resource.Success)?.data?.forEach { item ->
                            item.checked = it.data?.result?.initialDiagnosisDetails?.any { detial ->
                                detial.code == item.itemCode
                            } ?: false
                        }
                    }
                }
            } .subscribe {load27DiagnosisResult.value = it}
        }.subscribe { load4DiagnosisResult.value = it }

    }

    fun saveDiagnosis(){
     if(liveDiagnosis.value==null)return
        val diag=liveDiagnosis.value?.result?:InitialDiagnosis("")
        if(diag.id.isEmpty()){
            diag.patientId=patientId
            diag.createrId=account.id
            diag.createrName=account.userName
            diag.diagnosis_Doctor_Name=account.userName
            if(diag.diagnosis_Code=="8") {

                val list=diagnosis27Type.value!!.filter { it.checked } .map { InitialDiagnosisDetail(it.itemCode) }
                diag.initialDiagnosisDetails=list
            }
        }
        (if(diag.id.isEmpty()) diagnosisApi.insert(diag) else diagnosisApi.update(diag)).toResource()
            .subscribe{
                saveDiagnosisResult.value=it
            }
    }

    fun select4(code4:String, id:Int) {
        code = code4
        liveDiagnosis.value?.result?.diagnosis_Code = code4
        selectDiagnosisResult.value = code4 == "8" && id >= 0
        savableDiagnosisResult.value = (code4 != "8" && id >= 0)

    }

    private var code=""

    fun select27() {

        val savaleFlag = code == "8" && diagnosis27Type.value!!.any { it.checked }
        savableDiagnosisResult.value = savaleFlag

    }
}