package com.wxsoft.fcare.ui.details.assistant

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.lis.LisItem
import com.wxsoft.fcare.core.data.entity.lis.LisRecord
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
        lisRecords = loadLisRecordsResult.map { (it as? Resource.Success)?.data?.result?: emptyList() }
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

}