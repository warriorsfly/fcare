package com.wxsoft.fcare.ui.details.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.lis.LisItem
import com.wxsoft.fcare.core.data.entity.lis.LisRecord
import com.wxsoft.fcare.core.data.entity.lis.LisRecordItem
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.LISApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.map
import javax.inject.Inject

class AssistantExaminationViewModel @Inject constructor(private val lisApi: LISApi,
                                                        override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                        override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title = "辅助检查"

    override val clickableTitle: String
        get() = ""
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

    var recordId:String=""

    val selectedType:LiveData<String>
    private val initSelectedType = MediatorLiveData<String>()

    val clickEdit:LiveData<String>
    private val loadClickEdit = MediatorLiveData<String>()

    val lisItems:LiveData<List<LisItem>>
    private val loadLisItemsResult = MediatorLiveData<Resource<Response<List<LisItem>>>>()

    val lisRecords:LiveData<List<LisRecord>>
    private val loadLisRecordsResult = MediatorLiveData<Resource<Response<List<LisRecord>>>>()


    init {
        clickEdit = loadClickEdit.map { it }
        selectedType = initSelectedType.map { it }
        clickable = clickResult.map { it }
        lisItems = loadLisItemsResult.map { (it as? Resource.Success)?.data?.result?: emptyList() }
        lisRecords = loadLisRecordsResult.map {
            if (selectedType.value.equals("3")) (it as? Resource.Success)?.data?.result?: emptyList() else getDataModel()
        }
        getLisItems()

    }


    private fun getLisItems(){
        lisApi.getLisItems().toResource()
            .subscribe {
                loadLisItemsResult.value = it
                if (lisItems.value != null){
                    if (lisItems.value!!.isNotEmpty()){
                        selectType(lisItems.value!!.first())
                    }
                }

            }
    }

    fun getLisRecords(lisId:String){
        lisApi.getPatientLisRecords(patientId,lisId).toResource()
            .subscribe{
                loadLisRecordsResult.value = it
            }
    }


    fun selectType(item:LisItem){//选中检查项目
        lisItems.value?.filter { it.checked }?.map { it.checked = false }
        item.checked = true
        getLisRecords(item.id)
        initSelectedType.value = item.id
    }

    fun addJGDB(){//新增肌酐蛋白检测
        if (selectedType.value.equals("3")){
            recordId = ""
            loadClickEdit.value = "ADDJGDB"
        }
    }

    fun editJGDBTime(item:LisRecord){//编辑一条数据
        recordId = item.id
        loadClickEdit.value = "EDITJGDB"
    }

    override fun click() {

    }

    fun getDataModel():List<LisRecord>{
        var arr = ArrayList<LisRecord>()
        arr.add(LisRecord("").apply {
            lisRecordDetails = getListem()
            excuteTime = "2018-02-22 13:34:00"
            reportTime = "2018-02-22 13:47:00"
        })
        return arr
    }

    fun getListem():List<LisRecordItem>{
        var arr = ArrayList<LisRecordItem>()
        arr.add(LisRecordItem("").apply {
            itemName = "白细胞计数"
            result = "6.3"
            referenceAange = "4.00-10.0"
            unit = "10^9/L"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "中性粒细胞百分比"
            result = "62.4"
            referenceAange = "50.0-70.0"
            unit = "%"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "淋巴细胞百分比"
            result = "33.1"
            referenceAange = "20.0-40.0"
            unit = "%"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "单核细胞百分比"
            result = "4.5"
            referenceAange = "3.0-10.0"
            unit = "%"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "中性粒细胞计数"
            result = "4.10"
            referenceAange = "2.00-7.0"
            unit = "10^9"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "淋巴细胞计数"
            result = "2.0"
            referenceAange = "0.8-4.00"
            unit = "10^9"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "单核细胞计数"
            result = "0.20"
            referenceAange = "0.12-0.8"
            unit = "10^9/L"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "红细胞计数"
            result = "4.33"
            unit = "10^12/L"
            referenceAange = "4.09-5.74"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "血红蛋白"
            result = "117"
            unit = "g/L"
            referenceAange = "120-172"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "红细胞压积"
            result = "34.7"
            unit = "%"
            referenceAange = "38.0-50.8"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "平均红细胞体积"
            result = "80.0"
            unit = "fL"
            referenceAange = "83.9-99.1"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "平均血红蛋白量"
            result = "27.1"
            unit = "pg"
            referenceAange = "27.8-33.8"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "平均血红蛋白浓度"
            result = "338"
            unit = "g/L"
            referenceAange = "320-355"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "红细胞分布宽度CV"
            result = "13.1"
            unit = "%"
            referenceAange = "0.0-14.6"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "血小板"
            result = "287.0"
            unit = "10^9L"
            referenceAange = "15.0-392.0"
        })
        arr.add(LisRecordItem("").apply {
            itemName = "血小板平均分布宽度"
            result = "10.9"
            unit = "fL"
            referenceAange = "12.0-22.0"
        })
        return arr
    }

}