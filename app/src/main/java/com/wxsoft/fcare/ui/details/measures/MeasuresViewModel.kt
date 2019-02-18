package com.wxsoft.fcare.ui.details.measures

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
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
import com.wxsoft.fcare.core.utils.map
import javax.inject.Inject

class MeasuresViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                            private val measuresApi: MeasuresApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter {

    override var title = "治疗措施"
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
    private val loadPharmacy = MediatorLiveData<Dictionary>()

    val changeString:LiveData<String>
    private val loadChangeString = MediatorLiveData<String>()

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
        changeString = loadChangeString.map { it }
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



    private fun getMeasuresItems(){
        dicEnumApi.loadMeasuresItems().toResource()
            .subscribe {
                loadMeasuresItemsResult.value = it
                haveData()
            }
    }

    private fun getDetourItems(){
        dicEnumApi.loadDetour().toResource()
            .subscribe {
                loaddetourItemsResult.value = it
                haveData()
            }
    }

    private fun getCureResultItems(){
        dicEnumApi.loadCureResultItems().toResource()
            .subscribe {
                loadCureResultItemsResult.value = it
                haveData()
            }
    }

    private fun getOutcallResultItems(){
        dicEnumApi.loadOutcallResultItems().toResource()
            .subscribe {
                loadOutcallResultItemsResult.value = it
                haveData()
            }
    }

    fun loadMeasure(){
        measuresApi.loadMeasure(patientId).toResource()
            .subscribe {
                loadMeasureResult.value = it
                haveData()
            }
    }
    private fun saveMeasure(){
        measuresApi.save(measure.value!!).toResource()
            .subscribe{
                initResultString.value = it
            }
    }

    private fun haveData(){
        measure.value?.measureDtos?.map {
            val code = it.measureCode
            measuresItems.value?.filter { it.id == code }?.map {it.checked = true }
        }

        cureResultItems.value?.filter { it.id == measure.value?.preCureResultCode }?.map {it.checked = true }
        outcallResultItems.value?.filter { it.id == measure.value?.preVisitResultCode }?.map {it.checked = true }
        departments.value?.filter { it.id == measure.value?.preDirectDepartId }?.map {it.checked = true }
    }

    fun clickSelect(item: Dictionary){
        when(item.section){
            0->{
                when {
                    item.id == "212-5" -> //用药 跳转用药界面
                        loadPharmacy.value = item
                    item.id == "212-6" -> //溶栓 跳转溶栓界面
                        loadPharmacy.value = item
                    else -> item.checked = !item.checked
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
            ?.map { MeasureDic("",patientId,it.id) }?: emptyList()
        if (ds.isEmpty()){
            loadChangeString.value = "请选择治疗措施"
            return
        }
        measure.value?.measureDtos = ds.toList()

        cureResultItems.value?.filter { it.checked }
            ?.map { measure.value?.preCureResultCode =  it.id }

        outcallResultItems.value?.filter { it.checked }
            ?.map { measure.value?.preVisitResultCode =  it.id }
        departments.value?.filter { it.checked }
            ?.map { measure.value?.preDirectDepartId =  it.id }

        saveMeasure()
    }


}