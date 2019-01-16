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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class ProfileViewModel @Inject constructor(
    private val patientApi: PatientApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter {

    var preHos=true
    override val title: String
        get() = if(preHos)"基本信息" else "病人信息"
    override val clickableTitle: String
        get() = if(preHos)"保存" else ""

    var taskId=""
    var patientId=""
        set(value) {
            field=value
            loadPatient()
        }
    val patient:LiveData<Patient>

    val bitmaps= mutableListOf<String>()

    override val clickable:LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }
    private val loadPatientResult =MediatorLiveData<Resource<Response<Patient>>>()
    val savePatientResult =MediatorLiveData<Resource<Response<String>>>()

    init {

        clickable=clickResult.map { it }
        patient=loadPatientResult.map {
            (it as? Resource.Success)?.data?.result?:Patient("")
        }


    }

    private fun loadPatient(){

        if(patientId.isNullOrEmpty()){
            loadPatientResult.value=Resource.Success(Response<Patient>(true).apply {
                this.result= Patient("")
            })
        }else {
            patientApi.getOne(patientId).toResource().subscribe { it ->
                loadPatientResult.value = it
            }
        }
    }

    fun patientIsUnkown(){
        patient.value?.let {
            it.unKnown=!it.unKnown
        }
    }

    override fun click(){
        if(preHos && patientSavable) {
            val files = bitmaps.map {
                val file = File(it)
                return@map MultipartBody.Part.createFormData(
                    "images",
                    it.split("/").last(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                )
            }
            if (files.isNullOrEmpty()){
                patientApi.save(patient.value!!.apply {
                    createdBy = account.id
                    hospitalId = account.hospitalId
                    taskId=this@ProfileViewModel.taskId
                }).toResource().subscribe {

                    when (it) {
                        is Resource.Success -> {
                            clickResult.value = true
                            savePatientResult.value = it
                            messageAction.value = Event("保存成功")
                        }
                        is Resource.Error -> {
                            clickResult.value = true
                            messageAction.value = Event(it.throwable.message ?: "")
                        }
                        else -> {
                            clickResult.value = false
                        }
                    }
                }
            }else{
                    patientApi.save(patient.value!!.apply {
                        createdBy = account.id
                        hospitalId = account.hospitalId
                        taskId=this@ProfileViewModel.taskId
                    }, files).toResource().subscribe {
                        when (it) {
                            is Resource.Success -> {
                                clickResult.value = true
                                savePatientResult.value = it
                                messageAction.value = Event("保存成功")
                            }
                            is Resource.Error -> {
                                clickResult.value = true
                                messageAction.value = Event(it.throwable.message ?: "")
                            }
                            else -> {
                                clickResult.value = false
                            }
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