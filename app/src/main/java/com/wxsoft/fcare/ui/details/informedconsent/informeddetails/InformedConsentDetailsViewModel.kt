package com.wxsoft.fcare.ui.details.informedconsent.informeddetails

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

class InformedConsentDetailsViewModel @Inject constructor(private val informedApi: InformedApi,
                                                          override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                          override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter {

    override var title: String=""
        get() = "知情同意书详情"
    override val clickableTitle: String
        get() = ""

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }


    val talk:LiveData<Talk>
    private val loadTalkResult = MediatorLiveData<Resource<Response<Talk>>>()

    val informed:LiveData<InformedConsent>
    private val loadInformedResult = MediatorLiveData<Resource<Response<InformedConsent>>>()

    override val clickable: LiveData<Boolean>
    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }

    init {
        clickable = clickResult.map { it }
        talk = loadTalkResult.map { (it as? Resource.Success)?.data?.result?: Talk("") }
        informed = loadInformedResult.map { (it as? Resource.Success)?.data?.result?: InformedConsent("") }
    }

    fun getTalkById(id:String){
        informedApi.getTalkById(id).toResource()
            .subscribe {
                loadTalkResult.value = it
            }
    }

    fun getInformedConsentById(id:String){
        informedApi.getInformedConsentById(id).toResource()
            .subscribe {
                loadInformedResult.value = it
            }
    }



    override fun click() {

    }

}