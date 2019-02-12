package com.wxsoft.fcare.ui.details.reperfusion

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.CABG
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PharmacyApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.map
import javax.inject.Inject

class ReperfusionViewModel @Inject constructor(private val pharmacyApi: PharmacyApi,
                                               override val sharedPreferenceStorage: SharedPreferenceStorage,
                                               override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) ,
    ICommonPresenter {

    override var title: String=""
        get() = "CABG"
    override val clickableTitle: String
        get() = "保存"
    override val clickable: LiveData<Boolean>

    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()

    val mSelectTime:LiveData<String>
    private val initSelectTime = MediatorLiveData<String>()

    val cabg:LiveData<CABG>
    private val loadCABG = MediatorLiveData<Resource<Response<CABG>>>()

    init {
        clickable = clickResult.map { it }
        backToLast = initbackToLast.map { it }
        mSelectTime = initSelectTime.map { it }
        cabg = loadCABG.map { (it as? Resource.Success)?.data?.result ?: CABG("") }
    }

    override fun click() {
        saveCABG()
    }

    fun getCABG(){
        pharmacyApi.getCABG(patientId).toResource()
            .subscribe {
                loadCABG.value = it
            }
    }

    fun saveCABG(){
        cabg.value?.patientId = patientId
        pharmacyApi.saveCABG(cabg.value!!).toResource()
            .subscribe {
               when(it){
                   is Resource.Success ->{
                       messageAction.value= Event(it.data.msg)
                       initbackToLast.value = true
                   }
                   is Resource.Error->{
                       messageAction.value= Event(it.throwable.message?:"")
                   }
               }
            }
    }



    fun selectTime(type:String){
        initSelectTime.value = type
    }



}