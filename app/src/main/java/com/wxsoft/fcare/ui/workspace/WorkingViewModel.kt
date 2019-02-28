package com.wxsoft.fcare.ui.workspace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.TimeQuality
import com.wxsoft.fcare.core.data.entity.WorkOperation
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.remote.QualityControlApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class WorkingViewModel @Inject constructor(private val patientApi: PatientApi,
                                           private val qualityControlApi: QualityControlApi,
                                           @Named("WorkOperationIcon")
                                           private val icons:Array<Int>,
                                           @Named("WorkOperationTint")
                                           private val tints:IntArray,
                                           @Named("WorkOperationKey")
                                           private val keys:Array<String>,
                                           override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    var patientId:String=""
    set(value) {
        field=value
        loadPatient(field)
        loadQualities(field)
        loadOperations(field,account.id)
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
    }

    val operations:LiveData<List<WorkOperation>>
    private val loadOperationResult=MediatorLiveData<Response<List<WorkOperation>>>()

    init {
        operations=loadOperationResult.map { it.result?: emptyList() }
    }


    private fun loadPatient(id:String){
        disposable.add(patientApi.getOne(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                loadPatientResult.value=it
            },{
            messageAction.value= Event(it.message?:"未知错误")
        }))
    }

    private fun loadQualities(id:String){
        disposable.add(qualityControlApi.getQualities(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                loadTimeQualityResult.value=it
            },{
                messageAction.value= Event(it.message?:"未知错误")
            }))
    }


    private fun loadOperations(id:String,userId:String){
        disposable.add(qualityControlApi.getOperations(id,userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                it.result?.forEach { operation->
                    operation.apply {
                        val index=keys.indexOf(actionCode)
                        if(index>=0){
                            tint=tints[index]
                            ico=icons[index]
                        }
                    }

                }
                loadOperationResult.value=it
            },{
                messageAction.value= Event(it.message?:"未知错误")
            }))
    }

}
