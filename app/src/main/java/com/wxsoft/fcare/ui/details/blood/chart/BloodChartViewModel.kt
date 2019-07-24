package com.wxsoft.fcare.ui.details.blood.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class BloodChartViewModel @Inject constructor(private val api: PACSApi,
                                              override val sharedPreferenceStorage: SharedPreferenceStorage,
                                              override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon)  {

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadSigns()
            loadExcutePlan()
        }
    val vitalSigns: LiveData<List<VitalSignsCollectResult>>
    private val loadVitalSignsResult = MediatorLiveData<Response<List<VitalSignsCollectResult>>>()

    val excutePlans: LiveData<VitalSignsCollectExcutePlan>
    private val loadExcutePlans = MediatorLiveData<Response<VitalSignsCollectExcutePlan>>()

    init {
        vitalSigns = loadVitalSignsResult.map { it?.result ?: emptyList() }
        excutePlans = loadExcutePlans.map { it?.result ?: VitalSignsCollectExcutePlan("",patientId,"","","",account.id,0, emptyList()) }
    }

    private fun loadSigns(){
        disposable.add(api.getVitalSigns(patientId)
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        loadVitalSignsResult.value= it.data
                    }
                }
            })

    }
    private fun loadExcutePlan(){
        disposable.add(api.getExcutePlan(patientId)
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        loadExcutePlans.value= it.data
                    }
                }
            })
    }

}