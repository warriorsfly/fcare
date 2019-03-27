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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class VitalSignsRecordViewModel @Inject constructor(private val vitalSignApi: VitalSignApi,
                                                    @Named("ratingTint")
                                                    private val tints:IntArray,
                                                    override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title = "生命体征"
    override val clickableTitle: String
        get() = "分享"
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
    private val initVitals = MediatorLiveData<List<VitalSignRecord>>()

    val addvital:LiveData<String>
    private val initAddvital = MediatorLiveData<String>()

    val modifyvital:LiveData<VitalSign>
    private val initModifyvital = MediatorLiveData<VitalSign>()

    init {
        clickable=clickResult.map { it }
        vitals = initVitals.map {  it ?: emptyList() }
        addvital = initAddvital.map { it }
        modifyvital = initModifyvital.map { it }
    }

    override fun click(){
        initAddvital.value = "share"
    }

    fun getVitalRecords(){

        disposable.add(vitalSignApi.getVitalRecordlist(patientId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doScenceLoadVitalRecord))
    }

    private fun doScenceLoadVitalRecord(response: Response<List<VitalSignRecord>>){

        initVitals.value=response.result?.apply {
            forEachIndexed { index, result ->
                result.tint = tints[(index + 1) % tints.size]
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
        initModifyvital.value = item
    }

}