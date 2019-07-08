package com.wxsoft.fcare.ui.details.medicalhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.MedicalHistory
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
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

    val uploading:LiveData<Boolean>
    val savePatientResult =MediatorLiveData<Boolean>()

    val bitmaps= mutableListOf<String>()


    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()


    val medicalHistory:LiveData<MedicalHistory>
    val loadMedicalHistoryResult = MediatorLiveData<Response<MedicalHistory>>()

    val drugHistory:LiveData<List<DrugRecord>>
    val loadDrugHistoryResult = MediatorLiveData<List<DrugRecord>>()

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

        medicalHistory = loadMedicalHistoryResult.map { it.result?: MedicalHistory(account.id) }
        drugHistory = loadDrugHistoryResult.map { it.apply {
            forEach{item->
                item.doseString=item.dose.toString()
                item.selected = !item.id.isNullOrEmpty()
            }
        }?: emptyList() }
        uploading = savePatientResult.map { it }
    }


    private fun loadData(){
        savePatientResult.value= true
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


                savePatientResult.value= false
                loadProviderItemsResult.value = it.first.second
                loadHistoryItemsResult.value=it.first.first
                loadMedicalHistoryResult.value=it.second
                medicalHistory.value?.getPastHistorys()
                loadDrugHistoryResult.value = medicalHistory.value?.drugRecords

            })
    }

    private fun saveMedicalHistory(fs:List<File>) {
        savePatientResult.value= true
        medicalHistory.value?.also {history->
            if (saveAble) {
                if(history.provide.isNullOrEmpty()){
                    messageAction.value=Event("提供人信息不能为空")
                    return;
                }
                saveAble = false

                val files = fs.map {
                    return@map MultipartBody.Part.createFormData(
                        "images",
                        it.path.split("/").last(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), it)
                    )
                }
                history.drugRecords = drugHistory.value!!
                if (files.isNullOrEmpty()) {
                    medicalHistoryApi.save(history).toResource().subscribe {
                        savePatientResult.value= false
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
                        savePatientResult.value= false
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

    fun submit(fs:List<File>){
        providerItems.value?.filter { it.checked }
            ?.map { medicalHistory.value?.provide = it.id }
        historyItems.value?.filter { it.checked }
            ?.map { medicalHistory.value?.ph = it.id }
        medicalHistory.value?.patientId = patientId

        saveMedicalHistory(fs)
    }


    fun clickItems(line:String){
        loadmonitorClick.value = line
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
        loadDrugHistoryResult.value = drugHistory.value?.filter { it.drugId!= item.drugId }
    }

    fun deleteImage(url:String){
        medicalHistory.value?.attachments?.firstOrNull { it.httpUrl==url }?.let {
            medicalHistoryApi.deleteImage(it.id)
                .flatMap { medicalHistoryApi.loadMedicalHistory(patientId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doSavedMed,::error)
        }
    }
    private fun doSavedMed(response: Response<MedicalHistory>){
        loadMedicalHistoryResult.value = response
        medicalHistory.value?.getPastHistorys()
        loadDrugHistoryResult.value = medicalHistory.value?.drugRecords
    }

}