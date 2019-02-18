package com.wxsoft.fcare.ui.patient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
import com.wxsoft.fcare.core.utils.map
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class PatientEmrViewModel @Inject constructor(
    private val patientApi: PatientApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter {
    override var title = "基本信息"
    override val clickableTitle: String
        get() = "保存"

    var taskId=""
    var patientId=""
        set(value) {
            field=value
            loadPatient()
        }
    val patient:LiveData<Patient>

    private val bitmaps= mutableListOf<String>()

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


    }

    private fun loadPatient(){

        if(patientId.isEmpty()){
            loadPatientResult.value=Resource.Success(Response<Patient>(true).apply {
                this.result= Patient("")
            })
        }else {
            patientApi.getOne(patientId).toResource().subscribe {item->
                loadPatientResult.value = item
            }
        }
    }

    fun patientIsUnkown(){
        patient.value?.let {
            it.unKnown=!it.unKnown
        }
    }

    override fun click(){
        if(patientSavable) {
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
                    taskId=this@PatientEmrViewModel.taskId
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
                        taskId=this@PatientEmrViewModel.taskId
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
                        it.name.isEmpty()->{
                            messageAction.value= Event("姓名不能为空")
                            return@let false
                        }

                        it.idcard.isEmpty()->{
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