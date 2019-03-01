package com.wxsoft.fcare.ui.details.pharmacy.selectdrugs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PharmacyApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class SelectDrugsViewModel @Inject constructor(private val pharmacyApi: PharmacyApi,
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

    val clikSomething: LiveData<String>
    private val initClikSomething = MediatorLiveData<String>()

    val clickable: LiveData<Boolean>
    private val initClickable = MediatorLiveData<Boolean>()



    val selectNum: LiveData<String>
    private val initselectNum = MediatorLiveData<String>()


    init {
        clikSomething = initClikSomething.map { it }
        selectNum = initselectNum.map { it }
        clickable = initClickable.map { it }
    }



    fun click(){

    }

}