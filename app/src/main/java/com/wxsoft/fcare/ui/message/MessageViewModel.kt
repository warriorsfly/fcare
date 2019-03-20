package com.wxsoft.fcare.ui.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.CustomMessage
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.core.utils.DateTimeUtils
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MessageViewModel @Inject constructor(private val patientApi: PatientApi,
                                           override val sharedPreferenceStorage: SharedPreferenceStorage,
                                           override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    /**
     * 病人id
     */
    var extra: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadPatient()
        }

    var model: CustomMessage = CustomMessage("","","","","","","",0)


    val message:LiveData<String>
    private val _message=MediatorLiveData<String>()

    /**
     * 病人信息
     */
    val patient:LiveData<Patient>
    private val loadPatientResult=MediatorLiveData<Response<Patient>>()


    init {
        message=_message.map { it }
        patient=loadPatientResult.map { it.result?: Patient() }
    }

    private fun loadPatient(){
        model = gon.fromJson(extra, CustomMessage::class.java)

        disposable.add(patientApi.getOne(model.PatientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::doPatient,::error))
    }


    private fun doPatient(response:Response<Patient>){
        loadPatientResult.value=response
    }

    fun closePage(){
        _message.value = "close"
    }

    fun areadyRead(){
        disposable.add(patientApi.areadyRead(model.MessageId,account.id,account.trueName,patient.value!!.name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::haveRead,::error))
    }

    private fun haveRead(response:Response<String>){
        _message.value = "Success"
    }
}