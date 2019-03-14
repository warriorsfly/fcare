package com.wxsoft.fcare.ui.details.diagnose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DiagnoseApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.InterventionApi
import com.wxsoft.fcare.core.data.remote.PACSApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import javax.inject.Inject

class DiagnoseViewModel  @Inject constructor(private val diagnoseApi: DiagnoseApi,
                                             private val dictEnumApi: DictEnumApi,
                                             private val pacsApi: PACSApi,
                                             private val interventionApi: InterventionApi,
                                             override val sharedPreferenceStorage: SharedPreferenceStorage,
                                             override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title="诊断"
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
            loadThoracalgias()
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

    var activityType: String = ""
        set(value) {
            if (value == "") return
            field = value
        }


    val backToLast:LiveData<String>
    private val initbackToLast = MediatorLiveData<String>()

    val showSonList:LiveData<Boolean>
    private val initShowSonList = MediatorLiveData<Boolean>()

    val startConduitRoom:LiveData<Boolean>
    private val initStartConduitRoom = MediatorLiveData<Boolean>()

    val startCT:LiveData<Boolean>
    private val initStartCT = MediatorLiveData<Boolean>()

    val diagnosis: LiveData<Diagnosis>
    private val loadDiagnosisResult = MediatorLiveData<Resource<Response<Diagnosis>>>()

    val submitDiagnosis: LiveData<Diagnosis>
    val loadSubmitDiagnosis = MediatorLiveData<Diagnosis>()

    val thoracalgiaItems: LiveData<List<Dictionary>>
    private val secItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadthoracalgiaItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadApoplexyResultItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val sonItems: LiveData<List<Dictionary>>
    private val loadsonItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadnotACSItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadOtherXTItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()
    private val loadInfarctItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val illnessItems: LiveData<List<Dictionary>>
    private val loadIllnessItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()


    init {

        backToLast = initbackToLast.map { it }

        submitDiagnosis = loadSubmitDiagnosis.map { it?:Diagnosis(createrId = account.id,createrName = account.trueName) }

        startConduitRoom = initStartConduitRoom.map { it }
        startCT = initStartCT.map { it }
        showSonList = initShowSonList.map { it }
        diagnosis = loadDiagnosisResult.map { (it as? Resource.Success)?.data?.result?: Diagnosis(createrId = account.id,createrName = account.trueName) }

        clickable = clickResult.map { it }
        thoracalgiaItems = secItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        sonItems = loadsonItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        illnessItems = loadIllnessItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        loadNotACS()
        loadOtherXT()
        loadIllness()
        loadInfarct()
        loadApoplexyResultItems()
    }


    private fun loadThoracalgias(){
        disposable.add(dictEnumApi.loadSecondTypes(patientId).toResource()
            .subscribe{
                secItemsResult.value = it
                havedata()
            })
    }

    private fun loadNotACS(){
        disposable.add(dictEnumApi.loadDict27Diagnosis().toResource()
            .subscribe{
                loadnotACSItemsResult.value = it
                havedata()
            })
    }

    private fun loadOtherXT(){
        disposable.add(dictEnumApi.loadConsciousness().toResource()
            .subscribe{
                loadOtherXTItemsResult.value = it
                havedata()
            })
    }

    private fun loadInfarct(){//脑梗死
        disposable.add(dictEnumApi.loadInfarct().toResource()
            .subscribe{
                loadInfarctItemsResult.value = it
            })
    }

    private fun loadIllness(){
        disposable.add( dictEnumApi.loadIllnessResultItems().toResource()
            .subscribe{
                loadIllnessItemsResult.value = it
                havedata()
            })
    }

    private fun loadApoplexyResultItems(){
        disposable.add(dictEnumApi.loadApoplexyResultItems().toResource()
            .subscribe{
                loadApoplexyResultItemsResult.value = it
                havedata()
            })
    }

    fun getDiagnose(){
        disposable.add(diagnoseApi.getDiagnosis(id).toResource()
            .subscribe{
                loadDiagnosisResult.value = it
                havedata()
            })
    }

    fun saveDiagnose(diagnose:Diagnosis){
        disposable.add(diagnoseApi.save(diagnose).toResource()
            .subscribe{
                loadDiagnosisResult.value = it
                initbackToLast.value = "back"
            })
    }


    private fun havedata(){
        thoracalgiaItems.value?.filter { it.id == diagnosis.value?.diagnosisCode2 }?.map { checkDianose(it) }
        sonItems.value?.filter { it.id == diagnosis.value?.diagnosisCode3 }?.map { it.checked=true }
        illnessItems.value?.filter { it.id == diagnosis.value?.criticalLevel }?.map { it.checked=true }
    }

    override fun click(){
        if (activityType.equals("SelectDiagnose")){
            diagnosis.value?.apply {
                patientId=this@DiagnoseViewModel.patientId
                location=1
                doctorId=account.id
                doctorName=account.trueName
                sceneType = sceneTypeId
                initbackToLast.value = "haveSelected"
            }
        }else{
            illnessItems.value?.filter { it.checked }?.map { submitDiagnosis.value?.criticalLevel = it.id }
            saveDiagnose(submitDiagnosis.value!!)
        }


//        thoracalgiaItems.value?.filter { it.checked }?.map { diagnosis.value?.diagnosisCode2 = it.id }
//        sonItems.value?.filter { it.checked }?.map { diagnosis.value?.diagnosisCode3 = it.id }


    }


    private fun checkDianose(item:Dictionary){
        thoracalgiaItems.value?.filter { it.checked }
            ?.map { it.checked = false }
        item.checked = true

        diagnosis.value?.diagnosisCode2 = item.id
        diagnosis.value?.diagnosisCode2Name = item.itemName
    }

    fun selectDiagnose(item:Dictionary){

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
            "主动脉夹层","肺动脉栓塞" ->{
                initStartCT.value = true
            }
            "STEMI" ->{
                initStartConduitRoom.value = true
            }
            else ->{
                initShowSonList.value = false
                diagnosis.value?.diagnosisCode3 = ""
            }
        }
        checkDianose(item)
    }

    fun selectDiagnoseSon(item:Dictionary){
        when(item.section){
            3->{ sonItems.value?.filter { it.checked }?.map { it.checked = false }
                item.checked = true
                diagnosis.value?.diagnosisCode3 = item.id
                diagnosis.value?.diagnosisCode3Name = item.itemName
            }
            4->{ illnessItems.value?.filter { it.checked }?.map { it.checked = false }
                item.checked = true
            }
        }


    }

    fun commitNoticePacs(){
        disposable.add(
            pacsApi.notice(patientId,account.id).toResource()
                .subscribe {

                    when(it){
                        is Resource.Success->{
                            messageAction.value= Event("通知成功")
                        }
                        is Resource.Error->{
                            messageAction.value=Event(it.throwable.message?:"")
                        }
                    }
                }
        )
    }

    fun commitNoticeInv(){
        disposable.add(
            interventionApi.notice(patientId,account.id).toResource()
                .subscribe {

                    when(it){
                        is Resource.Success->{
                            messageAction.value= Event("通知成功")
                        }
                        is Resource.Error->{
                            messageAction.value=Event(it.throwable.message?:"")
                        }
                    }
                }
        )
    }
}