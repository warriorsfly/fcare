package com.wxsoft.fcare.ui.details.vitalsigns.records

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.VitalSign
import com.wxsoft.fcare.core.data.entity.VitalSignRecord
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.VitalSignApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import javax.inject.Inject

class VitalSignsRecordViewModel @Inject constructor(private val vitalSignApi: VitalSignApi,
                                                    override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title = "生命体征"
    override val clickableTitle: String
        get() = ""
    override val clickable: LiveData<Boolean>

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
            getVitalRecords()
        }


    val vitals:LiveData<List<VitalSignRecord>>
    private val initVitals = MediatorLiveData<Resource<Response<List<VitalSignRecord>>>>()

    val addvital:LiveData<String>
    private val initAddvital = MediatorLiveData<String>()

    val modifyvital:LiveData<String>
    private val initModifyvital = MediatorLiveData<String>()

    init {
        clickable=clickResult.map { it }
        vitals = initVitals.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        addvital = initAddvital.map { it }
        modifyvital = initModifyvital.map { it }
    }

    override fun click(){

    }

    fun getVitalRecords(){
        vitalSignApi.getVitalRecordlist(patientId).toResource()
            .subscribe{
                when (it) {
                    is Resource.Success -> {
                        initVitals.value = it
                        messageAction.value = Event("保存成功")
                    }
                    is Resource.Error -> {
                        messageAction.value = Event( "网络接口出错")
                    }
                }
            }
    }

    fun add(item:VitalSignRecord){
        initAddvital.value = item.typeId
//           when(item.typeId){
//                "223-1" ->{}//院前
//                "223-2" ->{}//初诊
//                "223-3" ->{}//诊疗前检查
//                "223-4" ->{}//治疗
//                "223-5" ->{}//转归
//           }
    }

    fun clickVital(item:VitalSign){
        initModifyvital.value = item.id
    }

}