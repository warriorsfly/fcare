package com.wxsoft.fcare.ui.details.informedconsent

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class InformedConsentViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                                   override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                   override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter {

    override val title: String
        get() = "知情谈话"
    override val clickableTitle: String
        get() = ""

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    override val clickable: LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }

    init {
        clickable=clickResult.map { it }

    }


    override fun click() {


    }




}