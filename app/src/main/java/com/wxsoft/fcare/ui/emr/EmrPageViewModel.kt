package com.wxsoft.fcare.ui.emr

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class EmrPageViewModel @Inject constructor(override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    val showEmr:LiveData<Boolean>
    val showEmrResult=MediatorLiveData<Boolean>().apply {
        value=true
    }
    init {

        showEmr=showEmrResult.map { it }
    }

    fun changeTitle(){
        val isEmr=showEmr.value?:true
        showEmrResult.value= !isEmr
    }

}