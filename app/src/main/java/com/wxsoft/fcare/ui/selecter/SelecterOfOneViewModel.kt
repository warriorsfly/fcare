package com.wxsoft.fcare.ui.selecter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class SelecterOfOneViewModel @Inject constructor(private val enumApi: DictEnumApi,
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
            loadDisChargeInfo()
        }


    var typeId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    val des: LiveData<List<Dictionary>>
    private val loadDesResult = MediatorLiveData<List<Dictionary>>()


    init {
        des = loadDesResult.map { it ?: emptyList()  }
    }

    private fun loadDisChargeInfo(){


    }



    fun clickSelect(item: Dictionary){

    }

}