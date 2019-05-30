package com.wxsoft.fcare.ui.patient.choice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChoicePatientViewModel @Inject constructor(
    private val patientApi: PatientApi,
    override val sharedPreferenceStorage: SharedPreferenceStorage,
    override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {

    val choiceSome:LiveData<String>
    private val initChoiceSome = MediatorLiveData<String>()

    val patientlist: LiveData<List<Patient>>
    private val loadPatientlist = MediatorLiveData<List<Patient>>()


    init {
        patientlist = loadPatientlist.map { it?: emptyList() }
        choiceSome = initChoiceSome.map { it }
    }


    fun getPatientsFromCis(){
        disposable.add(
            patientApi.getPatientsFromCis()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe ({
                    loadPatientlist.value = it.result
                },{

                })
        )
    }

    fun choicePatient(item:Patient){
        patientlist.value?.filter { it.checked }?.map { it.checked = false }
        item.checked = true
        initChoiceSome.value = "choicePatient"
    }


}