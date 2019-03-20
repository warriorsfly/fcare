package com.wxsoft.fcare.ui.details.ecg

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Ecg
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.ECGApi
import com.wxsoft.fcare.core.result.Event
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.utils.ActionType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class EcgViewModel @Inject constructor(private val api: ECGApi,
                                       override val sharedPreferenceStorage: SharedPreferenceStorage,
                                       override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon)  {

    val bitmaps= mutableListOf<String>()
    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadEcg()
        }

    val ecg:LiveData<Ecg>
    private val loadEcgResult = MediatorLiveData<Response<Ecg>>()

    init {
        ecg = loadEcgResult.map { it?.result ?: Ecg(createrId = account.id)  }
    }

    private fun loadEcg(){
        disposable.add(api.getPatientEcgs(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::doEcg,::error))

    }

    private fun doEcg(response: Response<Ecg>){
        bitmaps.clear()
        loadEcgResult.value= response
    }

    fun saveEcg(){
        val files = bitmaps.map {
            val file = File(it)
            return@map MultipartBody.Part.createFormData(
                "images",
                it.split("/").last(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            )
        }
        val item= ecg.value?.apply {
            savable=false
            if(patientId.isEmpty()) {
                patientId = this@EcgViewModel.patientId

            }
        }
        item?.let {
            disposable.add(api.save(it, files)
                .flatMap { api.getPatientEcgs(patientId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::doEcg,::error)

            )

        }
    }

}