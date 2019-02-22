package com.wxsoft.fcare.ui.details.medicalhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.MedicalHistory
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.previoushistory.History1
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.MedicalHistoryApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
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
                                                  override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title = "病史"
    override val clickableTitle: String
        get() = "保存"
    override val clickable:LiveData<Boolean>

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
            loadData()
        }

    private var saveAble=true

    val bitmaps= mutableListOf<String>()


    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()


    val medicalHistory:LiveData<MedicalHistory>
    private val loadMedicalHistoryResult = MediatorLiveData<Response<MedicalHistory>>()

    val historyPhoto:LiveData<String>
    private val loadPhoto = MediatorLiveData<String>()

    val providerItems: LiveData<List<Dictionary>>
    private val loadProviderItemsResult = MediatorLiveData<List<Dictionary>>()

    val historyItems: LiveData<List<Dictionary>>
    private val loadHistoryItemsResult = MediatorLiveData<List<Dictionary>>()

    init {

        backToLast = initbackToLast.map { it }
        clickable = clickResult.map { it }
        historyPhoto = loadPhoto.map { it }
        providerItems = loadProviderItemsResult.map { it }
        historyItems = loadHistoryItemsResult.map { it }

        medicalHistory = loadMedicalHistoryResult.map { it.result?: MedicalHistory("") }

    }


    private fun loadData(){
        dicEnumApi.loadMedicalHistoryItems(patientId)
            .zipWith(dicEnumApi.loadMedicalHistoryProviderItems())
            .zipWith(medicalHistoryApi.loadMedicalHistory(patientId))
            .subscribeOn(Schedulers.computation())
            .doOnSuccess { zip ->

                if(zip.second.result?.id.isNullOrEmpty()){
                    zip.first.second.firstOrNull()?.checked=true
                }else{

                    zip.first.second.filter { it.id == zip.second.result?.provide }?.map {it.checked = true }

                    zip.first.first?.filter {  item-> zip.second.result?.pastHistorys?.any { it.phCode==item.id }?:false }?.map {it.checked = true }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

                loadProviderItemsResult.value = it.first.second
                loadHistoryItemsResult.value=it.first.first
                loadMedicalHistoryResult.value=it.second
            }
    }
//    private fun loadProviderItems() {
//        dicEnumApi.loadMedicalHistoryProviderItems().toResource()
//            .subscribe{
//                if (medicalHistory.value?.id.isNullOrEmpty()){
//                    providerItems.value?.first()?.checked = true
//                }else{
//                    providerItems.value?.filter { it.checked }?.map {it.checked = false }
//                    providerItems.value?.filter { it.id == medicalHistory.value?.provide }?.map {it.checked = true }
//                }
//
//            }
//    }
//
//    private fun loadHistoryItems() {
//        dicEnumApi.loadMedicalHistoryItems(patientId).toResource()
//            .subscribe{
//                loadHistoryItemsResult.value = it
//                historyItems.value?.filter { it.id == medicalHistory.value?.ph }?.map {it.checked = true }
//
//            }
//    }
//
//    private fun loadMedicalHistory(){
//        medicalHistoryApi.loadMedicalHistory(patientId).toResource()
//            .subscribe{
//                loadMedicalHistoryResult.value = it
//                haveData()
//            }
//    }

    private fun saveMedicalHistory() {

        medicalHistory.value?.also {history->

            history.pastHistorys=historyItems.value?.filter { it.checked }?.map { History1(it.id,history.id) }?: emptyList()
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


                if (files.isNullOrEmpty()) {
                    medicalHistoryApi.save(history).toResource().subscribe {

                        when (it) {
                            is Resource.Success -> {
                                clickResult.value = true
                                messageAction.value = Event("保存成功")
                                initbackToLast.value = true
                                saveAble = true
                            }
                            is Resource.Error -> {
                                clickResult.value = true
                                messageAction.value = Event(it.throwable.message ?: "")
                            }
                            else -> {
                                clickResult.value = false
                            }
                        }
                    }
                } else {
                    medicalHistoryApi.save(history, files).toResource().subscribe {
                        when (it) {
                            is Resource.Success -> {
                                clickResult.value = true
                                messageAction.value = Event("保存成功")
                                initbackToLast.value = true
                                saveAble = true
                            }
                            is Resource.Error -> {
                                clickResult.value = true
                                messageAction.value = Event(it.throwable.message ?: "")
                            }
                            else -> {
                                clickResult.value = false
                            }
                        }
                    }
                }
            }
        }
    }

    fun clickSelect(item:Dictionary){
        when(item.section){
            3->{ providerItems.value?.filter { it.checked }?.map {it.checked = false } }
        }
        item.checked = !item.checked
    }

    fun submit(){
        providerItems.value?.filter { it.checked }
            ?.map { medicalHistory.value?.provide = it.id }
        historyItems.value?.filter { it.checked }
            ?.map { medicalHistory.value?.ph = it.id }
        medicalHistory.value?.patientId = patientId
        saveMedicalHistory()
    }

    override fun click(){
        submit()
    }

}