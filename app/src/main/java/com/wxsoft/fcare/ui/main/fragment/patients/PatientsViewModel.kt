package com.wxsoft.emergency.ui.main.fragment.patients

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PatientsViewModel @Inject constructor(private val api: PatientApi,
                                            override val sharedPreferenceStorage: SharedPreferenceStorage,
                                            override val gon: Gson
):  BaseViewModel(sharedPreferenceStorage,gon) {

    val isLoading: LiveData<Boolean>
    val patients: LiveData<List<Patient>>
    private val loadPatientsResult= MediatorLiveData<Resource<Response<List<Patient>>>>()

    init {

        load()
        isLoading=loadPatientsResult.map { it== Resource.Loading }

        patients = loadPatientsResult.map {(it as? Resource.Success)?.data?.result?: emptyList()}

    }

    fun onSwipeRefresh() {
        load()
    }

    private fun load() {

        api.patients().toResource()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { loadPatientsResult.value = it }
    }



}