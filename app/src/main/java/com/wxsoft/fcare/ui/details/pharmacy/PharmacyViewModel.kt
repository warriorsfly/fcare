package com.wxsoft.fcare.ui.details.pharmacy

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Pharmacy
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.drug.Drug
import com.wxsoft.fcare.core.data.entity.drug.DrugPackage
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PharmacyApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class PharmacyViewModel @Inject constructor(private val pharmacyApi: PharmacyApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title: String=""
        get() = "用药"
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

    var comeFrom: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()

    val pharmacy:LiveData<Pharmacy>
    private val initPharmacy = MediatorLiveData<Resource<Response<Pharmacy>>>()

    val drugRecords:LiveData<List<DrugRecord>>
    private val initDrugRecords = MediatorLiveData<Resource<Response<List<DrugRecord>>>>()

    val drugs:LiveData<List<Drug>>
    private val loadDrugsResult = MediatorLiveData<Resource<Response<List<Drug>>>>()

    val drugPackages:LiveData<List<DrugPackage>>
    private val loaddrugPackagesResult = MediatorLiveData<Resource<Response<List<DrugPackage>>>>()

    val selectedDrugs:LiveData<MutableList<Drug>>
    private val loadSelectedDrugsResult = MediatorLiveData<MutableList<Drug>>()

    init {

        backToLast = initbackToLast.map { it }
        pharmacy = initPharmacy.map { (it as? Resource.Success)?.data?.result ?: Pharmacy("") }
        clickable = clickResult.map { it }
        drugRecords = initDrugRecords.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        drugs = loadDrugsResult.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        drugPackages = loaddrugPackagesResult.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        selectedDrugs = loadSelectedDrugsResult.map { it }
        getAllDrugs()
        getAllDrugPackages()

    }

    override fun click(){

    }

    fun getAllDrugs(){
        pharmacyApi.getAllDrugs().toResource()
            .subscribe {
                loadDrugsResult.value = it
                checkedPharmacy()
            }
    }

    fun getAllDrugPackages(){
        pharmacyApi.getAllDrugPackages().toResource()
            .subscribe {
                loaddrugPackagesResult.value = it
                checkedPharmacy()
            }
    }

    fun savePharmacy(){
        pharmacyApi.save(pharmacy.value?.drugRecordDetails!!).toResource()
            .subscribe {
                initDrugRecords.value = it
                initbackToLast.value = true
            }
    }

    fun getDrugRecord(){
        pharmacyApi.getDrugRecord(patientId).toResource()
            .subscribe {
                initDrugRecords.value = it
                checkedPharmacy()
                initPharmacy.value = null
                pharmacy.value?.let {
                    it.drugRecordDetails = drugRecords.value!!
                }
            }
    }

    fun selected(drugPackage:DrugPackage){
        drugPackage.checked = !drugPackage.checked
        refresh()
    }

    fun addDrug(drug:Drug){
        drug.checked = !drug.checked
        refresh()
    }

    fun removeDrug(drug:Drug){
        if (drug.bagdrug){
            drugPackages.value?.filter { it.checked }?.map { it.checked = false }
        }else{
            drug.checked = !drug.checked
        }
        refresh()
    }

    fun refresh(){
        var dsg = ArrayList<Drug>()
        drugPackages.value?.filter { it.checked }
            ?.map {
                dsg.addAll(
                    it.drugPackageDetails.map {
                        Drug("").apply {
                            name=it.drugName
                            dose = it.dose
                            doseUnit = it.doseUnit
                            bagdrug = true
                        }
                    }
                ) }
        val bf=drugs.value?.filter { it.checked }
            ?.map { it }?: emptyList()
        dsg.addAll(bf)
        loadSelectedDrugsResult.value = dsg
    }

    fun checkedPharmacy(){
        pharmacy.value?.drugRecordDetails?.map {
            var bagId = it.drugPackageId
            var druId = it.drugId
            var dose = it.dose
            drugPackages.value?.filter { it.id.equals(bagId) }?.map { it.checked = true }
            drugs.value?.filter {it.id.equals(druId) }?.map {
                it.checked = true
                it.dose = dose
                it.doseNum = dose.toString()
            }
        }
        refresh()
    }

    fun submit(){
        var asg = ArrayList<DrugRecord>()
        drugPackages.value?.filter { it.checked }
            ?.map {
                asg.addAll(
                    it.drugPackageDetails.map {
                        DrugRecord("").apply {
                            patientId = this@PharmacyViewModel.patientId
                            drugId = it.drugId
                            drugName = it.drugName
                            dose = it.dose
                        }
                    }
                )
            }

        val bf=drugs.value?.filter { it.checked }
            ?.map {DrugRecord("").apply {
                drugId = it.id
                drugName = it.name
                dose = it.dose
            } }?: emptyList()
        asg.addAll(bf)
        pharmacy.value?.drugRecordDetails = asg
        pharmacy.value?.drugRecordDetails?.map {
            it.patientId = patientId
        }

        if (comeFrom.equals("THROMBOLYSIS")){
            initbackToLast.value = true
            return
        }
        savePharmacy()
    }

}