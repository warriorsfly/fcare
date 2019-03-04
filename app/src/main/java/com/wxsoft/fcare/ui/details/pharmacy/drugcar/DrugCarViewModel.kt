package com.wxsoft.fcare.ui.details.pharmacy.drugcar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.drug.Drug
import com.wxsoft.fcare.core.data.entity.drug.DrugPackage
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PharmacyApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import javax.inject.Inject

class DrugCarViewModel @Inject constructor(private val pharmacyApi: PharmacyApi,
                                           override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title = "用药清单"
    override val clickableTitle: String
        get() = "编辑"
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
            getAllDrugPackages()
        }

    var selectedDrugsToDelete:ArrayList<Drug> = ArrayList()

    val selectAll :LiveData<Boolean>
    private val initSelectAll = MediatorLiveData<Boolean>()

    val selectedDrugs:LiveData<List<Drug>>
    val initSelectedDrugs = MediatorLiveData<List<Drug>>()

    val submitTitle: LiveData<String>
    val initSubmitTitle = MediatorLiveData<String>()

    val isEditing: LiveData<Boolean>
    val initIsEditing = MediatorLiveData<Boolean>()

    val selectNum: LiveData<String>
    val initselectNum = MediatorLiveData<String>()

    val submitSuccess: LiveData<Boolean>
    val initSubmitSuccess = MediatorLiveData<Boolean>()

    val drugPackages:LiveData<List<DrugPackage>>
    private val loaddrugPackagesResult = MediatorLiveData<Resource<Response<List<DrugPackage>>>>()

    init {
        submitSuccess = initSubmitSuccess.map { it }
        selectAll = initSelectAll.map { it }
        clickable = clickResult.map { it }
        submitTitle = initSubmitTitle.map { it }
        isEditing = initIsEditing.map { it }
        initIsEditing.value = false
        initSelectAll.value = false
        selectedDrugs = initSelectedDrugs.map { it }
        selectNum= initselectNum.map { it }
        drugPackages = loaddrugPackagesResult.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
    }

    override fun click(){
        initIsEditing.value = !initIsEditing.value!!
        if (initIsEditing.value!!) initSubmitTitle.value = "删除所选药物（" +selectedDrugsToDelete.size+"）" else initSubmitTitle.value = "提交药物清单"
    }


    fun subdelow(item:Drug){
        if (item.doseNum.isNullOrEmpty()){
            item.doseNum = "0"
        }  else {
            if (item.doseNum.toInt() != 0) item.doseNum = (item.doseNum.toInt() - 1).toString() else item.doseNum = "0"
        }
    }

    fun add(item:Drug){
        if (item.doseNum.isNullOrEmpty()){
            item.doseNum = "1"
        }  else {
            if (item.doseNum.toInt() != 0) item.doseNum = (item.doseNum.toInt() + 1).toString() else item.doseNum = "1"
        }
    }
    fun selected(item:Drug){
        item.checked = !item.checked
        checkSelectedDrug()
    }


    fun selectAllDrug(){
        initSelectAll.value = !selectAll.value!!
        selectedDrugs.value?.map { it.checked = selectAll.value!! }
        checkSelectedDrug()
    }

    fun checkSelectedDrug(){
        selectedDrugsToDelete.clear()
        selectedDrugs.value?.filter { it.checked }?.map { selectedDrugsToDelete.add(it) }
        if (initIsEditing.value!!) initSubmitTitle.value = "删除所选药物（" +selectedDrugsToDelete.size+"）"
    }

    fun submitDrugs(){
        val asg = selectedDrugs.value?.map { DrugRecord("").apply {
            patientId = this@DrugCarViewModel.patientId
            drugId = it.id
            drugName = it.name
            dose = it.dose
            doseUnit = it.doseUnit
            createdDate = DateTimeUtils.getCurrentTime()
            createrName = account.trueName
        } }?: emptyList()

        pharmacyApi.save(asg).toResource()
            .subscribe {
                when (it) {
                    is Resource.Success -> {
                        initSubmitSuccess.value = true
                        messageAction.value = Event("保存成功")
                    }
                    is Error -> {
                        messageAction.value = Event(it.message ?: "")
                    }
                }
            }

    }



    private fun getAllDrugPackages(){
        pharmacyApi.getAllDrugPackages().toResource()
            .subscribe {
                loaddrugPackagesResult.value = it
            }
    }

    fun deleteSelectdDrug(){
        initSelectedDrugs.value = selectedDrugs.value?.filter { !selectedDrugsToDelete.contains(it) }?.map { it }
    }

    fun selected(drugPackage:DrugPackage){
        drugPackage.checked = !drugPackage.checked
        val asg = ArrayList<Drug>()
        var arr = selectedDrugs.value?.map { it } ?: emptyList()
        asg.addAll(arr)
        if (drugPackage.checked){
            asg.addAll(
                drugPackage.drugPackageDetails.map {
                    it.drug
                }
            )
        }else{
            drugPackage.drugPackageDetails.map {
                 item ->
                 arr.filter{ it.name.equals(item.drugName) }.map { asg.remove(it) }
            }
        }
        initSelectedDrugs.value = asg
    }


}