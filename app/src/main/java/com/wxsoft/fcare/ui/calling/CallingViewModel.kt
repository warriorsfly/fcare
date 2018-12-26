package com.wxsoft.fcare.ui.calling

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.fcare.data.entity.Account
import com.wxsoft.fcare.data.entity.Response
import com.wxsoft.fcare.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.data.remote.NotifyApi
import com.wxsoft.fcare.data.toResource
import com.wxsoft.fcare.data.toWxResource
import com.wxsoft.fcare.result.Resource
import com.wxsoft.fcare.utils.map
import javax.inject.Inject

class CallingViewModel @Inject constructor(
    sharedPreferenceStorage: SharedPreferenceStorage,
    private val notifyApi: NotifyApi,
    gson: Gson
):ViewModel() {

    val result:LiveData<Response<String?>>
    val  account:Account

    var patientId:String=""
    private val answerNotifyResult = MediatorLiveData<Resource<Response<String?>>>()
    init {
        result=answerNotifyResult.map{
            (it as?  Resource.Success)?.data?:Response(false)
        }

        val s=sharedPreferenceStorage.userInfo!!
        account=gson.fromJson(s,Account::class.java)
    }

    fun answer(){
        notifyApi.answer(account.id,patientId).toResource()
            .subscribe {  answerNotifyResult.value = it}
    }
}