package com.wxsoft.fcare.ui.details.vitalsigns

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableInt
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
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class VitalSignsViewModel @Inject constructor(private val vitalSignApi: VitalSignApi, private val dictEnumApi: DictEnumApi,
                                              override val sharedPreferenceStorage: SharedPreferenceStorage,
                                              override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override val title: String
        get() = "生命体征信息"
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


    init {
        clickable=clickResult.map { it }
        vital = loadVitalResult.map { (it as? Resource.Success)?.data ?: VitalSign("") }
        consciousnessItems = loadConsciousnessResult.map {
            val cos=(it as? Resource.Success)?.data?: emptyList()
            consciousItems=cos
            return@map consciousItems.map { item -> item.itemName }
        }
    }

    fun saveVitalSign() {

        val v = vital.value!!
        if (v.id.isEmpty())
            v.patientId = patientId
        if (
//            v.blood_Pressure.isEmpty() ||
//            v.body_Temperature==0f||
            v.heart_Rate == 0
        ) return
        if (selectedConsciousnessPosition.get() == -1) return
        v.consciousness_Type = consciousItems.get(selectedConsciousnessPosition.get()).itemCode
        (if (v.id.isEmpty()) vitalSignApi.insert(v) else vitalSignApi.update(v)).toResource()
            .subscribe({

                messageAction.value = Event( "保存成功")},
                {error->messageAction.value= Event(error.message?:"") })
    }

    fun loadVitalSign() {
        dictEnumApi.loadConsciousness().toResource()
            .doOnSuccess {  loadConsciousnessResult.value = it}
            .flatMap {vitalSignApi.list(patientId).toResource()  }
            .subscribe {vi->

                when (vi) {
                    is Resource.Success -> {
                        val l = (vi as? Resource.Success)?.data ?: emptyList()

                        if (l.isEmpty()) {
                            loadVitalResult.value = Resource.Success(VitalSign(""))
                        } else {
                            loadVitalResult.value = Resource.Success(l[0])
                            vital.value?.setUpChecked()
                            selectedConsciousnessPosition.set(consciousnessItems.value?.indexOf(l[0].consciousness_Type)?:-1)
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
            }
    }

    override fun click(){
        saveVitalSign()
    }

}