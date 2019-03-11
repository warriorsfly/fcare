package com.wxsoft.fcare.ui.main.fragment.patients.searchpatients

import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.InterventionApi
import com.wxsoft.fcare.ui.BaseViewModel
import javax.inject.Inject

class SearchPatientsViewModel  @Inject constructor(private val interventionApi: InterventionApi,
                                                   override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                   override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    val searchText = MediatorLiveData<String>()

    init {

    }



}