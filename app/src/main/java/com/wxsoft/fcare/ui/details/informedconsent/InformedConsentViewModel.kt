package com.wxsoft.fcare.ui.details.informedconsent

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Talk
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.InformedApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.core.utils.map
import javax.inject.Inject

class InformedConsentViewModel @Inject constructor(private val informedApi: InformedApi,
                                                   override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                   override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter {

    override var title = "知情同意书"
    override val clickableTitle: String
        get() = "新增"

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    override val clickable: LiveData<Boolean>
    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }

    val talk:LiveData<Talk>
    private  val initTalk = MediatorLiveData<Talk>()

    val addInformedConsent:LiveData<Boolean>
    private val addInitInformedConsent = MediatorLiveData<Boolean>()

    val talkRecords:LiveData<List<Talk>>
    private val loadTalkRecordsResult = MediatorLiveData<Resource<Response<List<Talk>>>>()

    val informeds:LiveData<List<InformedConsent>>
    private val loadInformedsResult = MediatorLiveData<Resource<Response<List<InformedConsent>>>>()


    init {
        talk = initTalk.map { it }

        talkRecords = loadTalkRecordsResult.map { (it as? Resource.Success)?.data?.result?: emptyList() }
        informeds = loadInformedsResult.map { (it as? Resource.Success)?.data?.result?: emptyList() }

        clickable=clickResult.map { it }
        addInformedConsent = addInitInformedConsent.map { it }
    }


    fun getTalkRecords(id:String){
        informedApi.getTalkRecords(id).toResource()
            .subscribe {
                loadTalkRecordsResult.value = it
            }
    }

    fun getInformedConsents(){
        informedApi.getInformedConsents().toResource()
            .subscribe {
                loadInformedsResult.value = it
            }
    }

    override fun click() {
        addInitInformedConsent.value = true
    }

    fun seeDetails(item:Talk){
        initTalk.value = item
    }

}