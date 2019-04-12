package com.wxsoft.fcare.ui.details.diagnose.diagnosenew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DiagnoseApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class DiagnoseNewViewModel @Inject constructor(private val diagnoseApi: DiagnoseApi,
                                               private val dictEnumApi: DictEnumApi,
                                               override val sharedPreferenceStorage: SharedPreferenceStorage,
                                               override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    val diagnosis: LiveData<Diagnosis>
    val loadDiagnosis = MediatorLiveData<Diagnosis>()

    init {
        diagnosis = loadDiagnosis.map { it?:Diagnosis(createrId = account.id,createrName = account.trueName) }
        loadDiagnosis.value = null
    }




}