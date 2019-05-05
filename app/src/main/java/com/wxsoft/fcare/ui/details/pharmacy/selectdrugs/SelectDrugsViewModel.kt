package com.wxsoft.fcare.ui.details.pharmacy.selectdrugs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.drug.Drug
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.entity.drug.DrugTypeitem
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PharmacyApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class SelectDrugsViewModel @Inject constructor(private val pharmacyApi: PharmacyApi,
                                               override val sharedPreferenceStorage: SharedPreferenceStorage,
                                               override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){


    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadDrugs()
        }

    var selectedDrugs:ArrayList<Drug> = ArrayList()

    val clikSomething: LiveData<String>
    private val initClikSomething = MediatorLiveData<String>()

    val sumit: LiveData<Boolean>
    private val initSumit = MediatorLiveData<Boolean>()

    val drugs:LiveData<List<DrugTypeitem>>
    private val initDrugs = MediatorLiveData<Resource<Response<List<DrugTypeitem>>>>()

    val selectTypeDrugs:LiveData<List<Drug>>
    private val initSelectTypeDrugs = MediatorLiveData<List<Drug>>()

    val selectNum: LiveData<String>
    private val initselectNum = MediatorLiveData<String>()


    init {
        sumit = initSumit.map { it }
        clikSomething = initClikSomething.map { it }
        selectNum = initselectNum.map { it }
        drugs = initDrugs.map { (it as? Resource.Success)?.data?.result ?: emptyList() }
        selectTypeDrugs = initSelectTypeDrugs.map { it }
    }

    fun loadDrugs(){
        pharmacyApi.loadDrugs(patientId).toResource()
            .subscribe {
                initDrugs.value = it
                drugs.value?.first()?.checked = true
                if (!drugs.value.isNullOrEmpty()){clickDrugType(drugs.value?.first()!!)}
            }
    }


    fun click(){
        initSumit.value = true
    }

    fun clickDrugType(item:DrugTypeitem){
        drugs.value?.filter { it.checked }?.map { it.checked = false }
        item.checked = !item.checked
        initSelectTypeDrugs.value = item.items
    }

    fun selectDrug(item:Drug){
        item.checked = !item.checked
        if (item.checked) selectedDrugs.add(item) else selectedDrugs.remove(item)
        initselectNum.value = "选择( " + selectedDrugs.size + " )"
    }


}