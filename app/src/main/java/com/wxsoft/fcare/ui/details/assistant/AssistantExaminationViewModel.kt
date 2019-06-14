package com.wxsoft.fcare.ui.details.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.lis.LisJCRecord
import com.wxsoft.fcare.core.data.entity.lis.LisType
import com.wxsoft.fcare.core.data.prefs.SharedPreferenceStorage
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

    var show:Boolean = true

    val jcjyShow: LiveData<Boolean>
    val loadJcjyShow = MediatorLiveData<Boolean>()

    val lisRecords: LiveData<List<LisType>>
    private val loadLisRecordsResult = MediatorLiveData<List<LisType>>()

    val lisJCRecords: LiveData<List<LisJCRecord>>
    private val loadLisJCRecordsResult = MediatorLiveData<List<LisJCRecord>>()


    init {
        jcjyShow= loadJcjyShow.map { it }
        lisRecords  = loadLisRecordsResult.map { it?: emptyList()}
        lisJCRecords = loadLisJCRecordsResult.map { it?: emptyList() }
        loadJcjyShow.value = true
    }


    fun loadJYResults(){
        disposable.add(lisApi.getJYResults(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::getYJresult,::error))
    }
    private fun getYJresult(response:Response<List<LisType>>){
        loadLisRecordsResult.value = response.result
    }

    fun loadJCResults(){
        disposable.add(lisApi.getJCResults(patientId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::getYCresult,::error))
    }
    private fun getYCresult(response:Response<List<LisJCRecord>>){
        loadLisJCRecordsResult.value = response.result
    }


    fun changeTitle(){
        show = !show
        loadJcjyShow.value = show
    }

}