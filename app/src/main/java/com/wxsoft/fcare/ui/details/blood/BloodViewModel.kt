package com.wxsoft.fcare.ui.details.blood

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Blood
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class BloodViewModel @Inject constructor(private val api: PACSApi,
                                         private val patientApi: PatientApi,
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
            loadPacs()
            loadPatient()
        }

    val blood:LiveData<Blood>
    val patient:LiveData<Patient>

    private val loadBloodResult = MediatorLiveData<Response<Blood>>()
    private val loadPatientResult = MediatorLiveData<Response<Patient>>()
    val commitResult = MediatorLiveData<Resource<Response<String>>>()

    init {
        blood = loadBloodResult.map { it?.result ?: Blood(patientId=patientId).apply {
            createrId=account.id
        }  }
        patient = loadPatientResult.map { it?.result ?: Patient()  }
    }

    private fun loadPacs(){
        disposable.add(api.getBlood(patientId)
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        loadBloodResult.value= it.data
                    }
                }
            })

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



    fun click() {
        blood.value?.let {
            if (it.id.isEmpty()) {
                it.createrId = account.id
                it.createrName = account.trueName
                it.patientId = patientId
            }

            disposable.add(api.saveBlood(it).toResource()
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