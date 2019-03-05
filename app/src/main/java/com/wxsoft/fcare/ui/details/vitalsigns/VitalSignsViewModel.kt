package com.wxsoft.fcare.ui.details.vitalsigns

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.databinding.ObservableInt
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.VitalSign
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.VitalSignApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.map
import javax.inject.Inject

class VitalSignsViewModel @Inject constructor(private val vitalSignApi: VitalSignApi, private val dictEnumApi: DictEnumApi,
                                              override val sharedPreferenceStorage: SharedPreferenceStorage,
                                              override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title = "生命体征信息"
    override val clickableTitle: String
        get() = "保存"
    override val clickable:LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }
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
    val consciousnessItems: LiveData<List<String>>
    private lateinit var consciousItems: List<Dictionary>
    val selectedConsciousnessPosition: ObservableInt = ObservableInt(0)

    private val loadVitalResult = MediatorLiveData<Resource<VitalSign>>()
    /**
    * 意识字典
    */
    private val loadConsciousnessResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()



    init {

        backToLast = initbackToLast.map { it }

        clickable=clickResult.map { it }
        vital = loadVitalResult.map { (it as? Resource.Success)?.data ?: VitalSign("") }
        consciousnessItems = loadConsciousnessResult.map {
            val cos=(it as? Resource.Success)?.data?: emptyList()
            consciousItems=cos
            return@map consciousItems.map { item -> item.itemName }
        }
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
        if (selectedConsciousnessPosition.get() == -1) return
        v.consciousness_Type = consciousItems[selectedConsciousnessPosition.get()].id
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
                                selectedConsciousnessPosition.set(
                                    consciousnessItems.value?.indexOf(l[0].consciousness_Type) ?: -1
                                )
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
                            selectedConsciousnessPosition.set(
                                consciousnessItems.value?.indexOf(vi.data.result?.consciousness_Type) ?: -1
                            )

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

    override fun click(){
        saveVitalSign()
    }

}