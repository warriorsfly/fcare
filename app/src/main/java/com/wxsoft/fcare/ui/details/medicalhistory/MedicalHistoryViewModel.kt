package com.wxsoft.fcare.ui.details.medicalhistory

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.MedicalHistory
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.FileApi
import com.wxsoft.fcare.core.data.remote.MedicalHistoryApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.map
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class MedicalHistoryViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                                  private val medicalHistoryApi: MedicalHistoryApi,
                                                  private val fileApi: FileApi,
                                                  override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                  override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title: String=""
        get() = "病史"
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
        }

    var saveAble=true

    val bitmaps= mutableListOf<String>()


    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()


    val medicalHistory:LiveData<MedicalHistory>
    private val loadMedicalHistoryResult = MediatorLiveData<Resource<Response<MedicalHistory>>>()

    val historyPhoto:LiveData<String>
    val loadPhoto = MediatorLiveData<String>()

    val providerItems: LiveData<List<Dictionary>>
    private val loadProviderItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val historyItems: LiveData<List<Dictionary>>
    private val loadHistoryItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    init {

        backToLast = initbackToLast.map { it }
        clickable = clickResult.map { it }
        historyPhoto = loadPhoto.map { it }
        providerItems = loadProviderItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }
        historyItems = loadHistoryItemsResult.map { (it as? Resource.Success)?.data?: emptyList() }

        medicalHistory = loadMedicalHistoryResult.map { (it as? Resource.Success)?.data?.result?: MedicalHistory("") }

        loadProviderItems()
        loadHistoryItems()
    }

    fun loadProviderItems() {
        dicEnumApi.loadMedicalHistoryProviderItems().toResource()
            .subscribe{
                loadProviderItemsResult.value = it
                if (medicalHistory.value?.id.isNullOrEmpty()){
                    providerItems.value?.first()?.checked = true
                }else{
                    providerItems.value?.filter { it.checked }?.map {it.checked = false }
                    providerItems.value?.filter { it.id.equals(medicalHistory.value?.provide) }?.map {it.checked = true }
                }

            }
    }

    fun loadHistoryItems() {
        dicEnumApi.loadMedicalHistoryItems().toResource()
            .subscribe{
                loadHistoryItemsResult.value = it
                historyItems.value?.filter { it.id.equals(medicalHistory.value?.ph) }?.map {it.checked = true }

            }
    }

    fun loadMedicalHistory(){
        medicalHistoryApi.loadMedicalHistory(patientId).toResource()
            .subscribe{
                loadMedicalHistoryResult.value = it
                haveData()
            }
    }

    fun saveMedicalHistory(){
        if (saveAble){
            saveAble = false
            val files = bitmaps.map {
                val file = File(it)
                return@map MultipartBody.Part.createFormData(
                    "images",
                    it.split("/").last(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                )
            }
            if (files.isNullOrEmpty()){
                medicalHistoryApi.save(medicalHistory.value!!).toResource().subscribe {

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
            }else{
                medicalHistoryApi.save(medicalHistory.value!!, files).toResource().subscribe {
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


    fun haveData(){
        if (medicalHistory.value?.id.isNullOrEmpty()){
            providerItems.value?.first()?.checked = true
        }else{
            providerItems.value?.filter { it.checked }?.map {it.checked = false }
            providerItems.value?.filter { it.id.equals(medicalHistory.value?.provide) }?.map {it.checked = true }
            historyItems.value?.filter { it.id.equals(medicalHistory.value?.ph) }?.map {it.checked = true }
        }

    }



    fun clickSelect(item:Dictionary){
        when(item.section){
            1->{ historyItems.value?.filter { it.checked }?.map {it.checked = false } }
            2->{ providerItems.value?.filter { it.checked }?.map {it.checked = false } }
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