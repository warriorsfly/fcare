package com.wxsoft.fcare.ui.details.cure

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.entity.chest.Intervention
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.*
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CureViewModel @Inject constructor(private val cureApi: CureApi,
                                        private val thrombolysisApi: ThrombolysisApi,
                                        private val dictEnumApi: DictEnumApi,
                                        private val interventionApi: InterventionApi,
                                        private val inforApi: InformedApi,
                                        override val sharedPreferenceStorage: SharedPreferenceStorage,
                                        override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {


    var type: String = ""
    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadCure()
        }
    var typeId=ObservableField<String>()

    var num1= ObservableField<String>()
    var num2= ObservableField<String>()
    //pci
    val intervention: LiveData<Intervention>
    var docs:List<User> = emptyList()
    private val loadInterventionResult = MediatorLiveData<Intervention>()
    val modifySome: LiveData<String>
    private val initModifySome =  MediatorLiveData<String>()
    val talk: LiveData<Talk>
    val loadTalk = MediatorLiveData<Talk>()

    //溶栓
    val thrombolysis:LiveData<Thrombolysis>
    private val loadThrombolysis = MediatorLiveData<Thrombolysis>()
    val thromPlaces:LiveData<List<Dictionary>>
    private val loadThromPlaces = MediatorLiveData<Resource<List<Dictionary>>>()
    val informed:LiveData<InformedConsent>
    private val loadInformedResult = MediatorLiveData<Resource<Response<InformedConsent>>>()
    var drugs = mutableListOf<DrugRecord>()

    //CABG
    val cabg:LiveData<CABG>
    private val loadCABG = MediatorLiveData<CABG>()

    //Cure
    val cure:LiveData<Cure>
    private val loadCure = MediatorLiveData<Cure>()

    init {
        cure = loadCure.map { it?:Cure("") }
        modifySome = initModifySome.map { it }
        intervention = loadInterventionResult.map { it?: Intervention("",createrId = account.id).apply { patientId = this@CureViewModel.patientId }  }
        talk = loadTalk.map { it?:Talk("") }

        thrombolysis = loadThrombolysis.map { it?: Thrombolysis("",account.id).apply { patientId = this@CureViewModel.patientId } }
        thromPlaces = loadThromPlaces.map { (it as? Resource.Success)?.data?: emptyList() }
        informed = loadInformedResult.map { (it as? Resource.Success)?.data?.result?: InformedConsent("").apply { patientId = this@CureViewModel.patientId } }
        cabg = loadCABG.map { it?:CABG("").apply { patientId = this@CureViewModel.patientId }}
    }

    fun loadCure(){
        disposable.add(cureApi.loadCure(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadCureResult,::error))
    }

    private fun loadCureResult(response: Response<Cure>){
        loadCure.value = response.result
        if(response.result?.treatStrategy == null) {
            initModifySome.value = "isnull"
            return
        }
        typeId.set(response.result?.treatStrategy?.strategyCode?:"")
        num1.set(response.result?.treatStrategy?.preoperative_Timi_Level.toString())
        num2.set(response.result?.treatStrategy?.postoperative_Timi_Level.toString())
        type(typeId.get()!!)
    }
    fun type(id:String){
        when(id){
            "14-01","14-3","14-5" ->{//pci
                type = "pci"
                getInformedConsent("1")
                getPciTalk()
                if (cure.value?.intervention!=null){
                    loadInterventionResult.value = cure.value?.intervention
                    loadTalk.value =null
                    return
                }
                loadIntervention()
            }
            "14-2" ->{//溶栓
                type = "throm"
                getInformedConsent("2")
                loadPlaces()
                if (cure.value?.throm!=null){
                    loadThrombolysis.value = cure.value?.throm
                    return
                }
                loadThrombolysis(patientId)
            }
            "14-7"->{//CABG
                type = "cabg"
                loadCABG.value = cure.value?.cabg
            }
            else ->{}
        }
    }

    private fun loadIntervention(){
        disposable.add(interventionApi.getIntervention(patientId).zipWith(interventionApi.getInterventionDocs(account.id,patientId))
            .toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        loadInterventionResult.value=it.data.first.result
                        docs=it.data.second.result?: emptyList()
                    }
                }
            })

    }

    fun getPciTalk(){
        if (cure.value?.intervention?.informedConsentId.isNullOrEmpty()) {
            loadTalk.value =null
            return
        }
        disposable.add(inforApi.getTalkById(cure.value?.intervention?.informedConsentId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::gettalk,::error))
    }
    fun gettalk(response:Response<Talk>){
        loadTalk.value = response.result?.apply { judgeTime() }
    }

    //获取溶栓数据
    fun loadThrombolysis(id:String){
        disposable.add(thrombolysisApi.loadThrom(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::getThrom,::error))
    }

    private fun getThrom(response: Response<List<Thrombolysis>>){
        if (response.result!!.isNotEmpty()){
            loadThrombolysis.value = response.result?.first()
            thrombolysis.value?.setUpChecked()
        }else{
            loadThrombolysis.value = null
        }
    }

    //获取溶栓知情同意书内容
    private fun getInformedConsent(id:String){
        thrombolysisApi.getInformedConsentById(id).toResource()
            .subscribe {
                loadInformedResult.value = it
            }
    }
    private fun loadPlaces(){
        dictEnumApi.loadDictsByPatient("16",patientId).toResource()
            .subscribe {
                loadThromPlaces.value = it
            }
    }
    //点击选择溶栓场所
    fun selectPlace(line:Int){
        if (thrombolysis.value != null){
            when(line){
                1 -> initModifySome.value = "place"
            }
        }
    }

    //点击去做知情同意书
    fun toInformedConsent(){
        if (informed.value != null){
            initModifySome.value = "informedConsent"
        }
    }
    //修改知情同意书时间
    fun modifyInformedTime(isStart:Int){
        if (isStart == 1){//知情同意书开始时间
            initModifySome.value = "ModifyStartInformedTime"
        }else{//知情同意书签署时间
            initModifySome.value = "ModifyEndInformedTime"
        }
    }

    //开始溶栓点击事件
    fun startthromTiem(){
        initModifySome.value = "ModifyStartThromTime"
    }

    //结束溶栓点击事件
    fun endthromTime(){
        initModifySome.value = "ModifyEndThromTime"
    }

    //溶栓后造影
    fun radiographyTime(){
        initModifySome.value = "ModifyRadiographyTime"
    }

    //溶栓场所列表点击选择
    fun clickPlaces(item:Dictionary){
        thrombolysis.value?.thromTreatmentPlaceName = item.itemName
        thrombolysis.value?.throm_Treatment_Place = item.id
        initModifySome.value = "HidenDialog"
    }




    fun clickSelectTime(location:String){
        initModifySome.value= location
    }

    fun seletedNum1(id:String){
        num1.set(id)
        cure.value?.treatStrategy?.preoperative_Timi_Level = id.toInt()
    }
    fun seletedNum2(id:String){
        num2.set(id)
        cure.value?.treatStrategy?.postoperative_Timi_Level = id.toInt()
    }

    fun save(){
        when(type){
            "pci"->{
                cure.value?.intervention = intervention.value!!
            }
            "throm"->{
                cure.value?.throm = thrombolysis.value!!
            }
            "cabg"->{
                cure.value?.cabg = cabg.value!!
            }
        }
        submit()
    }

    fun submit(){
        disposable.add(cureApi.save(cure.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::submitResult,::error))
    }
    fun submitResult(response:Response<String>){
        if (response.success) initModifySome.value = "saveSuccess"
    }


}