package com.wxsoft.fcare.ui.details.measures

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.databinding.ObservableInt
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Measure
import com.wxsoft.fcare.core.data.entity.MeasureDic
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.MeasuresApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class MeasuresViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                            private val measuresApi: MeasuresApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter {

    override val title: String
        get() = "治疗措施"
    override val clickableTitle: String
        get() = "保存"

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    override val clickable:LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }

    val pharmacy:LiveData<Dictionary>
    val loadPharmacy = MediatorLiveData<Dictionary>()

    val measuresItems: LiveData<List<Dictionary>>
    private val loadMeasuresItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    val departments:LiveData<List<Dictionary>>
    private val loaddetourItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val cureResultItems: LiveData<List<Dictionary>>
    private val loadCureResultItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val outcallResultItems: LiveData<List<Dictionary>>
    private val loadOutcallResultItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val measure:LiveData<Measure>
    private val loadMeasureResult = MediatorLiveData<Resource<Response<Measure>>>()

    var resultString: LiveData<String>
    private val initResultString= MediatorLiveData<Resource<Response<String>>>()


    init {
        clickable=clickResult.map { it }
        pharmacy = loadPharmacy.map { it }
        resultString = initResultString.map { (it as? Resource.Success)?.data?.result ?: ""}
        departments = loaddetourItemsResult.map {(it as? Resource.Success)?.data?: emptyList()}
        measuresItems = loadMeasuresItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        cureResultItems = loadCureResultItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        outcallResultItems = loadOutcallResultItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }

        measure = loadMeasureResult.map { (it as? Resource.Success)?.data?.result?: Measure(emptyList(),"","","") }

        getMeasuresItems()
        getCureResultItems()
        getOutcallResultItems()
        getDetourItems()

    }



    fun getMeasuresItems(){
        dicEnumApi.loadMeasuresItems().toResource()
            .subscribe {
                loadMeasuresItemsResult.value = it
            }
    }

    fun getDetourItems(){
        dicEnumApi.loadDetour().toResource()
            .subscribe {
                loaddetourItemsResult.value = it
                departments.value?.filter { it.itemCode.equals(measure.value?.preDirectDepartId) }?.map {it.checked = true }
            }
    }

    fun getCureResultItems(){
        dicEnumApi.loadCureResultItems().toResource()
            .subscribe {
                loadCureResultItemsResult.value = it
                cureResultItems.value?.filter { it.itemCode.equals(measure.value?.preCureResultCode) }?.map {it.checked = true }
            }
    }

    fun getOutcallResultItems(){
        dicEnumApi.loadOutcallResultItems().toResource()
            .subscribe {
                loadOutcallResultItemsResult.value = it
                outcallResultItems.value?.filter { it.itemCode.equals(measure.value?.preVisitResultCode) }?.map {it.checked = true }
            }
    }

    fun loadMeasure(){
        measuresApi.loadMeasure(patientId).toResource()
            .subscribe {
                loadMeasureResult.value = it
                haveData()
            }
    }
    fun saveMeasure(){
        measuresApi.save(measure.value!!).toResource()
            .subscribe{
                initResultString.value = it
            }
    }

    fun haveData(){
        measure.value?.measures?.map {
            var code = it.measureCode
            measuresItems.value?.filter { it.itemCode.equals(code) }?.map {it.checked = true }
        }

        cureResultItems.value?.filter { it.itemCode.equals(measure.value?.preCureResultCode) }?.map {it.checked = true }
        outcallResultItems.value?.filter { it.itemCode.equals(measure.value?.preVisitResultCode) }?.map {it.checked = true }
        departments.value?.filter { it.itemCode.equals(measure.value?.preDirectDepartId) }?.map {it.checked = true }
    }

    fun clickSelect(item: Dictionary){
        when(item.section){
            0->{
                if (item.itemCode.equals("5") ){//用药 跳转用药界面
                    loadPharmacy.value = item
                }else{
                    item.checked = !item.checked
                }
            }
            1->{ cureResultItems.value?.filter { it.checked }?.map {it.checked = false }
                item.checked = !item.checked}
            2->{ outcallResultItems.value?.filter { it.checked }?.map {it.checked = false }
                item.checked = !item.checked}
            3->{ departments.value?.filter { it.checked }?.map {it.checked = false }
                item.checked = !item.checked}

        }

    }


    override fun click(){

        val ds=measuresItems.value?.filter { it.checked }
            ?.map { MeasureDic("",patientId,it.itemCode) }?: emptyList()
        measure.value?.measures = ds.toList()

        cureResultItems.value?.filter { it.checked }
            ?.map { measure.value?.preCureResultCode =  it.itemCode }

        outcallResultItems.value?.filter { it.checked }
            ?.map { measure.value?.preVisitResultCode =  it.itemCode }
        departments.value?.filter { it.checked }
            ?.map { measure.value?.preDirectDepartId =  it.itemCode }

        saveMeasure()
    }


}