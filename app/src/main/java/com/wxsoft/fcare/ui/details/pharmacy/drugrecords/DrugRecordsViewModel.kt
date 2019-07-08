package com.wxsoft.fcare.ui.details.pharmacy.drugrecords

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PharmacyApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DrugRecordsViewModel  @Inject constructor(private val pharmacyApi: PharmacyApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon)  {

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            refreshList()
        }

    val drugrecords:LiveData<List<DrugRecord>>
    private val initDrugrecords = MediatorLiveData<Resource<Response<List<DrugRecord>>>>()

    val clikSomething:LiveData<String>
    private val initClikSomething = MediatorLiveData<String>()


    init {
        clikSomething = initClikSomething.map { it }
        drugrecords = initDrugrecords.map { (it as? Resource.Success)?.data?.result?.apply {
            forEach { item->item.doseString=item.dose.toString() }
        } ?: emptyList() }
    }

    fun click(){
        initClikSomething.value = "add"
    }

    fun refreshList(){
        pharmacyApi.getDrugRecord(patientId).toResource()
            .subscribe {
                initDrugrecords.value = it
            }
    }

    fun removeItem(pos:Int){
        val record = drugrecords.value!!.get(pos)
        disposable.add(pharmacyApi.deleteDrug(record.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::haveDelete,::error))

    }

    private fun haveDelete(response:Response<String>){
        initClikSomething.value = "delete"
    }

}