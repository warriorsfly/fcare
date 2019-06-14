package com.wxsoft.fcare.ui.details.ecg

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Ecg
import com.wxsoft.fcare.core.data.entity.EcgDiagnose
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.ECGApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class EcgViewModel @Inject constructor(private val api: ECGApi,
                                       private val dictApi:DictEnumApi,
                                       override val sharedPreferenceStorage: SharedPreferenceStorage,
                                       override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon)  {


    val query=ObservableField<String>()
    val bitmaps= mutableListOf<String>()
    val seleted= mutableListOf<String>()

    var pre=false
    val selectedEcgDiagnosis = mutableListOf<Dictionary>()

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadEcg(field)
            loadCommonDiagnoses(field)
        }

    val ecg:LiveData<Ecg>
    private val loadEcgResult = MediatorLiveData<Response<Ecg>>()
    val uploading:LiveData<Boolean>
    private val uploadResult = MediatorLiveData<Boolean>()

    val diagnoses:LiveData<List<Dictionary>>
    private val loadDiagnoseResult = MediatorLiveData<List<Dictionary>>()
    val selectedDiagnoseResult = MediatorLiveData<Dictionary>()
    val diagnosised = MediatorLiveData<Boolean>()
    val saved = MediatorLiveData<Boolean>()
    init {

        uploading = uploadResult.map { it}
        ecg = loadEcgResult.map {
            it?.result ?: Ecg(createrId = account.id).apply {
                location=if(pre)1 else 2
            }
        }
        diagnoses = loadDiagnoseResult.map { it }
    }

    private fun loadEcg(patientId:String){
        disposable.add(api.getPatientEcgs(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doEcg,::error))

    }

    private fun loadCommonDiagnoses(patientId:String){
        disposable.add(dictApi.loadEcgCommonDiagnoses()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doCommonDiagnose,::error))
    }

    private fun doCommonDiagnose(response: List<Dictionary>?){
        bitmaps.clear()
        loadDiagnoseResult.value= response?: emptyList()
    }

    private fun doEcg(response: Response<Ecg>){
        uploadResult.value=false
        bitmaps.clear()
        seleted.clear()
        loadEcgResult.value= response.apply {
            result?.apply {
                selectedEcgDiagnosis.apply{
                    clear()
                    addAll(diagnoses.map { Dictionary(it.code,it.name).apply { checked = true } })}
                val t=diagnoses.joinToString("\n",transform={ (diagnoses.indexOf(it)+1).toString()+"."+it.name})
                diagnoseText=t
                seleted.addAll(diagnoses.map { it.code })
            }
        }

//        diagnoses.value?.filter {
//            seleted.contains(it.id)
//        }?.map { it.checked = true }
    }

    private fun doSavedEcg(response: Response<Ecg>){
        doEcg(response)
        saved.value=true
    }

    private fun doDiagnosed(response: Response<Ecg>){
//        response.let(::doEcg)
        diagnosised.value=true
        loadEcg(patientId)
        loadCommonDiagnoses(patientId)
    }

    fun saveEcg(fs:List<File>){
        val files = fs.map {
            return@map MultipartBody.Part.createFormData(
                "images",
                it.path.split("/").last(),
                RequestBody.create(MediaType.parse("multipart/form-data"), it)
            )
        }
        val item= ecg.value?.apply {
            savable=false
            if(patientId.isEmpty()) {
                patientId = this@EcgViewModel.patientId

            }
        }
        item?.let {
            uploadResult.value=true
            disposable.add(api.save(it, files)
                .flatMap { api.getPatientEcgs(patientId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doSavedEcg
                ) {
                    error(it)
                    uploadResult.value=false
                }
            )
        }
    }

    fun deleteImage(url:String){
        ecg.value?.attachments?.firstOrNull { it.httpUrl==url }?.let {
            api.deleteImage(it.id)
                .flatMap { api.getPatientEcgs(patientId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doSavedEcg,::error)
        }
    }
    fun diagnose(){

        if(selectedEcgDiagnosis.size==0){
            messageAction.value= Event("请添加诊断提示")
            return
        }
       ecg.value?.apply {
           doctorId=account.id
           doctorName=account.trueName
           diagnoses=selectedEcgDiagnosis.map { EcgDiagnose("", it.id, it.itemName) }
           disposable.add(
               api.diagnosed(this)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(::doDiagnosed, ::error)
           )
       }
    }
}