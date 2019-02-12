package com.wxsoft.fcare.ui.details.ct

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Pacs
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.map
import javax.inject.Inject

class CTViewModel @Inject constructor(private val api: PACSApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title: String=""
        get() = "CT室操作"
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
            loadPacs()
        }

    val intervention:LiveData<Pacs>
    private val loadInterventionResult = MediatorLiveData<Response<Pacs>>()
    val commitResult = MediatorLiveData<Resource<Response<String>>>()

    init {
        clickable = clickResult.map { it }
        intervention = loadInterventionResult.map { it?.result ?: Pacs()  }
    }

    private fun loadPacs(){
        disposable.add(api.getPAC(patientId)
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        loadInterventionResult.value=it?.data
                    }
                }
            })

    }



    override fun click() {
        intervention.value?.let {
            if (it.id.isEmpty()) {
                it.createrId = account.id
                it.createrName = account.trueName
                it.patientId = patientId
            }

            disposable.add(api.savePAC(it).toResource()
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