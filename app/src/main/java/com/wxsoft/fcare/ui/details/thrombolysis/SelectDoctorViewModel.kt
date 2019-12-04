package com.wxsoft.fcare.ui.details.thrombolysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.TaskApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SelectDoctorViewModel @Inject constructor(private val doctorApi:TaskApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon){

    val doctors:LiveData<List<User>>

    var patientId=""
    set(value) {
        field=value
        loadDocs(field)

    }
    private val loadConsultantDoctors=MediatorLiveData<Response<List<User>>>()

    val selected=MediatorLiveData<Int>()

    init {
        doctors=loadConsultantDoctors.map { it.result?: emptyList()}
    }

    private fun loadDocs(id:String){
        fun doDoctor2(response: Response<List<User>>){
            loadConsultantDoctors.value=response
        }

        disposable.add(doctorApi.getEmergencyNurse(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doDoctor2,::error))
    }

    fun change(flag:Int){
        selected.value=flag
    }

    fun showDocs(name: String): Boolean {
        if(name.isNullOrEmpty()) {
            loadDocs(patientId)
        } else {
            disposable.add(
                doctorApi.searchUser(name, true, account.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::doSearch, ::error)
            )
        }
        return true
    }

    private fun doSearch(response: Response<List<User>>){
        loadConsultantDoctors.value=response
    }
}