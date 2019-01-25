package com.wxsoft.fcare.ui.details.informedconsent.addinformed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.graphics.Bitmap
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Talk
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.InformedApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.ui.ICommonPresenter
import com.wxsoft.fcare.utils.map
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class AddInformedConsentViewModel @Inject constructor(private val informedApi: InformedApi,
                                                      override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                      override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon), ICommonPresenter {

    override var title: String=""
        get() = titleName
    override val clickableTitle: String
        get() = "保存"

    /**
     * 病人id
     */
    var saveAble=true

    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()


    var titleName: String = ""
        set(value) {
            field = value
        }

    var voicePath: String = ""
        set(value) {
            field = value
        }

    var patientsId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadTalk()
        }

    var informedContenId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }
    var informedname: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    val bitmaps= mutableListOf<String>()

    val photos= ArrayList<Bitmap>()

    var checked:Boolean = false

    val voiceStart:LiveData<Boolean>
    private val initVoiceStart = MediatorLiveData<Boolean>()

    val showVoiceTime:LiveData<Boolean>
    val initShowVoiceTime = MediatorLiveData<Boolean>()

    override val clickable: LiveData<Boolean>
    private val clickResult  = MediatorLiveData<Boolean>().apply {
        value=true
    }
    val informedConsent:LiveData<InformedConsent>
    private val initInformedConsent = MediatorLiveData<Resource<InformedConsent>>()

    val talk:LiveData<Talk>
    private val initTalk = MediatorLiveData<Resource<Response<Talk>>>()

    val talkResultId:LiveData<String>
    val saveTalkResult =MediatorLiveData<Resource<Response<String>>>()

    init {
        talkResultId = saveTalkResult.map { (it as? Resource.Success)?.data?.result?: ""}
        backToLast = initbackToLast.map { it }
        voiceStart = initVoiceStart.map { it }
        showVoiceTime = initShowVoiceTime.map { it }
        clickable=clickResult.map { it }
        informedConsent = initInformedConsent.map { (it as? Resource.Success)?.data?: InformedConsent("") }
        talk = initTalk.map { (it as? Resource.Success)?.data?.result?: Talk("") }
    }

    private fun loadTalk(){
        initTalk.value=Resource.Success(Response<Talk>(true).apply {
            this.result= Talk("")
        })
    }


    override fun click() {

        if (saveAble){
            saveAble = false
            if (voicePath.isNotEmpty()) bitmaps.add(voicePath)
            val files = bitmaps.map {
                val file = File(it)
                return@map MultipartBody.Part.createFormData(
                    "images",
                    it.split("/").last(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
                )
            }
            if (files.isNullOrEmpty()){
                informedApi.save(talk.value!!.apply {
                    patientId = patientsId
                    informedConsentId = informedContenId
                    informedConsentName = informedname
                }).toResource().subscribe {

                    when (it) {
                        is Resource.Success -> {
                            clickResult.value = true
                            saveTalkResult.value = it
                            messageAction.value = Event("保存成功")
                            initbackToLast.value = true
                            saveAble = true
                        }
                        is Resource.Error -> {
                            clickResult.value = true
                            messageAction.value = Event(it.throwable.message ?: "")
                        }
                        else -> {
                            clickResult.value = false
                        }
                    }
                }
            }else{
                informedApi.save(talk.value!!.apply {
                    patientId = patientsId
                    informedConsentId = informedContenId
                    informedConsentName = informedname
                }, files).toResource().subscribe {
                    when (it) {
                        is Resource.Success -> {
                            clickResult.value = true
                            saveTalkResult.value = it
                            messageAction.value = Event("保存成功")
                            initbackToLast.value = true
                            saveAble = true
                        }
                        is Resource.Error -> {
                            clickResult.value = true
                            messageAction.value = Event(it.throwable.message ?: "")
                        }
                        else -> {
                            clickResult.value = false
                        }
                    }
                }
            }
        }


    }

    fun showOrVoiceTime(){
        initShowVoiceTime.value = true
    }

    fun clickVoice(){
        checked = !checked
        initVoiceStart.value = checked
    }

}