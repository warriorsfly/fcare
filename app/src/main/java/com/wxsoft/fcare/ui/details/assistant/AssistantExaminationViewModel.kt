package com.wxsoft.fcare.ui.details.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.lis.LisJCRecord
import com.wxsoft.fcare.core.data.entity.lis.LisRecord
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
import com.wxsoft.fcare.core.data.remote.DictEnumApi
import com.wxsoft.fcare.core.data.remote.LISApi
import com.wxsoft.fcare.core.utils.map
import com.wxsoft.fcare.ui.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AssistantExaminationViewModel @Inject constructor(private val lisApi: LISApi,
                                                        override val sharedPreferenceStorage: SharedPreferenceStorage,
                                                        override val gon: Gson
) : BaseViewModel(sharedPreferenceStorage,gon){

    /**
     * 病人id
     */
    var patientId: String = ""
        set(value) {
            if (value == "") return
            field = value
            loadJYResults()
            loadJCResults()
        }

    val lisRecords: LiveData<List<LisRecord>>
    private val loadLisRecordsResult = MediatorLiveData<List<LisRecord>>()

    val lisJCRecords: LiveData<LisJCRecord>
    private val loadLisJCRecordsResult = MediatorLiveData<LisJCRecord>()


    init {
        lisRecords  = loadLisRecordsResult.map { it?: emptyList()}
        lisJCRecords = loadLisJCRecordsResult.map { it?:LisJCRecord("") }
    }


    fun loadJYResults(){
        disposable.add(lisApi.getJYResults(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::getYJresult,::error))
    }
    private fun getYJresult(response:Response<List<LisRecord>>){
        loadLisRecordsResult.value = response.result
    }

    fun loadJCResults(){
        disposable.add(lisApi.getJCResults(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::getYCresult,::error))
    }
    private fun getYCresult(response:Response<LisJCRecord>){
        loadLisJCRecordsResult.value = response.result
    }
}