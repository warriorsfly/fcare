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
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DiagnoseViewModel  @Inject constructor(private val diagnoseApi: DiagnoseApi,
                                             private val dictEnumApi: DictEnumApi,
//                                             private val pacsApi: PACSApi,
//                                             private val interventionApi: InterventionApi,
                                             override val sharedPreferenceStorage: SharedPreferenceStorage,
                                             override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

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
            getDiagnose()
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

    val diagnosis: LiveData<Diagnosis>
    private val loadDiagnosisResult = MediatorLiveData<Resource<Response<Diagnosis>>>()

    val submitDiagnosis: LiveData<Diagnosis>
    val loadSubmitDiagnosis = MediatorLiveData<Diagnosis>()

    val thoracalgiaItems: LiveData<List<Dictionary>>
    private val secItemsResult = MediatorLiveData<List<Dictionary>>()


    val sonItems: LiveData<List<Dictionary>>
    private val loadsonItemsResult = MediatorLiveData<List<Dictionary>>()

    val illnessItems: LiveData<List<Dictionary>>
    private val loadIllnessItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val modifySomeThing: LiveData<String>
    val loadModifySomeThing= MediatorLiveData<String>()

    init {
        modifySomeThing = loadModifySomeThing.map { it }
        backToLast = initbackToLast.map { it }

        submitDiagnosis = loadSubmitDiagnosis.map { it?:Diagnosis(createrId = account.id,createrName = account.trueName) }

        showSonList = initShowSonList.map { it }
        diagnosis = loadDiagnosisResult.map { (it as? Resource.Success)?.data?.result?: Diagnosis(createrId = account.id,createrName = account.trueName) }
        loadDiagnosisResult.value = null
        thoracalgiaItems = secItemsResult.map { it ?: emptyList() }
        sonItems = loadsonItemsResult.map { it?: emptyList() }
        illnessItems = loadIllnessItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        loadIllness()
    }


    fun loadThoracalgias(){
        disposable.add(dictEnumApi.loadSecondTypes(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getThoracalgias,::error))
    }

    fun getThoracalgias(response:List<Dictionary>){
        secItemsResult.value = response
        havedata()
    }

    private fun loadIllness(){
        disposable.add( dictEnumApi.loadIllnessResultItems().toResource()
            .subscribe{
                loadIllnessItemsResult.value = it
                havedata()
            })
    }



    fun getDiagnose(){
        disposable.add(diagnoseApi.getDiagnosis(id).toResource()
            .subscribe{
                loadDiagnosisResult.value = it
                loadSubmitDiagnosis.value = diagnosis.value
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


    fun getSonList(id:String){
        disposable.add(dictEnumApi.loadSonList(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadSonList,::error))
    }

    fun loadSonList(response:List<Dictionary>){
        loadsonItemsResult.value = response
        if (response.isEmpty()){
            click()
        }
    }


    private fun havedata(){
        thoracalgiaItems.value?.filter { it.id == diagnosis.value?.diagnosisCode2 }?.map { checkDianose(it) }
        sonItems.value?.filter { it.id == diagnosis.value?.diagnosisCode3 }?.map { it.checked=true }
        illnessItems.value?.filter { it.id == diagnosis.value?.criticalLevel }?.map { it.checked=true }
    }

    fun click(){
        if (activityType.equals("SelectDiagnose")){
            initbackToLast.value = "haveSelected"
        }else{
            submitDiagnosis.value?.apply {
                patientId=this@DiagnoseViewModel.patientId
                location=1
                doctorId=account.id
                doctorName=account.trueName
            }
            illnessItems.value?.filter { it.checked }?.map { submitDiagnosis.value?.criticalLevel = it.id }
            if(submitDiagnosis.value?.criticalLevel.isNullOrEmpty()) {
                loadModifySomeThing.value = "have_not_seleted_criticalLevel"
                return
            }
            submitDiagnosis.value?.sceneType = sceneTypeId
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
        diagnosis.value?.diagnosisCode3 = ""
        diagnosis.value?.diagnosisCode3Name = ""
    }

    fun selectDiagnose(item:Dictionary){
        getSonList(item.id)
        initShowSonList.value = true
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
        click()
    }

//    fun commitNoticePacs(){
//        disposable.add(
//            pacsApi.notice(patientId,account.id).toResource()
//                .subscribe {
//
//                    when(it){
//                        is Resource.Success->{
//                            messageAction.value= Event("通知成功")
//                        }
//                        is Resource.Error->{
//                            messageAction.value=Event(it.throwable.message?:"")
//                        }
//                    }
//                }
//        )
//    }
//
//    fun commitNoticeInv(){
//        disposable.add(
//            interventionApi.notice(patientId,account.id).toResource()
//                .subscribe {
//
//                    when(it){
//                        is Resource.Success->{
//                            messageAction.value= Event("通知成功")
//                        }
//                        is Resource.Error->{
//                            messageAction.value=Event(it.throwable.message?:"")
//                        }
//                    }
//                }
//        )
//    }
}