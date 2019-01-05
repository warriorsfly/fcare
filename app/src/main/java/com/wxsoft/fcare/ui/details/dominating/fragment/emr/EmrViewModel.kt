package com.wxsoft.fcare.ui.details.dominating.fragment.emr

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.EmrItem
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.PatientApi
import com.wxsoft.fcare.data.dictionary.ActionRes
import com.wxsoft.fcare.ui.BaseViewModel
import com.wxsoft.fcare.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EmrViewModel @Inject constructor(private val patientApi: PatientApi,
                                       override val sharedPreferenceStorage: SharedPreferenceStorage,
                                       gson: Gson) : BaseViewModel(sharedPreferenceStorage,gson) {

    val emrs:LiveData<List<EmrItem>>
    private val loadEmrResult=MediatorLiveData<Response<List<EmrItem>>>()

    init {

        emrs=loadEmrResult.map { it.result?: emptyList()}
    }
    var patientId=""
        set(value) {
           field=value
            loadEmrs(field)
        }


    fun loadEmrs(id:String){
        patientApi.getEmrs(id).zipWith(patientApi.getOne(patientId))
            .subscribeOn(Schedulers.computation())
            .doOnSuccess {zip->
                val list= zip.first.result
                list?.first{it.code==ActionRes.ActionType.患者信息录入}?.result=zip.second.result
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ it->

                loadEmrResult.value=it?.first
        }

    }
}
