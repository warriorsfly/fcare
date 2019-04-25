package com.wxsoft.fcare.ui.hardwaredata

import com.google.gson.Gson
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.HardwareApi
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class HardwareDataViewModel @Inject constructor(private val hardwareApi: HardwareApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon)  {


    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value

        }

    init {

    }

}