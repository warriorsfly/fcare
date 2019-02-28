package com.wxsoft.fcare.ui.details.pharmacy.drugrecords

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PharmacyApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import javax.inject.Inject

class DrugRecordsViewModel  @Inject constructor(private val pharmacyApi: PharmacyApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title = "用药"
    override val clickableTitle: String
        get() = "添加"
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

    val clikSomething:LiveData<String>
    private val initClikSomething = MediatorLiveData<String>()


    init {
        clickable = clickResult.map { it }
        clikSomething = initClikSomething.map { it }
    }

    override fun click(){
        initClikSomething.value = "add"
    }

}