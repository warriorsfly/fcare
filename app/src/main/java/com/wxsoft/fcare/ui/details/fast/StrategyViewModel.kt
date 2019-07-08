package com.wxsoft.fcare.ui.details.strategy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Strategy
import com.wxsoft.fcare.core.data.entity.Strock120
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class FastViewModel  @Inject constructor(private val api: PatientApi,
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
//            getStrock()
        }

    var id: String=""

        set(value) {
            field = value
            getStrock()
        }

    val strock: LiveData<Strock120>
    private val strocking = MediatorLiveData<Response<Strock120>>()

//        .apply {
//        value= Strock120(drugId="",createrId = account.drugId,createrName = account.trueName)
//    }

    val result: LiveData<Boolean>
     val savingResult = MediatorLiveData<Resource<Response<String>>>()

    init {
        strock = strocking.map {it.result?:Strock120(id="",createrId = account.id,createrName = account.trueName)  }
        result=savingResult.map { (it as? Resource.Success)?.data?.success?:false }
    }

    fun saveStrock(){
        strock.value?.let {
            disposable.add(api.saveStrock120(it).toResource()
                .subscribe {
                    savingResult.value = it

                })
        }

    }


    fun getStrock(){
        disposable.add(api.getStrock120(id).toResource()
            .subscribe {
                strocking.value = (it  as? Resource.Success)?.data?:Response(false)

            })
    }

}