package com.wxsoft.fcare.ui.details.pharmacy

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Pharmacy
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.PharmacyApi
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class PharmacyViewModel @Inject constructor(private val dicEnumApi: DictEnumApi,
                                            private val pharmacyApi: PharmacyApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override val title: String
        get() = "用药"
    override val clickableTitle: String
        get() = "保存"
    override val clickable: LiveData<Boolean>

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



    val pharmacy:LiveData<Pharmacy>
    private val initPharmacy = MediatorLiveData<Resource<Response<Pharmacy>>>()


    init {
        clickable = clickResult.map { it }
        pharmacy = initPharmacy.map { (it as? Resource.Success)?.data?.result ?: Pharmacy("") }
    }

    override fun click(){

    }


    fun selectBag(){

    }

}