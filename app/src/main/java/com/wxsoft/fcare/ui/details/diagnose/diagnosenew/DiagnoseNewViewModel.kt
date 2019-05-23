package com.wxsoft.fcare.ui.details.diagnose.diagnosenew

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.entity.drug.ACSDrug
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DiagnoseApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DiagnoseNewViewModel @Inject constructor(private val diagnoseApi: DiagnoseApi,
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
            getTreaatment()
        }

    var doctorName=ObservableField<String>()
    var graceScore=ObservableField<Int>()
    var talkShow=ObservableField<Boolean>()


    var diagnosis: LiveData<Diagnosis>
    var loadDiagnosis = MediatorLiveData<Diagnosis>()

    var selectedTreatment: LiveData<Strategy>
    var loadSelectedTreatment = MediatorLiveData<Strategy>()

    var diagnosisTreatment: LiveData<DiagnoseTreatment>
    var loadDiagnosisTreatment = MediatorLiveData<DiagnoseTreatment>()

    val acsDrug: LiveData<ACSDrug>
    val loadAcsDrug = MediatorLiveData<ACSDrug>()

    val talk: LiveData<Talk>
    val loadTalk = MediatorLiveData<Talk>()

    val saveResult: LiveData<String>
    val loadSaveResult= MediatorLiveData<String>()

    init {
        diagnosisTreatment = loadDiagnosisTreatment.map { it?:DiagnoseTreatment("") }
        diagnosis = loadDiagnosis.map { it?:Diagnosis(createrId = account.id,createrName = account.trueName) }
        selectedTreatment = loadSelectedTreatment.map { it?: Strategy(patientId,1) }
        acsDrug = loadAcsDrug.map { it?: ACSDrug("") }
        talk = loadTalk.map { it?: Talk("") }
        haveData()
        saveResult = loadSaveResult.map { it }
    }

    fun getTreaatment(){
        disposable.add(diagnoseApi.getTreatment(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getData,::error))
    }

    private fun getData(response: Response<DiagnoseTreatment>){
        loadDiagnosisTreatment.value = response.result
        haveData()
    }
    fun haveData(){
        loadSelectedTreatment.value = diagnosisTreatment.value?.treatStrategy
        loadDiagnosis.value = diagnosisTreatment.value?.diagnosis
        if (diagnosis.value?.doctorName.isNullOrEmpty()) doctorName.set(account.trueName) else doctorName.set(diagnosis.value?.doctorName)
        loadAcsDrug.value = diagnosisTreatment.value?.acs?.apply {
            haveDrugs()
        }
        loadTalk.value = diagnosisTreatment.value?.talk?.apply {
            judgeTime()
        }
        graceScore.set(diagnosisTreatment.value?.graceRating?.score)
        talkShow.set(!diagnosisTreatment.value?.talk?.allTime.isNullOrEmpty())
    }

    fun saveDiagnose(){

        diagnosisTreatment.value?.acs = acsDrug.value!!
        diagnosisTreatment.value?.treatStrategy = selectedTreatment.value!!
        diagnosisTreatment.value?.talk = talk.value!!
        diagnosisTreatment.value?.diagnosis?.doctorName = doctorName.get()!!
        if (saveable){
            disposable.add(diagnoseApi.saveNewDiagnose(diagnosisTreatment.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (::loadResult,::error))
        }
    }
    private fun loadResult(response: Response<String>){
        if (response.success) loadSaveResult.value = "success"
    }

    val saveable:Boolean
        get(){
            return diagnosisTreatment.value?.let {
                if(it.diagnosis?.diagnosisCode2.isNullOrEmpty()){
                    messageAction.value= Event("请选择诊断类型")
                    return@let false
                }else if(it.diagnosis?.diagnosisCode2.equals("4-1")||it.diagnosis?.diagnosisCode2.equals("4-2")||it.diagnosis?.diagnosisCode2.equals("4-3")||it.diagnosis?.diagnosisCode2.equals("4-4")||it.diagnosis?.diagnosisCode2.equals("4-5")||it.diagnosis?.diagnosisCode2.equals("4-6")){
//                    when{
//                        it.treatStrategy?.strategyCode.isNullOrEmpty()->{
//                            messageAction.value= Event("请选择治疗策略")
//                            return@let false
//                        }
//                        else->
//                            return@let true
//                    }
                    return@let true
                } else{
                    return@let true
                }

            }?:false

        }

}