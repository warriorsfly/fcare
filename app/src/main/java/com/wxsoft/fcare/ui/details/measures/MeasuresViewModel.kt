package com.wxsoft.fcare.ui.details.measures

import com.google.gson.Gson
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.CheckBodyApi
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class MeasuresViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                            private val checkBodyApi: CheckBodyApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gson: Gson) : BaseViewModel(sharedPreferenceStorage,gson) {
    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

}