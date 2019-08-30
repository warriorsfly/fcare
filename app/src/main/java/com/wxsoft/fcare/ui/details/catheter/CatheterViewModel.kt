package com.wxsoft.fcare.ui.details.catheter

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.entity.chest.Intervention
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.InterventionApi
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.remote.ThrombolysisApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.rxkotlin.zipWith
import javax.inject.Inject

class CatheterViewModel @Inject constructor(private val interventionApi: InterventionApi,
                                            private val patientApi: PatientApi,
                                            private val api: PACSApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {


    var xtShow= ObservableField<Boolean>()
    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadPatient()
            loadIntervention()
            getStrategy()
            getInformedConsent()
        }

    val patient:LiveData<Patient>
    private val loadPatientResult = MediatorLiveData<Response<Patient>>()

    val strategy: LiveData<Strategy>
    private val loadStrategy = MediatorLiveData<Resource<Response<Strategy>>>()

    var typeId= ObservableField<String>()

    var num1= ObservableField<String>()
    var num2= ObservableField<String>()

    val intervention:LiveData<Intervention>
    var docs:List<User> = emptyList()
    private val loadInterventionResult = MediatorLiveData<Response<Intervention>>()
    val commitResult = MediatorLiveData<Resource<Response<String>>>()
    val modifySome:LiveData<String>
    private val initModifySome =  MediatorLiveData<String>()

    val clickLine:LiveData<String>
    private val loadClickLine =  MediatorLiveData<String>()

    val informed:LiveData<InformedConsent>
    private val loadInformedResult = MediatorLiveData<Response<InformedConsent>>()

    init {
        strategy = loadStrategy.map { (it as? Resource.Success)?.data?.result?: Strategy(patientId,1) }
        modifySome = initModifySome.map { it }
        intervention = loadInterventionResult.map { it?.result ?: Intervention("",createrId = account.id)  }
        patient = loadPatientResult.map { it?.result ?: Patient()  }
        clickLine = loadClickLine.map { it }
        informed = loadInformedResult.map { (it?.result ?: InformedConsent("")) }


    }

    //获取溶栓知情同意书内容
    private fun getInformedConsent(){
        disposable.add(interventionApi.getInformedConsentById("1")
            .toResource()
            .subscribe ({
                when(it){
                    is Resource.Success->{
                        loadInformedResult.value = it.data
                    }
                }
            },::error))
    }

    private fun loadPatient(){
        disposable.add(patientApi.getOne(patientId)
            .toResource()
            .subscribe ({
                when(it){
                    is Resource.Success->{
                        loadPatientResult.value= it.data
                    }
                }
            },::error))

    }

    fun getStrategy(){
        api.getStrategy(patientId).toResource()
            .subscribe {
                    result ->
                when (result) {
                    is Resource.Success -> {
                        loadStrategy.value = result
                        typeId.set(result.data.result?.strategyCode?:"")
                        num1.set(result.data.result?.preoperative_Timi_Level.toString())
                        num2.set(result.data.result?.postoperative_Timi_Level.toString())
                    }
                    is Error -> {
                        messageAction.value = Event(result.message ?: "")
                    }
                }
            }
    }


    fun seletedNum1(id:String){
        num1.set(id)
        strategy.value?.preoperative_Timi_Level = id.toInt()
    }
    fun seletedNum2(id:String){
        num2.set(id)
        strategy.value?.postoperative_Timi_Level = id.toInt()
    }
    private fun loadIntervention(){
        disposable.add(interventionApi.getIntervention(patientId).zipWith(interventionApi.getInterventionDocs(account.id,patientId))
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        loadInterventionResult.value=it.data.first
                        docs=it.data.second.result?: emptyList()
                        intervention.value?.setUpChecked()
                    }
                }
            })

    }

    fun clickSelectTime(location:String){
        initModifySome.value= location
    }

    //去知情同意书
    fun toInformedConsent(){
        if (informed.value != null){
            loadClickLine.value = "informedConsent"
        }
    }


    fun saving() {
        intervention.value?.let {
            if (it.id.isEmpty()) {
                it.doctorId = account.id
                it.doctorName = account.trueName
                it.patientId = patientId
            }

            disposable.add(interventionApi.save(it)
                .flatMap {
                    api.saveStrategy(strategy.value!!)
                }
                .toResource()
                .subscribe { result ->
                    when (result) {
                        is Resource.Success -> {
                            commitResult.value = result
                            messageAction.value = Event("保存成功")
                        }
                        is Error -> {
                            messageAction.value = Event(result.message ?: "")
                        }
                    }
                })
        }

    }

}