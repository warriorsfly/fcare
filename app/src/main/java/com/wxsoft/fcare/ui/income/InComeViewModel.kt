package com.wxsoft.fcare.ui.income

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.data.entity.Account
import com.wxsoft.fcare.data.entity.Patient
import com.wxsoft.fcare.data.entity.Response
import com.wxsoft.fcare.data.entity.Transfer
import com.wxsoft.fcare.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.data.remote.PatientApi
import com.wxsoft.fcare.data.remote.TransferApi
import com.wxsoft.fcare.data.toResource
import com.wxsoft.fcare.result.Event
import com.wxsoft.fcare.result.Resource
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class InComeViewModel @Inject constructor(private val transferApi: TransferApi,
                                          private val patientApi:PatientApi,
                                          gson: Gson,
                                          sharedPreferenceStorage: SharedPreferenceStorage
) :ViewModel() {

    val patient:LiveData<Patient>



    val account: Account
    var patientId:String=""
        set(value) {
            field=value
            if(patientId.isEmpty()){

                loadPatientResult.value=Resource.Loading
                loadTransferResult.value=Resource.Loading
            }else {

                loadPatientDetail()
                loadTransfer()
            }
        }
    val transfer:LiveData<Transfer>
    val navigateToErrorAction: LiveData<Event<String>>
        get() = _errorToOperationAction


    private val loadPatientResult=MediatorLiveData<Resource<Response<Patient>>>()
    private val loadTransferResult=MediatorLiveData<Resource<Response<Transfer>>>()
    private val _errorToOperationAction = MediatorLiveData<Event<String>>()



    init {
        account=gson.fromJson(sharedPreferenceStorage.userInfo!!,Account::class.java)

        patient=loadPatientResult.map {
            (it as? Resource.Success)?.data?.result?:Patient("")
        }

        transfer = loadTransferResult.map {
            (it as? Resource.Success)?.data ?.result?: Transfer("")
        }

    }

    private fun loadPatientDetail() {
        patientApi.getOne(patientId).toResource().doOnSuccess { loadPatientResult.value=it }
    }

    private fun loadTransfer(){
        transferApi.getOne(patientId).toResource().subscribe{loadTransferResult.value=it}
    }


    fun save(){
//        when{
//            patient.value?.helped?:false->{}
//        }

        patient.value?.let {p->
            patientApi.save(p).toResource().subscribe {
                patientId = (it as? Resource.Success)?.data ?: ""
                if (it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }

            }
        }

        transfer.value?.let {t->
            transferApi.save(t).toResource().subscribe {
                if (it is Resource.Error) {
                    _errorToOperationAction.value = Event(it.throwable.message ?: "错误")
                }
            }

        }
    }

}