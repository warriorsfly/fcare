package com.wxsoft.fcare.ui.patient

import android.annotation.SuppressLint
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
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class ProfileViewModel @Inject constructor(
    private val patientApi: PatientApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

    var preHos=true

    var taskId:String=""
    var patientId=""
        set(value) {
            field=value
            loadPatient()
        }
    val patient:LiveData<Patient>
    val uploading:LiveData<Boolean>

    val bitmaps= mutableListOf<String>()

    private val loadPatientResult =MediatorLiveData<Resource<Response<Patient>>>()
    val savePatientResult =MediatorLiveData<Resource<Response<String>>>()

    val shareClick:LiveData<String>
    private val initShareClick = MediatorLiveData<String>()

    init {
        shareClick = initShareClick.map { it }
        uploading=savePatientResult.map { it==Resource.Loading }
        patient=loadPatientResult.map {
            (it as? Resource.Success)?.data?.result.apply {
                if (this?.diagnosisName.equals("代码不存在"))this?.diagnosisName = ""
            }?:Patient("")
        }


    }

    fun changeCode(dCode:String){
        if(patient.value?.id!!.isEmpty()) {
            patient.value?.diagnosisCode=dCode
        }
    }

    @SuppressLint("CheckResult")
    private fun loadPatient(){

        if(patientId.isEmpty()){
            loadPatientResult.value=Resource.Success(Response<Patient>(true).apply {
                this.result= Patient("")
            })
        }else {
            patientApi.getOne(patientId).toResource().subscribe { inf ->
                loadPatientResult.value = inf
            }
        }
    }

//    fun patientIsUnkown(){
//        patient.value?.let {
//            it.unKnown=!it.unKnown
//        }
//    }

    fun save(){
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
                    if(this@ProfileViewModel.taskId.isNotEmpty()) {
                        taskId = this@ProfileViewModel.taskId
                    }
                }).toResource().subscribe {
                    savePatientResult.value = it
                    when (it) {
                        is Resource.Success -> {
                            initShareClick.value = "saveSuccess"
                            messageAction.value = Event("保存成功")
                        }
                        is Resource.Error -> {
                            initShareClick.value = "saveSuccess"
                            messageAction.value = Event(it.throwable.message ?: "")
                        }

                    }
                }
            }else{
                patientApi.save(patient.value!!.apply {
                    createdBy = account.id
                    hospitalId = account.hospitalId
                    if(this@ProfileViewModel.taskId.isNotEmpty()) {
                        taskId = this@ProfileViewModel.taskId
                    }
                }, files).toResource().subscribe {

                    savePatientResult.value = it
                    when (it) {
                        is Resource.Success -> {
                            initShareClick.value = "saveSuccess"
                            messageAction.value = Event("保存成功")
                        }
                        is Resource.Error -> {
                            initShareClick.value = "saveSuccess"
                            messageAction.value = Event(it.throwable.message ?: "")
                        }

                    }
                }
            }
        }
    }

    fun click(){
        save()
//        initShareClick.value = "share"
    }

    val patientSavable:Boolean
        get(){
            return patient.value?.let {

                if(it.unKnown){
                    if(it.diagnosisCode.isEmpty()){
                        messageAction.value= Event("大病分类需要选择")
                        return@let false
                    }else {
                        return@let true
                    }
                }else{
                    when{
                        it.name.isEmpty()->{
                            messageAction.value= Event("姓名不能为空")
                            return@let false
                        }

                        it.diagnosisCode.isEmpty() ->{
                            messageAction.value= Event("大病分类需要选择")
                            return@let false
                        }
                        else->
                            return@let true
                    }
                }

            }?:false

    }
}