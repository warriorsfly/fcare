package com.wxsoft.fcare.ui.details.vitalsigns

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.VitalSign
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.remote.VitalSignApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class VitalSignsViewModel @Inject constructor(private val vitalSignApi: VitalSignApi,
                                              private val dictEnumApi: DictEnumApi,
                                              private val patientApi: PatientApi,
                                              override val sharedPreferenceStorage: SharedPreferenceStorage,
                                              override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon)  {

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    var id: String = ""
        set(value) {
            if (value == "") return
            field = value
        }
    var sceneTypeId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    val patient:LiveData<Patient>

    val loadPatientResult =MediatorLiveData<Resource<Response<Patient>>>()

    var canSaveAble=true

    var xtShow= ObservableField<Boolean>()

    private val _errorToOperationAction = MutableLiveData<Event<String>>()

    val vital: LiveData<VitalSign>
    private val loadVitalResult = MediatorLiveData<Resource<VitalSign>>()
    /**
    * 意识字典
    */
    private val loadConsciousnessResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()

    val clickConcious:LiveData<String>
    private val initClickConcious = MediatorLiveData<String>()


    init {
        clickConcious = initClickConcious.map { it }
        backToLast = initbackToLast.map { it }
        vital = loadVitalResult.map { (it as? Resource.Success)?.data ?: VitalSign("") }
        patient=loadPatientResult.map {
            (it as? Resource.Success)?.data?.result.apply {
                if (this?.diagnosisName.equals("代码不存在"))this?.diagnosisName = ""
            }?:Patient("").apply { createrId=account.id }
        }

    }

    private fun saveVitalSign() {
        if (saveable && canSaveAble) {
            canSaveAble = false
            val v = vital.value!!
            v.sceneType = sceneTypeId
            if (v.id.isEmpty()) v.patientId = patientId
            v.createrName = account.trueName
            disposable.add((if (v.id.isEmpty()) vitalSignApi.insert(v) else vitalSignApi.update(v)).toResource()
                .subscribe({
                    initbackToLast.value = true
                    messageAction.value = Event( "保存成功")},
                    {error->messageAction.value= Event(error.message?:"") }))
        }

    }

    fun loadVitalSign() {

        disposable.add(patientApi.getOne(patientId).toResource().subscribe ({ inf ->
            loadPatientResult.value = inf
        },::error))
        if(id.isEmpty()) {
            disposable.add(dictEnumApi.loadConsciousness().toResource()
                .doOnSuccess { loadConsciousnessResult.value = it }
                .flatMap { vitalSignApi.list(patientId).toResource() }
                .subscribe { vi ->

                    when (vi) {
                        is Resource.Success -> {
                            val l = (vi as? Resource.Success)?.data ?: emptyList()

                            if (l.isEmpty()) {
                                loadVitalResult.value = Resource.Success(VitalSign(""))
                            } else {
                                loadVitalResult.value = Resource.Success(l[0])
                                vital.value?.setUpChecked()
                            }

                        }
                        is Resource.Loading -> {
                            loadVitalResult.value = Resource.Loading
                        }
                        is Resource.Error -> {
                            loadVitalResult.value = vi
                            _errorToOperationAction.value = Event(vi.throwable.message ?: "错误")
                        }
                    }
                })
        }else{
            disposable.add(dictEnumApi.loadConsciousness().toResource()
                .doOnSuccess { loadConsciousnessResult.value = it }
                .flatMap {
                    vitalSignApi.getOne(id).toResource() }
                .subscribe { vi ->

                    when (vi) {
                        is Resource.Success -> {
                            loadVitalResult.value = Resource.Success(vi.data.result?: VitalSign())
                            vital.value?.setUpChecked()

                        }
                        is Resource.Loading -> {
                            loadVitalResult.value = Resource.Loading
                        }
                        is Resource.Error -> {
                            loadVitalResult.value = vi
                            _errorToOperationAction.value = Event(vi.throwable.message ?: "错误")
                        }
                    }
                })
        }
    }

    fun click(){
        saveVitalSign()
    }

    fun selectConscious(){
        initClickConcious.value = "Conscious"
    }


    val saveable:Boolean
        get(){
            return vital.value?.let {
                when{
                    it.consciousness_Type.isNullOrEmpty()->{
                        messageAction.value= Event("请选择意识")
                        return@let false
                    }

                     it.height==null->{

                         if(patient.value?.diagnosisCode=="215-2") {
                             messageAction.value = Event("身高不能为空")
                             return@let false
                         }
                         return true
                    }
//                    it.respiration_Rate==0->{
//                        messageAction.value= Event("请填写呼吸")
//                        return@let false
//                    }
//                    it.pulse_Rate==0->{
//                        messageAction.value= Event("请填写脉搏")
//                        return@let false
//                    }
//                    it.heart_Rate==0->{
//                        messageAction.value= Event("请填写心率")
//                        return@let false
//                    }
//                    it.dbp.isNullOrEmpty()->{
//                        messageAction.value= Event("请填舒张压")
//                        return@let false
//                    }
//                    it.sbp.isNullOrEmpty()->{
//                        messageAction.value= Event("请填收缩压")
//                        return@let false
//                    }
                    else->
                        return@let true
                }
            }?:false

        }

}