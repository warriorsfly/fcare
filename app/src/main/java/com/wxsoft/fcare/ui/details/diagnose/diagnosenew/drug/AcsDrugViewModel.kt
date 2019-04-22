package com.wxsoft.fcare.ui.details.diagnose.diagnosenew.drug

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.drug.ACSDrug
import com.wxsoft.fcare.core.data.entity.drug.Drug
import com.wxsoft.fcare.core.data.entity.drug.DrugPackage
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.PharmacyApi
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AcsDrugViewModel @Inject constructor(private val dictEnumApi: DictEnumApi,
                                           private val pharmacyApi: PharmacyApi,
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
            getDrugs(value)
        }

    val drugs1: LiveData<List<Dictionary>>
    private val loadDrugs1 = MediatorLiveData<List<Dictionary>>()

    val drugs2: LiveData<List<Dictionary>>
    private val loadDrugs2 = MediatorLiveData<List<Dictionary>>()

    val clickSomething: LiveData<String>
    private val loadClickSomething = MediatorLiveData<String>()

    val acsDrug: LiveData<ACSDrug>
    private val loadAcsDrug = MediatorLiveData<ACSDrug>()


    init {
        acsDrug = loadAcsDrug.map { it?: ACSDrug("") }
        clickSomething = loadClickSomething.map { it }
        drugs1 = loadDrugs1.map { it?: emptyList() }
        drugs2 = loadDrugs2.map { it?: emptyList() }
        getDrugs1("235")
        getDrugs2("22")
    }

    fun changeDrug(id:String){
        loadClickSomething.value = id
    }

    fun subdelow1(){
        if (acsDrug.value?.aspirin_Dose_str.isNullOrEmpty()){
            acsDrug.value?.aspirin_Dose_str = "0"
        }  else {
            if (acsDrug.value?.aspirin_Dose_str!!.toDouble() >25) acsDrug.value?.aspirin_Dose_str = (acsDrug.value?.aspirin_Dose_str!!.toDouble() - 25).toString() else acsDrug.value?.aspirin_Dose_str = "0"
        }
    }
    fun subdelow2(){
        if (acsDrug.value?.acs_Drug_Dose_str.isNullOrEmpty()){
            acsDrug.value?.acs_Drug_Dose_str = "0"
        }  else {
            if (acsDrug.value?.acs_Drug_Dose_str!!.toDouble() >25) acsDrug.value?.acs_Drug_Dose_str = (acsDrug.value?.acs_Drug_Dose_str!!.toDouble() - 25).toString() else acsDrug.value?.acs_Drug_Dose_str = "0"
        }
    }
    fun subdelow3(){
        if (acsDrug.value?.anticoagulation_Unit.isNullOrEmpty()){
            acsDrug.value?.anticoagulation_Unit = "0"
        }  else {
            if (acsDrug.value?.anticoagulation_Unit!!.toDouble() >25) acsDrug.value?.anticoagulation_Unit = (acsDrug.value?.anticoagulation_Unit!!.toInt() - 25).toString() else acsDrug.value?.anticoagulation_Unit = "0"
        }
    }

    fun add1(){
            if (acsDrug.value?.aspirin_Dose_str.isNullOrEmpty()){
                acsDrug.value?.aspirin_Dose_str = "25"
            }  else {
                if (acsDrug.value?.aspirin_Dose_str!!.toDouble() != 0.0) acsDrug.value?.aspirin_Dose_str = (acsDrug.value?.aspirin_Dose_str!!.toDouble() + 25).toString() else acsDrug.value?.aspirin_Dose_str = "25"
            }
    }
    fun add2(){
            if (acsDrug.value?.acs_Drug_Dose_str.isNullOrEmpty()){
                acsDrug.value?.acs_Drug_Dose_str = "25"
            }  else {
                if (acsDrug.value?.acs_Drug_Dose_str!!.toDouble() != 0.0) acsDrug.value?.acs_Drug_Dose_str = (acsDrug.value?.acs_Drug_Dose_str!!.toDouble() + 25).toString() else acsDrug.value?.acs_Drug_Dose_str = "25"
            }
    }
    fun add3(){
            if (acsDrug.value?.anticoagulation_Unit.isNullOrEmpty()){
                acsDrug.value?.anticoagulation_Unit = "25"
            }  else {
                if (acsDrug.value?.anticoagulation_Unit!!.toInt() != 0) acsDrug.value?.anticoagulation_Unit = (acsDrug.value?.anticoagulation_Unit!!.toInt() + 25).toString() else acsDrug.value?.anticoagulation_Unit = "25"
            }
    }

    fun change(item:Dictionary){
        drugs1.value?.filter { it.checked }?.map { it.checked = false }
        drugs2.value?.filter { it.checked }?.map { it.checked = false }
        item.checked = !item.checked
    }

    fun cancel(){
        loadClickSomething.value = "cancel"
    }

    fun sure(){
        drugs1.value?.filter { it.checked }?.map {
            acsDrug.value?.acs_Drug_Type = it.id
            acsDrug.value?.acsDrugTypeName = it.itemName
        }
        drugs2.value?.filter { it.checked }?.map {
            acsDrug.value?.anticoagulation_Drug = it.id
            acsDrug.value?.anticoagulation_Drug_Name = it.itemName
        }
        loadClickSomething.value = "ok"
    }



    fun getDrugs(id:String){
        disposable.add(pharmacyApi.getACSDrug(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadAcsDrug,::error))
    }

    fun loadAcsDrug(response:Response<ACSDrug>){
        loadAcsDrug.value  = response.result?.apply { haveDrugs() }
    }

    fun getDrugs1(id:String){
        disposable.add(dictEnumApi.loadDrugs1(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadSonList1,::error))
    }

    fun loadSonList1(response:List<Dictionary>){
        loadDrugs1.value = response
    }

    fun getDrugs2(id:String){
        disposable.add(dictEnumApi.loadDrugs1(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadSonList2,::error))
    }

    fun loadSonList2(response:List<Dictionary>){
        loadDrugs2.value = response
    }

    fun save(){
        acsDrug.value?.patientId= patientId
        disposable.add(pharmacyApi.saveAcs(acsDrug.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::saveResult,::error))
    }

    fun saveResult(response:Response<String>){
        loadClickSomething.value = "saveResult"
    }
}