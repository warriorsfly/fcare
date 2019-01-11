package com.wxsoft.fcare.ui.details.diagnose

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DiagnoseApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
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

    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()

    val showSonList:LiveData<Boolean>
    private val initShowSonList = MediatorLiveData<Boolean>()

    val startConduitRoom:LiveData<Boolean>
    private val initStartConduitRoom = MediatorLiveData<Boolean>()

    val diagnosis: LiveData<Diagnosis>
    private val loadDiagnosisResult = MediatorLiveData<Resource<Response<Diagnosis>>>()

    val thoracalgiaItems: LiveData<List<Dictionary>>
    private val secItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadthoracalgiaItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadApoplexyResultItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val typeItems: LiveData<List<Dictionary>>
    private val loadTypeItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val sonItems: LiveData<List<Dictionary>>
    private val loadsonItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadnotACSItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadOtherXTItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadInfarctItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val illnessItems: LiveData<List<Dictionary>>
    private val loadIllnessItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()


    init {

        backToLast = initbackToLast.map { it }

        startConduitRoom = initStartConduitRoom.map { it }
        showSonList = initShowSonList.map { it }
        diagnosis = loadDiagnosisResult.map { (it as? Resource.Success)?.data?.result?: Diagnosis("") }


        clickable = clickResult.map { it }
        typeItems = loadTypeItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        thoracalgiaItems = secItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        sonItems = loadsonItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        illnessItems = loadIllnessItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        loadDiagnoseType()
        loadThoracalgias()
        loadNotACS()
        loadOtherXT()
        loadIllness()
        loadInfarct()
        loadApoplexyResultItems()
    }

    fun loadDiagnoseType(){
        dictEnumApi.loadDiagnoseTypeResultItems().toResource()
            .subscribe{
                loadTypeItemsResult.value = it
                havedata()
            }
    }

    fun loadThoracalgias(){
        dictEnumApi.loadDict4Diagnosis().toResource()
            .subscribe{
                loadthoracalgiaItemsResult.value = it
                havedata()
            }
    }

    fun loadNotACS(){
        dictEnumApi.loadDict27Diagnosis().toResource()
            .subscribe{
                loadnotACSItemsResult.value = it
                havedata()
            }
    }

    fun loadOtherXT(){
        dictEnumApi.loadConsciousness().toResource()
            .subscribe{
                loadOtherXTItemsResult.value = it
                havedata()
            }
    }

    fun loadInfarct(){//脑梗死
        dictEnumApi.loadInfarct().toResource()
            .subscribe{
                loadInfarctItemsResult.value = it
            }
    }

    fun loadIllness(){
        dictEnumApi.loadIllnessResultItems().toResource()
            .subscribe{
                loadIllnessItemsResult.value = it
                havedata()
            }
    }

    fun loadApoplexyResultItems(){
        dictEnumApi.loadApoplexyResultItems().toResource()
            .subscribe{
                loadApoplexyResultItemsResult.value = it
                havedata()
            }
    }

    fun getDiagnose(){
        diagnoseApi.getDiagnosis(patientId,1).toResource()
            .subscribe{
                loadDiagnosisResult.value = it
                havedata()
            }
    }

    fun saveDiagnose(){
        diagnoseApi.save(diagnosis.value!!).toResource()
            .subscribe{
                loadDiagnosisResult.value = it
                initbackToLast.value = true
            }
    }


    fun havedata(){
        typeItems.value?.filter { it.itemCode.equals(diagnosis.value?.diagnosisCode1) }?.map { selected(it) }
        thoracalgiaItems.value?.filter { it.itemCode.equals(diagnosis.value?.diagnosisCode2) }?.map { selectDiagnose(it) }
        sonItems.value?.filter { it.itemCode.equals(diagnosis.value?.diagnosisCode3) }?.map { it.checked=true }
        illnessItems.value?.filter { it.itemCode.equals(diagnosis.value?.criticalLevel) }?.map { it.checked=true }
    }

    override fun click(){
        diagnosis.value?.patientId = patientId
        diagnosis.value?.location = 1
        diagnosis.value?.doctorId = "3"
        diagnosis.value?.doctorName = "张三"
        typeItems.value?.filter { it.checked }?.map { diagnosis.value?.diagnosisCode1 = it.itemCode }
//        thoracalgiaItems.value?.filter { it.checked }?.map { diagnosis.value?.diagnosisCode2 = it.itemCode }
//        sonItems.value?.filter { it.checked }?.map { diagnosis.value?.diagnosisCode3 = it.itemCode }
        illnessItems.value?.filter { it.checked }?.map { diagnosis.value?.criticalLevel = it.itemCode }
        saveDiagnose()
    }

    fun selected(item:Dictionary){
        typeItems.value?.filter { it.checked }
            ?.map { it.checked = false }
        item.checked = true
        initShowSonList.value = false
        when(item.itemCode){
            "1"->{secItemsResult.value = loadthoracalgiaItemsResult.value}
            "2"->{secItemsResult.value = loadApoplexyResultItemsResult.value }
            "3"->{}
            "4"->{}
            "5"->{}
            "6"->{}
        }
        thoracalgiaItems.value?.filter { it.checked }?.map { selectDiagnose(it)}
    }

    fun selectDiagnose(item:Dictionary){
        thoracalgiaItems.value?.filter { it.checked }
            ?.map { it.checked = false }
        item.checked = true
        when(item.itemName){
            "非ACS胸痛" -> {
                loadsonItemsResult.value = loadnotACSItemsResult.value
                initShowSonList.value = true
            }
            "非心源性胸痛" -> {
                loadsonItemsResult.value = loadOtherXTItemsResult.value
                initShowSonList.value = true
            }
            "脑梗死" ->{
                loadsonItemsResult.value = loadInfarctItemsResult.value
                initShowSonList.value = true
            }
            "STEMI" ->{
                initStartConduitRoom.value = true
            }
            else ->{
                initShowSonList.value = false
                diagnosis.value?.diagnosisCode3 = ""
            }
        }
        diagnosis.value?.diagnosisCode2 = item.itemCode
    }

    fun selectDiagnoseSon(item:Dictionary){
        when(item.section){
            3->{ sonItems.value?.filter { it.checked }?.map { it.checked = false }
                item.checked = true
                diagnosis.value?.diagnosisCode3 = item.itemCode
            }
            4->{ illnessItems.value?.filter { it.checked }?.map { it.checked = false }
                item.checked = true
            }
        }


    }
}