package com.wxsoft.fcare.ui.patient

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import java.lang.Error
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val patientApi: PatientApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gson: Gson
) : BaseViewModel(sharedPreferenceStorage,gson), ICommonPresenter {
    override val title: String
        get() = "基本信息"
    override val clickableTitle: String
        get() = "保存"

    var taskId=""
    val patient:LiveData<Patient>

    override val clickable:LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }
    private val loadPatientResult =MediatorLiveData<Resource<Response<Patient>>>()
    private val savePatientResult =MediatorLiveData<Resource<Response<String>>>()

    init {

        clickable=clickResult.map { it }
        patient=loadPatientResult.map {
            (it as? Resource.Success)?.data?.result?:Patient("")
        }

        loadPatientResult.value=Resource.Success(Response<Patient>(true).apply {
            this.result= Patient("")
        })
    }

    fun patientIsUnkown(){
        patient.value?.let {
            it.unKnown=!it.unKnown
        }
    }

    override fun click(){
        if(patientSavable){
            //在上面patientSavable已经判定了patient.value非空才会执行到这一步
            patientApi.save(patient.value!!.apply {
                taskId=this@ProfileViewModel.taskId
                createdBy=account.id
            }).toResource().subscribe {
                when (it) {
                    is Resource.Success -> {
                        clickResult.value=true
                        savePatientResult.value = it
                        messageAction.value = Event("保存成功")
                    }
                    is Resource.Error -> {
                        clickResult.value=true
                        messageAction.value = Event(it.throwable.message ?: "")
                    }
                    else->{
                        clickResult.value=false
                    }
                }
            }
        }
    }

    private val patientSavable:Boolean
        get(){
            return patient.value?.let {

                if(it.unKnown){
                    return@let true
                }else{
                    when{
                        it.name.isNullOrEmpty()->{
                            messageAction.value= Event("姓名不能为空")
                            return@let false
                        }

                        it.idcard.isNullOrEmpty()->{
                            messageAction.value= Event("身份证不能为空")
                            return@let false
                        }

                        it.age==0->{
                            messageAction.value= Event("年龄不能为空")
                            return@let false
                        }

                        else->
                            return@let true
                    }
                }

            }?:false

    }
}