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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    var canSaveAble=true

    var taskId:String=""
    var patientId=""
        set(value) {
            field=value
            loadPatient()
        }
    val patient:LiveData<Patient>
    val uploading:LiveData<Boolean>

    val bitmaps= mutableListOf<String>()

    val loadPatientResult =MediatorLiveData<Resource<Response<Patient>>>()
    val savePatientResult =MediatorLiveData<Resource<Response<String>>>()

    val shareClick:LiveData<String>
    private val initShareClick = MediatorLiveData<String>()
    val startFasting = MediatorLiveData<Boolean>()



    init {
        shareClick = initShareClick.map { it }
        uploading=savePatientResult.map { it==Resource.Loading }
        patient=loadPatientResult.map {
            (it as? Resource.Success)?.data?.result.apply {
                if (this?.diagnosisName.equals("代码不存在"))this?.diagnosisName = ""
            }?:Patient("").apply { createrId=account.id }
        }

    }

    fun changeCode(dCode:String){
        if(patient.value?.id!!.isEmpty()) {
            patient.value?.diagnosisCode=dCode
        }
        if(dCode=="215-2"){
            startFasting.value=true
        }
    }

    @SuppressLint("CheckResult")
    private fun loadPatient(){
        if(patientId.isEmpty()){
            loadPatientResult.value=Resource.Success(Response<Patient>(true).apply {
                this.result= Patient("").apply { createrId=account.id }
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
        if(preHos && patientSavable && canSaveAble) {
            canSaveAble = false
            patientApi.save(patient.value!!.apply {
                hospitalId = account.hospitalId
                if(createrId.isNullOrEmpty())
                    createrId = account.id
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
        }
    }

    fun savePic(fs:List<File>){
        if(preHos && patientSavable && canSaveAble) {
            canSaveAble = false
            val files = fs.map {
                return@map MultipartBody.Part.createFormData(
                    "images",
                    it.path.split("/").last(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), it)
                )
            }

            patientApi.save(patient.value!!.apply {
                hospitalId = account.hospitalId
                if(createrId.isNullOrEmpty())
                    createrId = account.id
                if (this@ProfileViewModel.taskId.isNotEmpty()) {
                    taskId = this@ProfileViewModel.taskId
                }
            }, files).toResource().subscribe {
                when (it) {
                    is Resource.Success -> {
                        loadPatient()
                    }
                    is Resource.Error -> {
                        messageAction.value = Event(it.throwable.message ?: "")
                    }

                }
                canSaveAble = true
            }
        }
    }

    val patientSavable:Boolean
        get(){
            return patient.value?.let {

                if(it.unKnown){
                    if(it.diagnosisCode.isEmpty()){
                        messageAction.value= Event("大病分类需要选择")
                        return@let false
                    }else if(it.attackTime.isNullOrEmpty()){
                        messageAction.value= Event("发病时间需要选择")
                        return@let false
                    } else {
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
                        it.attackTime.isNullOrEmpty() ->{
                            messageAction.value= Event("发病时间需要选择")
                            return@let false
                        }
                        it.diagnosisCode=="215-2" && it.phone.isNullOrEmpty()->{
                            messageAction.value= Event("联系方式不能为空")
                            return@let false
                        }

                        it.diagnosisCode=="215-2" && it.height==null->{
                            messageAction.value= Event("身高不能为空")
                            return@let false
                        }
                        else->
                            return@let true
                    }
                }

            }?:false

    }


    fun getPatientInfos(blh:String,type:Int){
        if (blh.isNullOrEmpty()) return
        disposable.add(
            patientApi.getPatientInfo(blh,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                    val p = it.result
                    if (p!=null){
                        loadPatientResult.value = Resource.Success(Response<Patient>(true).apply {
                            this.result= patient.value?.apply {
                                if (!p.idcard.isNullOrEmpty()) idcard = p.idcard
                                if (!p.name.isNullOrEmpty()) name = p.name
                                gender = p.gender
                                age = p.age
                                if (!p.phone.isNullOrEmpty()) phone = p.phone
//                                if (!p.outpatientId.isNullOrEmpty()) outpatientId = p.outpatientId
                                if (!p.inpatientId.isNullOrEmpty()) inpatientId = p.inpatientId
                            }
                        })
                        messageAction.value= Event("已获取到患者信息")
                    }
                },{

                })

        )
    }

    fun delete(url:String){

        patient.value?.let{
            for ( t in it.attachments){
                if(t.httpUrl==url){
                    disposable.add( patientApi.deleteImage(t.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(::reloadPatientDetails, ::error))
                    return@let
                }

            }
        }
    }

    private fun reloadPatientDetails(response: Response<String>?) {
        if(response?.success==true)
            loadPatient()
    }



}