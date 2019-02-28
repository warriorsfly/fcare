package com.wxsoft.fcare.ui.workspace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.TimeQuality
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WorkingViewModel @Inject constructor(private val patientApi: PatientApi,
                                           override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    var patientId:String=""
    set(value) {
        field=value
        loadPatient(field)
    }
    /**
     * 病人信息
     */
    val patient:LiveData<Patient>
    private val loadPatientResult=MediatorLiveData<Response<Patient>>()

    init {
        patient=loadPatientResult.map { it.result?: Patient() }
    }

    /**
     * 质控信息
     */
    val qualities:LiveData<List<TimeQuality>>
    private val loadTimeQualityResult=MediatorLiveData<Response<List<TimeQuality>>>()

    init {
        qualities=loadTimeQualityResult.map {it.result?: emptyList()}
        loadTimeQualityResult.value= Response(true)
        loadTimeQualityResult.value?.result=listOf(TimeQuality("NOT",128,true),
            TimeQuality("DNT",40),
            TimeQuality("COT",89),
            TimeQuality("DRT",0),
            TimeQuality("DOT",0))

    }

    fun loadPatient(id:String){
        disposable.add(patientApi.getOne(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                loadPatientResult.value=it
            },{
            messageAction.value= Event(it.message?:"未知错误")
        }))
    }

}
