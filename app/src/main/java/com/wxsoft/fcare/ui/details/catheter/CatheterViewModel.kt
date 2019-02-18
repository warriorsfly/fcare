package com.wxsoft.fcare.ui.details.catheter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.data.entity.chest.Intervention
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.InterventionApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.map
import io.reactivex.rxkotlin.zipWith
import java.lang.Error
import javax.inject.Inject

class CatheterViewModel @Inject constructor(private val interventionApi: InterventionApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title="导管室操作"
    override val clickableTitle="保存"
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
    var docs:List<User> = emptyList()
    private val loadInterventionResult = MediatorLiveData<Response<Intervention>>()
    val commitResult = MediatorLiveData<Resource<Response<String>>>()

    init {
        clickable = clickResult.map { it }
        intervention = loadInterventionResult.map { it?.result ?: Intervention("")  }
    }

    private fun loadIntervention(){
        disposable.add(interventionApi.getIntervention(patientId).zipWith(interventionApi.getInterventionDocs(account.id))
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        loadInterventionResult.value=it.data.first
                        docs=it.data.second.result?: emptyList()
                    }
                }
            })

    }



    override fun click() {
        intervention.value?.let {
            if (it.id.isEmpty()) {
                it.doctorId = account.id
                it.doctorName = account.trueName
                it.patientId = patientId
            }

            disposable.add(interventionApi.save(it).toResource()
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