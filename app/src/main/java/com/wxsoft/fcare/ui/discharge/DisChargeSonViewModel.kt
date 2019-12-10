package com.wxsoft.fcare.ui.discharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.DisChargeDiagnosis
import com.wxsoft.fcare.core.data.entity.DisChargeEntity
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DischargeApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DisChargeSonViewModel @Inject constructor(private val api: DischargeApi,
                                                override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                override val gon: Gson) : BaseViewModel(sharedPreferenceStorage,gon)  {

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }
    var dicId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadDics(value)
        }

    val des:LiveData<List<DisChargeEntity>>
    private val loadDesResult = MediatorLiveData<List<DisChargeEntity>>()
    val saveResult:LiveData<Boolean>
    private val loadSaveResult = MediatorLiveData<Boolean>()

    init {
        des = loadDesResult.map { it ?: emptyList() }
        saveResult = loadSaveResult.map { it }
    }


    private fun loadDics(id:String){
        disposable.add(api.getDics(patientId,id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadOtDiagnosis,::error))
    }

    private fun loadOtDiagnosis(response: Response<List<DisChargeEntity>>){
        loadDesResult.value=response.result?.map { it.apply { patientId = this@DisChargeSonViewModel.patientId } }
    }

    override fun error(throwable: Throwable){
        messageAction.value= Event(throwable.message?:"未知错误")
    }


    fun save(){
        disposable.add(api.saveChooseItems(des.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::saveResult,::error))
    }
    private fun saveResult(response: Response<Boolean>){
        loadSaveResult.value = response.result
    }

}