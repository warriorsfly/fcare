package com.wxsoft.fcare.ui.detail.dialog.transfer

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.data.entity.Account
import com.wxsoft.fcare.data.entity.Response
import com.wxsoft.fcare.data.entity.Transfer
import com.wxsoft.fcare.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.data.remote.TransferApi
import com.wxsoft.fcare.data.toResource
import com.wxsoft.fcare.result.Resource
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class TransferViewModel @Inject constructor(private val transferApi: TransferApi,
                                            gson:Gson,
                                            sharedPreferenceStorage: SharedPreferenceStorage): ViewModel() {

    val account:Account
    var patientId:String=""
        set(value) {
            field=value
            loadTransfer()
        }
    val transfer:LiveData<Transfer>
    val result:LiveData<Response<String?>>

    private val loadTransferResult=MediatorLiveData<Resource<Transfer>>()
    private val saveTransferReslut=MediatorLiveData<Resource<Response<String?>>>()


    init {

        account=gson.fromJson(sharedPreferenceStorage.userInfo!!,Account::class.java)

        transfer = loadTransferResult.map {
            (it as? Resource.Success)?.data ?: Transfer("")
        }

        result = saveTransferReslut.map {
            (it as? Resource.Success)?.data ?: Response(false)
        }
    }
    fun loadTransfer(){
        transferApi.getOne(patientId).toResource()
            .subscribe {
                if (it is Resource.Success && it.data.success) {
                    loadTransferResult.value = Resource.Success(it.data.result?:Transfer(""))
                }
            }
    }

    fun saveTransfer(){
        transfer.value?.apply {
            this.patientId=this@TransferViewModel.patientId
            this.createrId=account.id
            transferApi.save(transfer = this).toResource()
                .subscribe {result-> saveTransferReslut.value=result }
        }

    }
}