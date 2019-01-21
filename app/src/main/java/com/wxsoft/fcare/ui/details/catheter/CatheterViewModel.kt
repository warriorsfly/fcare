package com.wxsoft.fcare.ui.details.catheter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.chest.Intervention
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.InterventionApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import java.lang.Error
import javax.inject.Inject

class CatheterViewModel @Inject constructor(private val interventionApi: InterventionApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override val title: String
        get() = "导管室操作"
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
            loadIntervention()
        }

    val intervention:LiveData<Intervention>
    private val loadInterventionResult = MediatorLiveData<Resource<Response<Intervention>>>()
    val commitInterventionResult = MediatorLiveData<Resource<Response<String>>>()

    init {
        clickable = clickResult.map { it }
        intervention = loadInterventionResult.map { (it as? Resource.Success)?.data?.result ?: Intervention("")  }
    }

    fun loadIntervention(){
        interventionApi.getIntervention(patientId).toResource()
            .subscribe {
                loadInterventionResult.value = it
            }
    }



    override fun click() {
        intervention.value?.let {
            interventionApi.save(it).toResource()
                .subscribe {result->
                    when(result){
                        is Resource.Success->{
                            commitInterventionResult.value=result
                            messageAction.value= Event("保存成功")
                        }
                        is Error->{
                            messageAction.value=Event(result.message?:"")
                        }
                    }
                }
        }

    }

}