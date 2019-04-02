package com.wxsoft.fcare.ui.details.medicalhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.MedicalHistory
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.drug.DrugHistory
import com.wxsoft.fcare.core.data.entity.previoushistory.History2
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.MedicalHistoryApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class MedicalHistoryViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                                  private val medicalHistoryApi: MedicalHistoryApi,
//                                                  private val fileApi: FileApi,
                                                  override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                  override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) {
    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadData()
        }

    private var saveAble=true

    val bitmaps= mutableListOf<String>()


    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()


    val medicalHistory:LiveData<MedicalHistory>
    val loadMedicalHistoryResult = MediatorLiveData<Response<MedicalHistory>>()

    val drugHistory:LiveData<List<History2>>
    val loadDrugHistoryResult = MediatorLiveData<List<History2>>()

    val historyPhoto:LiveData<String>
    private val loadPhoto = MediatorLiveData<String>()

    val providerItems: LiveData<List<Dictionary>>
    private val loadProviderItemsResult = MediatorLiveData<List<Dictionary>>()

    val historyItems: LiveData<List<Dictionary>>
    private val loadHistoryItemsResult = MediatorLiveData<List<Dictionary>>()

    val monitorClick:LiveData<String>
    private val loadmonitorClick = MediatorLiveData<String>()

    init {
        monitorClick = loadmonitorClick.map { it }
        backToLast = initbackToLast.map { it }
        historyPhoto = loadPhoto.map { it }
        providerItems = loadProviderItemsResult.map { it }
        historyItems = loadHistoryItemsResult.map { it }

        medicalHistory = loadMedicalHistoryResult.map { it.result?: MedicalHistory("") }
        drugHistory = loadDrugHistoryResult.map { it?: emptyList() }

    }


    private fun loadData(){
        disposable.add(dicEnumApi.loadMedicalHistoryItems(patientId)
            .zipWith(dicEnumApi.loadMedicalHistoryProviderItems())
            .zipWith(medicalHistoryApi.loadMedicalHistory(patientId))
            .subscribeOn(Schedulers.io())
            .doOnSuccess { zip ->

                if(zip.second.result?.id.isNullOrEmpty()){
                    zip.first.second.firstOrNull()?.checked=true
                }else{

                    zip.first.second.filter { it.id == zip.second.result?.provide }.map {it.checked = true }

                    zip.first.first.filter { item-> zip.second.result?.pastHistorys?.any { it.phCode==item.id }?:false }
                        .map {it.checked = true }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                loadProviderItemsResult.value = it.first.second
                loadHistoryItemsResult.value=it.first.first
                loadMedicalHistoryResult.value=it.second
                medicalHistory.value?.getPastHistorys()
                loadDrugHistoryResult.value = medicalHistory.value?.drugHistorys

//                disposable.add(medicalHistoryApi.loadDrugHistory(patientId)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe {drugs->
//
//                        if(!it.second.result?.drugHistorys.isNullOrEmpty()) {
//                            val histories = it.second.result!!.drugHistorys
//                            drugs.result?.forEach { drug ->
//                                histories.firstOrNull { his -> his.drugId == drug.id }?.let { history2 ->
//                                    drug.dose = history2.dose
//                                }
//                            }
//                        }
//                        loadDrugHistoryResult.value=drugs.result
//
//                    })

            })
    }

    private fun saveMedicalHistory() {

        medicalHistory.value?.also {history->
            if (saveAble) {
                saveAble = false
                val files = bitmaps.map {
                    val file = File(it)
                    return@map MultipartBody.Part.createFormData(
                        "images",
                        it.split("/").last(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    )
                }
                history.drugHistorys = drugHistory.value!!
                if (files.isNullOrEmpty()) {
                    medicalHistoryApi.save(history).toResource().subscribe {

                        when (it) {
                            is Resource.Success -> {
//                                clickResult.value = true
                                messageAction.value = Event("保存成功")
                                initbackToLast.value = true
                                saveAble = true
                            }
                            is Resource.Error -> {
//                                clickResult.value = true
                                messageAction.value = Event(it.throwable.message ?: "")
                            }
                        }
                    }
                } else {
                    medicalHistoryApi.save(history, files).toResource().subscribe {
                        when (it) {
                            is Resource.Success -> {
//                                clickResult.value = true
                                messageAction.value = Event("保存成功")
                                initbackToLast.value = true
                                saveAble = true
                            }
                            is Resource.Error -> {
//                                clickResult.value = true
                                messageAction.value = Event(it.throwable.message ?: "")
                            }
                        }
                    }
                }
            }
        }
    }

    fun submit(){
        providerItems.value?.filter { it.checked }
            ?.map { medicalHistory.value?.provide = it.id }
        historyItems.value?.filter { it.checked }
            ?.map { medicalHistory.value?.ph = it.id }
        medicalHistory.value?.patientId = patientId
        saveMedicalHistory()
    }

    fun click(){
        submit()
    }


    fun clickItems(line:String){
        loadmonitorClick.value = line
    }

    fun subdelow(item: History2){
        if (item.dose ==0){
            item.dose = 0
        }  else {
            if (item.dose != 0) item.dose = (item.dose - 1) else item.dose = 0
        }
    }

    fun add(item: History2){
        if (item.dose == 0){
            item.dose = 1
        }  else {
            if (item.dose != 0) item.dose = (item.dose + 1) else item.dose = 1
        }
    }

    fun deleteDrug(item: History2){
        loadDrugHistoryResult.value = drugHistory.value?.filter { it.drugId!= item.drugId }
    }


}