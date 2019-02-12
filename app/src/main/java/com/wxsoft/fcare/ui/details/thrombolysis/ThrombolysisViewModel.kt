package com.wxsoft.fcare.ui.details.thrombolysis

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Thrombolysis
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.ThrombolysisApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class ThrombolysisViewModel @Inject constructor(private val thrombolysisApi: ThrombolysisApi,
                                                private val dictEnumApi: DictEnumApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title: String=""
        get() = "溶栓"
    override val clickableTitle: String
        get() = "保存"
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
    private val loadThrombolysis = MediatorLiveData<Resource<Response<Thrombolysis>>>()

    val thromPlaces:LiveData<List<Dictionary>>
    private val loadThromPlaces = MediatorLiveData<Resource<List<Dictionary>>>()

    val informed:LiveData<InformedConsent>
    private val loadInformedResult = MediatorLiveData<Resource<Response<InformedConsent>>>()

    init {
        modifySome = initModifySome.map { it }
        clickLine = loadClickLine.map { it }
        clickable = clickResult.map { it }
        thrombolysis = loadThrombolysis.map { (it as? Resource.Success)?.data?.result ?: Thrombolysis("") }
        thromPlaces = loadThromPlaces.map { (it as? Resource.Success)?.data?: emptyList() }
        informed = loadInformedResult.map { (it as? Resource.Success)?.data?.result?: InformedConsent("") }

        loadPlaces()
        getInformedConsent()
    }


    fun loadPlaces(){
        dictEnumApi.loadThromPlaces().toResource()
            .subscribe {
                loadThromPlaces.value = it
                thrombolysis.value?.setPlaceCheck(comefrom)
            }
    }

    //获取溶栓数据
    fun loadThrombolysis(id:String){
        if (id.isNullOrEmpty()) {
            loadThrombolysis.value = null
            return
        }
        thrombolysisApi.loadThrombolysis(id).toResource()
            .subscribe {
                loadThrombolysis.value = it
                thrombolysis.value?.setUpChecked()
            }
    }

    //获取溶栓知情同意书内容
    fun getInformedConsent(){
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
        if (isStart.equals(1)){//知情同意书开始时间
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



    //溶栓场所列表点击选择
    fun clickPlaces(item:Dictionary){
        thrombolysis.value?.thromTreatmentPlaceName = item.itemName
        thrombolysis.value?.throm_Treatment_Place = item.id
        initModifySome.value = "HidenDialog"
    }

    override fun click() {
        thrombolysis.value?.patientId = patientId
        thrombolysisApi.save(thrombolysis.value!!).toResource()
            .subscribe {
                when(it){
                    is Resource.Success->{
                        initModifySome.value = "saveSuccess"
                    }
                }

            }
    }

}