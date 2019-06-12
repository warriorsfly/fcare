package com.wxsoft.fcare.ui.details.informedconsent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Talk
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.InformedApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class InformedConsentViewModel @Inject constructor(private val informedApi: InformedApi,
                                                   override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                   override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {


    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }



    val talk:LiveData<Talk>
    private  val initTalk = MediatorLiveData<Talk>()

    val addInformedConsent:LiveData<Boolean>
    private val addInitInformedConsent = MediatorLiveData<Boolean>()

    val talkRecords:LiveData<List<Talk>>
    private val loadTalkRecordsResult = MediatorLiveData<Response<List<Talk>>>()

    val informeds:LiveData<List<InformedConsent>>
    private val loadInformedsResult = MediatorLiveData<Resource<Response<List<InformedConsent>>>>()


    init {
        talk = initTalk.map { it }

        talkRecords = loadTalkRecordsResult.map { it?.result?: emptyList() }
        informeds = loadInformedsResult.map { (it as? Resource.Success)?.data?.result?: emptyList() }

        addInformedConsent = addInitInformedConsent.map { it }
    }


    fun getTalkRecords(id:String){
        disposable.add(informedApi.getTalkRecords(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadTalkRecords,::error))
    }

    fun loadTalkRecords(response:Response<List<Talk>>){
        loadTalkRecordsResult.value = response
    }

    fun getInformedConsents(){
        disposable.add(informedApi.getInformedConsents().toResource()
            .subscribe( {
                loadInformedsResult.value = it
            },::error))
    }

    fun delete (id:String) {
       disposable.add( informedApi.delete(id).flatMap {
            informedApi.getTalkRecords(patientId)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                loadTalkRecordsResult.value = it
            },::error))

    }

    fun click() {
        addInitInformedConsent.value = true
    }

    fun seeDetails(item:Talk){
        initTalk.value = item
    }

}