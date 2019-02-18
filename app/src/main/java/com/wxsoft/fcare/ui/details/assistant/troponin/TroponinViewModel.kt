package com.wxsoft.fcare.ui.details.assistant.troponin

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.lis.LisCr
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.LISApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.core.utils.map
import javax.inject.Inject

class TroponinViewModel @Inject constructor(private val lisApi: LISApi,
                                            private var dictEnumApi:DictEnumApi,
                                           override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) {


    val lisCr:LiveData<LisCr>
    private val loadLisCrResult = MediatorLiveData<Resource<Response<LisCr>>>()

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

        loadTroponin()
    }

    fun getCrById(id:String){
        if (id.isEmpty()){
            loadLisCrResult.value = null
            return
        }
        lisApi.getCrById(id).toResource()
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
        lisApi.saveCr(lisCr.value!!).toResource()
            .subscribe {
                loadClickEdit.value = "success"
            }
    }


    fun clickTime(id:String){
        loadClickEdit.value = id
    }

}