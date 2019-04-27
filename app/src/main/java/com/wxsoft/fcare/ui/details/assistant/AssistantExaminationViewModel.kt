package com.wxsoft.fcare.ui.details.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.lis.LisRecord
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.LISApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class AssistantExaminationViewModel @Inject constructor(private val lisApi: LISApi,
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
        }

    val lisRecords: LiveData<List<LisRecord>>
    private val loadLisRecordsResult = MediatorLiveData<List<LisRecord>>()


    init {
        lisRecords  = loadLisRecordsResult.map { it?: emptyList()}
    }


    fun loadLisRecords(){

    }




}