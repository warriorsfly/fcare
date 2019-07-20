package com.wxsoft.fcare.ui.details.thrombolysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Thrombolysis
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.ThrombolysisApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ThrombolysisViewModel @Inject constructor(private val thrombolysisApi: ThrombolysisApi,
                                                private val dictEnumApi: DictEnumApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon)  {


    var drugs = mutableListOf<DrugRecord>()

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadThrombolysis(value)
        }

    var comefrom: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    var id: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    val clickLine:LiveData<String>
    private val loadClickLine =  MediatorLiveData<String>()

    val modifySome:LiveData<String>
    private val initModifySome =  MediatorLiveData<String>()

    val thrombolysis:LiveData<Thrombolysis>
    private val loadThrombolysis = MediatorLiveData<Thrombolysis>()

    val thromPlaces:LiveData<List<Dictionary>>
    private val loadThromPlaces = MediatorLiveData<Resource<List<Dictionary>>>()

    val informed:LiveData<InformedConsent>
    private val loadInformedResult = MediatorLiveData<Resource<Response<InformedConsent>>>()
    val itemForChangeTime = MediatorLiveData<DrugRecord>()

    init {
        modifySome = initModifySome.map { it }
        clickLine = loadClickLine.map { it }
        thrombolysis = loadThrombolysis.map { it?.apply {
            drugRecords?.forEach{
                item->item.doseString=item.dose.toString()
                item.selected = !item.id.isNullOrEmpty()
            }
        }?: Thrombolysis("",account.id) }
        thromPlaces = loadThromPlaces.map { (it as? Resource.Success)?.data?: emptyList() }
        informed = loadInformedResult.map { (it as? Resource.Success)?.data?.result?: InformedConsent("") }
//        loadPlaces()
        getInformedConsent()
    }

    private fun loadPlaces(){
        dictEnumApi.loadDictsByPatient("16",patientId).toResource()
            .subscribe {
                loadThromPlaces.value = it
                thrombolysis.value?.setPlaceCheck(comefrom)
            }
    }

    //获取溶栓数据
    fun loadThrombolysis(id:String){
        if (id.isEmpty()) {
            loadThrombolysis.value = null
            return
        }
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
    private fun getInformedConsent(){
        thrombolysisApi.getInformedConsentById("2").toResource()
            .subscribe {
                loadInformedResult.value = it
            }
    }

    //点击选择溶栓场所
    fun selectPlace(line:Int){
        if (thrombolysis.value != null){
            when(line){
                1 -> loadClickLine.value = "place"
            }
        }
    }

    //点击去做知情同意书
    fun toInformedConsent(){
        if (informed.value != null){
            loadClickLine.value = "informedConsent"
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

    //溶栓用药
    fun toDrugs(){
        loadClickLine.value = "drugs"
    }


    //并发症
    fun toComplication(){
        loadClickLine.value = "complication"
    }


    //溶栓场所列表点击选择
    fun clickPlaces(item:Dictionary){
        thrombolysis.value?.thromTreatmentPlaceName = item.itemName
        thrombolysis.value?.throm_Treatment_Place = item.id
        initModifySome.value = "HidenDialog"
    }

    fun click() {
        thrombolysis.value?.patientId = patientId
        thrombolysis.value!!.drugRecords = drugs
        thrombolysisApi.save(thrombolysis.value!!).toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        initModifySome.value = "saveSuccess"
                    }
                }

            }
    }

    fun subdelow(item: DrugRecord){
        if (item.dose <=1f){
            return
        }  else {
            item.doseString = (item.dose - 1).toString()
        }
    }

    fun add(item: DrugRecord) {
        item.doseString = (item.dose + 1).toString()
    }

    fun deleteDrug(item: DrugRecord){
        drugs.remove(item)
        loadClickLine.value = "refreshDrugs"
    }

    fun changeTime(item: DrugRecord) {
        itemForChangeTime.value=item
    }
}