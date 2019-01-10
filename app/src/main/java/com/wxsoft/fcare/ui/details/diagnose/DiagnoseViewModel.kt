package com.wxsoft.fcare.ui.details.diagnose

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DiagnoseApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.PharmacyApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class DiagnoseViewModel  @Inject constructor(private val diagnoseApi: DiagnoseApi,
                                             private val dictEnumApi: DictEnumApi,
                                             override val sharedPreferenceStorage: SharedPreferenceStorage,
                                             override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override val title: String
        get() = "诊断"
    override val clickableTitle: String
        get() = "确定"
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
        }

    val showSonList:LiveData<Boolean>
    private val initShowSonList = MediatorLiveData<Boolean>()

    val thoracalgiaItems: LiveData<List<Dictionary>>
    private val loadthoracalgiaItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val typeItems: LiveData<List<Dictionary>>
    private val loadTypeItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val sonItems: LiveData<List<Dictionary>>
    private val loadsonItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadnotACSItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadOtherXTItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()



    init {

        showSonList = initShowSonList.map { it }

        clickable = clickResult.map { it }
        typeItems = loadTypeItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        thoracalgiaItems = loadthoracalgiaItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        sonItems = loadsonItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }

        loadDiagnoseType()
        loadThoracalgias()
        loadNotACS()
        loadOtherXT()
    }

    fun loadDiagnoseType(){
        dictEnumApi.loadDiagnoseTypeResultItems().toResource()
            .subscribe{
                loadTypeItemsResult.value = it
            }
    }

    fun loadThoracalgias(){
        dictEnumApi.loadDict4Diagnosis().toResource()
            .subscribe{
                loadthoracalgiaItemsResult.value = it
            }
    }

    fun loadNotACS(){
        dictEnumApi.loadDict27Diagnosis().toResource()
            .subscribe{
                loadnotACSItemsResult.value = it
            }
    }

    fun loadOtherXT(){
        dictEnumApi.loadConsciousness().toResource()
            .subscribe{
                loadOtherXTItemsResult.value = it
            }
    }


    override fun click(){

    }

    fun selected(item:Dictionary){
        typeItems.value?.filter { it.checked }
            ?.map { it.checked = false }
        item.checked = true
    }

    fun selectDiagnose(item:Dictionary){
        thoracalgiaItems.value?.filter { it.checked }
            ?.map { it.checked = false }
        item.checked = true
        when(item.itemName){
            "非ACS胸痛" -> {
                initShowSonList.value = true
                loadsonItemsResult.value = loadnotACSItemsResult.value
            }
            "非心源性胸痛" -> {
                initShowSonList.value = true
                loadsonItemsResult.value = loadOtherXTItemsResult.value
            }
            else ->{
                initShowSonList.value = false
            }
        }
    }

    fun selectDiagnoseSon(item:Dictionary){
        sonItems.value?.filter { it.checked }
            ?.map { it.checked = false }
        item.checked = true

    }
}