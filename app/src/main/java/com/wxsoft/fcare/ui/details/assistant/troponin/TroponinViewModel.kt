package com.wxsoft.fcare.ui.details.assistant.troponin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.lis.LisCr
import com.wxsoft.fcare.core.data.entity.lis.LisRecord
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.LISApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.core.utils.map
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class TroponinViewModel @Inject constructor(private val lisApi: LISApi,
                                            private var dictEnumApi:DictEnumApi,
                                           override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) {

    val lisCr:LiveData<LisCr>
    private val loadLisCrResult = MediatorLiveData<Resource<Response<LisCr>>>()

    private var saveAble=true
    val bitmaps= mutableListOf<String>()
    val uploading:LiveData<Boolean>
    val savePatientResult =MediatorLiveData<Boolean>()

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    /**
     * 肌酐蛋白单位
     */
    val troponinUnitsItems: LiveData<List<String>>
    private lateinit var troponinUnits: List<Dictionary>
    /**
     *获取肌酐蛋白单位字典
     */
    private val loadTroponinDictEnumResult = MediatorLiveData<Resource<List<Dictionary>>>()

    val clickEdit:LiveData<String>
    private val loadClickEdit = MediatorLiveData<String>()


    init {
        clickEdit = loadClickEdit.map { it }
        lisCr = loadLisCrResult.map { (it as? Resource.Success)?.data?.result?: LisCr("") }
        troponinUnitsItems= loadTroponinDictEnumResult.map {
            val cos=(it as? Resource.Success)?.data?: emptyList()
            troponinUnits=cos
            return@map troponinUnits.map { item -> item.itemName }
        }
        uploading = savePatientResult.map { it }
//        loadTroponin()
    }

    fun getCrById(id:String){
        if (id.isEmpty()){
            loadLisCrResult.value = null
            return
        }
        lisApi.getPoct(id).toResource()
            .subscribe {
                loadLisCrResult.value = it
                lisCr.value?.setUpChecked()
            }
    }

    /**
     * 获取肌酐蛋白单位字典信息
     */
    private fun loadTroponin() {
        dictEnumApi.loadTroponinUnit().toResource()
            .subscribe {
                loadTroponinDictEnumResult.value = it
            }
    }

    fun submit(){
        if (lisCr.value == null) return
        lisCr.value!!.patientId = patientId
        savePatientResult.value= true
        lisCr.value?.also {lisCr->
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
                    lisApi.savePoct(lisCr).toResource().subscribe {
                        savePatientResult.value= false
                        when (it) {
                            is Resource.Success -> {
//                                clickResult.value = true
                                messageAction.value = Event("保存成功")
                                saveAble = true
                                loadClickEdit.value = "success"
                            }
                            is Resource.Error -> {
//                                clickResult.value = true
                                messageAction.value = Event(it.throwable.message ?: "")
                            }
                        }
                    }
                } else {
                    lisApi.savePoct(lisCr, files).toResource().subscribe {
                        savePatientResult.value= false
                        when (it) {
                            is Resource.Success -> {
//                                clickResult.value = true
                                messageAction.value = Event("保存成功")
                                saveAble = true
                                loadClickEdit.value = "success"
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

    fun clickTime(id:String){
        loadClickEdit.value = id
    }

}