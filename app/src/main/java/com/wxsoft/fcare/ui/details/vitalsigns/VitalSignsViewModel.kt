package com.wxsoft.fcare.ui.details.vitalsigns

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.VitalSign
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.VitalSignApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class VitalSignsViewModel @Inject constructor(private val vitalSignApi: VitalSignApi, private val dictEnumApi: DictEnumApi,
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

    }

    private fun saveVitalSign() {

        val v = vital.value!!
        v.sceneType = sceneTypeId
        if (v.id.isEmpty())
            v.patientId = patientId
        if (
//            v.blood_Pressure.isEmpty() ||
//            v.body_Temperature==0f||
            v.heart_Rate == 0
        ) return
        v.createrName = account.trueName
        disposable.add((if (v.id.isEmpty()) vitalSignApi.insert(v) else vitalSignApi.update(v)).toResource()
            .subscribe({
                initbackToLast.value = true
                messageAction.value = Event( "保存成功")},
                {error->messageAction.value= Event(error.message?:"") }))
    }

    fun loadVitalSign() {
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
                .flatMap { vitalSignApi.getOne(id).toResource() }
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

}