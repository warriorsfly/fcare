package com.wxsoft.fcare.ui.details.informedconsent.addinformed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.graphics.Bitmap
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class AddInformedConsentViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                                      override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                      override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter {

    override val title: String
        get() = titleName
    override val clickableTitle: String
        get() = "保存"

    /**
     * 病人id
     */

    var titleName: String = ""
        set(value) {
            field = value
        }

    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }
    val bitmaps= mutableListOf<String>()

    val photos= ArrayList<Bitmap>()

    override val clickable: LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }
    val informedConsent:LiveData<InformedConsent>
    private val initInformedConsent = MediatorLiveData<Resource<InformedConsent>>()

    init {
        clickable=clickResult.map { it }
        informedConsent = initInformedConsent.map { (it as? Resource.Success)?.data?: InformedConsent("") }
    }




    override fun click() {

    }

}