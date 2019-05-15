package com.wxsoft.fcare.ui.details.informedconsent.addinformed

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Talk
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.InformedApi
import com.wxsoft.fcare.core.data.toResource
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.result.Resource
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class AddInformedConsentViewModel @Inject constructor(private val informedApi: InformedApi,
                                                      override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                      override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon) {


    /**
     * 病人id
     */
    private var saveAble=true

    val backToLast:LiveData<Boolean>
    private val initbackToLast = MediatorLiveData<Boolean>()


    var titleName: String = ""

    var patientsId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }
    var talkId: String = ""
        set(value) {
            if (value == "") return
            field = value
        }

    var informedContenId: String = ""
        set(value) {
            if (value == "") return
            field = value
            getInformedConsentById(value)
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

    val informedConsent:LiveData<InformedConsent>
    private val initInformedConsent = MediatorLiveData<Response<InformedConsent>>()

    val talk:LiveData<Talk>
    private val initTalk = MediatorLiveData<Response<Talk>>()

    val talkResultId:LiveData<String>
    private val saveTalkResult =MediatorLiveData<Resource<Response<String>>>()

    val clickStr:LiveData<String>
    private val clickStrResult =MediatorLiveData<String>()

    init {
        clickStr = clickStrResult.map { it }
        talkResultId = saveTalkResult.map { (it as? Resource.Success)?.data?.result?: ""}
        backToLast = initbackToLast.map { it }
        voiceStart = initVoiceStart.map { it }
        showVoiceTime = initShowVoiceTime.map { it }
        initShowVoiceTime.value = false
        initVoiceStart.value = false
        informedConsent = initInformedConsent.map { it.result?: InformedConsent("") }
        talk = initTalk.map { it?.result?: Talk("") }
    }

    fun loadTalk(){
        if (talkId.isNullOrEmpty()){
            initTalk.value = null
        }else{
            disposable.add(informedApi.getTalkById(talkId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (::getTheTalk,::error))
        }
    }

    fun getTheTalk(response: Response<Talk>){
        initTalk.value = response
        talk.value?.judgeTime()
    }


    fun click(fs:List<File>) {

        if (saveAble){
            saveAble = false
            val files = fs.map {
                return@map MultipartBody.Part.createFormData(
                    "images",
                    it.path.split("/").last(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), it)
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
//                            clickResult.value = true
                            saveTalkResult.value = it
                            messageAction.value = Event("保存成功")
                            initbackToLast.value = true
                            saveAble = true
                        }
                        is Resource.Error -> {
//                            clickResult.value = true
                            messageAction.value = Event(it.throwable.message ?: "")
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
//                            clickResult.value = true
                            saveTalkResult.value = it
                            messageAction.value = Event("保存成功")
                            initbackToLast.value = true
                            saveAble = true
                        }
                        is Resource.Error -> {
//                            clickResult.value = true
                            messageAction.value = Event(it.throwable.message ?: "")
                        }

                    }
                }
            }
        }


    }

    fun getInformedConsentById(id:String){
        disposable.add(informedApi.getInformedConsentById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (::loadInformedConsent,::error))
    }

    fun loadInformedConsent(response: Response<InformedConsent>){
        initInformedConsent.value = response
    }

    fun showOrVoiceTime(){
        initShowVoiceTime.value = !showVoiceTime.value!!
        if (showVoiceTime.value!!) clickStrResult.value = "startVoice" else clickStrResult.value = "endVoice"
    }

    fun changeVoice(){
        initVoiceStart.value = !voiceStart.value!!
    }

    fun clickVoice(){
        checked = !checked
        initVoiceStart.value = checked
    }

}