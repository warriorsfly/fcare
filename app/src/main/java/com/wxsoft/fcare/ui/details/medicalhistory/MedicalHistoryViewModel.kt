package com.wxsoft.fcare.ui.details.medicalhistory

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.graphics.Bitmap
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
import com.wxsoft.fcare.utils.map
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class MedicalHistoryViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                                  private val medicalHistoryApi: MedicalHistoryApi,
                                                  private val fileApi: FileApi,
                                                  override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                  override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override val title: String
        get() = "IllnessHistory"
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
    val photos= ArrayList<Bitmap>()

    val medicalHistory:LiveData<MedicalHistory>
    private val loadMedicalHistoryResult = MediatorLiveData<Resource<Response<MedicalHistory>>>()

    val historyPhoto:LiveData<String>
    val loadPhoto = MediatorLiveData<String>()

    val providerItems: LiveData<List<Dictionary>>
    private val loadProviderItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val historyItems: LiveData<List<Dictionary>>
    private val loadHistoryItemsResult = MediatorLiveData<Resource<List<Dictionary>>>()

    init {
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
                providerItems.value?.filter { it.itemCode.equals(medicalHistory.value?.provide) }?.map {it.checked = true }
            }
    }

    fun loadHistoryItems() {
        dicEnumApi.loadMedicalHistoryItems().toResource()
            .subscribe{
                loadHistoryItemsResult.value = it
                historyItems.value?.filter { it.itemCode.equals(medicalHistory.value?.ph) }?.map {it.checked = true }

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
        medicalHistoryApi.save(medicalHistory.value!!).toResource()
            .subscribe {
                it
            }
    }

    fun uploadFile(){
        val files = photos.map {
            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            return@map MultipartBody.Part.create(RequestBody.create(MediaType.parse("multipart/form-data"), byteArray))
        }

//        fileApi.save(files).toResource().subscribe {
//            when (it) {
//                is Resource.Success -> {
//                    messageAction.value = Event("保存成功")
//                }
//                is Resource.Error -> {
//                    messageAction.value = Event(it.throwable.message ?: "")
//                }
//                else->{
//
//                }
//            }
//        }
    }

    fun haveData(){
        providerItems.value?.filter { it.itemCode.equals(medicalHistory.value?.provide) }?.map {it.checked = true }
        historyItems.value?.filter { it.itemCode.equals(medicalHistory.value?.ph) }?.map {it.checked = true }
    }



    fun clickSelect(item:Dictionary){
        when(item.section){
            2->{ historyItems.value?.filter { it.checked }?.map {it.checked = false } }
            3->{ providerItems.value?.filter { it.checked }?.map {it.checked = false } }
        }
        item.checked = !item.checked
    }

    fun submit(){
        providerItems.value?.filter { it.checked }
            ?.map { medicalHistory.value?.provide = it.itemCode }
        historyItems.value?.filter { it.checked }
            ?.map { medicalHistory.value?.ph = it.itemCode }
        medicalHistory.value?.patientId = patientId
//        uploadFile()
        saveMedicalHistory()
    }

    override fun click(){
        submit()
    }

}